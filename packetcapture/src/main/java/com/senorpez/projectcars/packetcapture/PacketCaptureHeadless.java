package com.senorpez.projectcars.packetcapture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.file.StandardOpenOption.*;

public class PacketCaptureHeadless {
    private static final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(10000);
    private static final Logger logger = LoggerFactory.getLogger(PacketCaptureHeadless.class);

    private static Thread readerThread;
    private static Thread writerThread;

    private static PacketReader packetReader;
    private static PacketWriter packetWriter;

    private static class EndCapture extends Thread {
        public void run() {
            if (readerThread != null) readerThread.interrupt();
            if (writerThread != null) writerThread.interrupt();
            if (packetReader != null) packetReader.cancel();
            if (packetWriter != null) packetWriter.cancel();
            logger.info("Shutting Down");
        }
    }

    public static void main(final String[] args) throws IOException {
        final Path outputFile;
        if (args.length > 1) outputFile = Paths.get(args[1]);
        else {
            final String timeStamp = "packetcapture-"
                    + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())
                    + ".pcars";
            outputFile = Paths.get(timeStamp);
        }

        Runtime.getRuntime().addShutdownHook(new EndCapture());

        logger.info("Launching");

        packetReader = new PacketReader(queue);
        readerThread = new Thread(packetReader, "Reader Thread");
        readerThread.start();

        final Writer writer = new SimplePCAPNGWriter(
                Files.newOutputStream(outputFile,
                        CREATE, TRUNCATE_EXISTING, WRITE));
        packetWriter = new PacketWriter(queue, writer);
        writerThread = new Thread(packetWriter, "Writer Thread");
        writerThread.start();
    }
}
