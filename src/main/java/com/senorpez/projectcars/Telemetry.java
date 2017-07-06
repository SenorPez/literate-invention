package com.senorpez.projectcars;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.*;

public class Telemetry implements Iterator<Packet> {
    private final DataInputStream telemetryData;

    public Telemetry(Path telemetryFile) {
        DataInputStream telemetryData = null;
        try {
            telemetryData = new DataInputStream(new BufferedInputStream(new FileInputStream(telemetryFile.toFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.telemetryData = telemetryData;
    }

    public static void main(String[] args) {
        Telemetry telemetry = new Telemetry(Paths.get(args[0]));
        while (telemetry.hasNext()) {
            Race race = new Race(telemetry);

            while (race.hasNext()) {
                race.next();
                System.out.println(race.getElapsedTime());
            }
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return telemetryData.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Packet next() {
        try {
            PacketType packetType = PacketType.fromLength(telemetryData.readShort());
            return packetType.getPacket(telemetryData);
        } catch (IOException e) {
            return null;
        }
    }

    public static void convertDirectory(Path telemetryDirectory, Path outputFile) {
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
                            byte[] fileData = Files.readAllBytes(path);
                            System.out.println(path.toString());

                            ByteBuffer buffer = ByteBuffer.allocate(2);
                            buffer.putShort((short) fileData.length);
                            Files.write(outputFile, buffer.array(), APPEND);
                            Files.write(outputFile, fileData, APPEND);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mark() {
        telemetryData.mark(Integer.MAX_VALUE);
    }

    public void reset() {
        try {
            telemetryData.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
