package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.file.StandardOpenOption.*;

class FileWriter implements Runnable {
    private final BlockingQueue<DatagramPacket> queue;
    private final OutputStream outputStream;
    private int bytesWritten = 0;

    public Thread shutdown = new Thread(() -> System.out.println("SHUTDOWN"));

    FileWriter(final BlockingQueue<DatagramPacket> queue, final Path outputFile) throws IOException {
        this.queue = queue;
        this.outputStream = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING);
    }

    @Override
    public void run() {
        System.out.println("AWAY WE GO");
        while (true) {
            try {
                final DatagramPacket packet = queue.take();

                final ByteBuffer lengthBuffer = ByteBuffer.allocate(2).order(LITTLE_ENDIAN);
                lengthBuffer.putShort((short) packet.getLength());
                outputStream.write(lengthBuffer.array());
                outputStream.write(packet.getData(), 0, packet.getLength());

                bytesWritten += packet.getLength() + 2;
                System.out.println("Bytes Written: " + bytesWritten);
            } catch (final InterruptedException e) {
                try {
                    System.out.println("HERE WE ARE");
                    outputStream.close();
                } catch (final IOException ignored) {
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
