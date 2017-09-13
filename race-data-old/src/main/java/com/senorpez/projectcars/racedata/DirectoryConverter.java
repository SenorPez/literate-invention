package com.senorpez.projectcars.racedata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.file.StandardOpenOption.*;

public class DirectoryConverter {
    public static void main(final String[] args) {
        assert args.length == 2;

        final Path telemetryDirectory = Paths.get(args[0]);
        final Path outputFile = Paths.get(args[1]);

        try {
            Files.write(outputFile, new byte[]{}, CREATE, TRUNCATE_EXISTING);
            Files.list(telemetryDirectory)
                    .filter(path -> !path.toFile().getName().replaceAll("[^\\d]", "").equals(""))
                    .sorted((o1, o2) -> {
                        Integer n1 = Integer.valueOf(o1.toFile().getName().replaceAll("[^\\d]", ""));
                        Integer n2 = Integer.valueOf(o2.toFile().getName().replaceAll("[^\\d]", ""));
                        return n1.compareTo(n2);
                    })
                    .forEach(path -> {
                        try {
                            final byte[] fileData = Files.readAllBytes(path);
                            System.out.println(path.toString());

                            final ByteBuffer buffer = ByteBuffer.allocate(2).order(LITTLE_ENDIAN);
                            buffer.putShort((short) fileData.length);
                            Files.write(outputFile, buffer.array(), APPEND);
                            Files.write(outputFile, fileData, APPEND);
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
