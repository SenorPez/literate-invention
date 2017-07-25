package com.senorpez.projectcars.racedata;

import java.io.DataInputStream;
import java.io.IOException;

abstract public class Packet {
    private final Integer buildVersionNumber;
    private final Short packetTypeCount;

    Packet(final DataInputStream data) throws IOException {
        this.buildVersionNumber = data.readUnsignedShort();
        this.packetTypeCount = (short) data.readUnsignedByte();
    }

    Integer getBuildVersionNumber() {
        return buildVersionNumber;
    }

    abstract public PacketType getPacketType();

    Short getCount() {
        return Integer.valueOf(packetTypeCount >>> 2).shortValue();
    }

    Boolean isCorrectPacketType(final PacketType packetType) {
        final Integer mask = 3; /* 0000 0011 */
        return PacketType.valueOf(mask & packetTypeCount) == packetType;
    }
}
