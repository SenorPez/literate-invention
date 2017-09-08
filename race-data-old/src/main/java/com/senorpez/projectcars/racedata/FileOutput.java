package com.senorpez.projectcars.racedata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.util.concurrent.atomic.AtomicInteger;

public class FileOutput implements Runnable {
    Pipe.SourceChannel inputChannel;
    FileChannel outputChannel;
    ByteBuffer buffer = ByteBuffer.allocate(2048);
    AtomicInteger packetNumber = new AtomicInteger(0);

    public FileOutput(FileChannel outputChannel, Pipe.SourceChannel inputChannel) {
        this.inputChannel = inputChannel;
        this.outputChannel = outputChannel;
        buffer.clear();
    }

    @Override
    public void run() {
        while (true) {
            try {
                int length = inputChannel.read(buffer);
                buffer.flip();

                if (length > 0) {
                    outputChannel.write(buffer);
                    System.out.println("Writing packet: " + packetNumber.incrementAndGet());
                }
                buffer.clear();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
