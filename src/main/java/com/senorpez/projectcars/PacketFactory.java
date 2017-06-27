package com.senorpez.projectcars;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class PacketFactory implements Iterator<Packet> {
    private final InputStream telemetryData;

    PacketFactory(DataInputStream telemetryData) {
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
        PacketType packetType;

        try {
            packetType = PacketType.valueOf((int) ((DataInputStream) telemetryData).readShort());
            return packetType.getPacket(((DataInputStream) telemetryData));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
