package com.senorpez.projectcars;

import java.io.DataInputStream;
import java.io.IOException;

abstract public class Packet {
    private final Integer buildVersionNumber;
    private final Short packetTypeCount;

    public Packet(DataInputStream data) throws IOException {
        this.buildVersionNumber = data.readUnsignedShort();
        this.packetTypeCount = (short) data.readUnsignedByte();
    }

    public Integer getBuildVersionNumber() {
        return buildVersionNumber;
    }

    abstract public PacketType getPacketType();

    public Short getCount() {
        return Integer.valueOf(packetTypeCount >>> 2).shortValue();
    }

    public Boolean isCorrectPacketType(PacketType packetType) {
        Integer mask = 3; /* 0000 0011 */
        return PacketType.valueOf(mask & packetTypeCount) == packetType;
    }
}
