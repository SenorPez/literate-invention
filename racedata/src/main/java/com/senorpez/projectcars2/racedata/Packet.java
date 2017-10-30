package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;

abstract class Packet {
    private final long packetNumber;
    private final long categoryPacketNumber;
    private final short partialPacketIndex;
    private final short partialPacketNumber;
    private final short packetType;
    private final short packetVersion;

    Packet(final ByteBuffer data) {
        this.packetNumber = readUnsignedInt(data);
        this.categoryPacketNumber = readUnsignedInt(data);
        this.partialPacketIndex = readUnsignedByte(data);
        this.partialPacketNumber = readUnsignedByte(data);
        this.packetType = readUnsignedByte(data);
        this.packetVersion = readUnsignedByte(data);
    }

    long getPacketNumber() {
        return packetNumber;
    }

    long getCategoryPacketNumber() {
        return categoryPacketNumber;
    }

    short getPartialPacketIndex() {
        return partialPacketIndex;
    }

    short getPartialPacketNumber() {
        return partialPacketNumber;
    }

    short getPacketType() {
        return packetType;
    }

    short getPacketVersion() {
        return packetVersion;
    }

    private static short readUnsignedByte(final ByteBuffer data) {
        final byte[] bytes = new byte[1];
        data.get(bytes);
        return (short) (bytes[0] & 0xFF);
    }

    private static long readUnsignedInt(final ByteBuffer data) {
        final byte[] bytes = new byte[4];
        data.get(bytes);
        return (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24);
    }


}
