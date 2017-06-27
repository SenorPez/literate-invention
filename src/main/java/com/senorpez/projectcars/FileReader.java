package com.senorpez.projectcars;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class FileReader {
    public static void main(String[] args) {
        final Path inputFile = Paths.get(args[0]);
        final AtomicInteger count = new AtomicInteger(0);

        try {
            final FileInputStream inputStream = new FileInputStream(inputFile.toFile());
            while (inputStream.available() > 0) {
                byte[] packetLength = new byte[2];
                inputStream.read(packetLength);
                Short packetLengthShort = ByteBuffer.wrap(packetLength).getShort();
                System.out.printf("%d: %d\n", count.getAndIncrement(), packetLengthShort);

                inputStream.skip(packetLengthShort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
