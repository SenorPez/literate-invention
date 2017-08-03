package com.senorpez.projectcars.racedata;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

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
