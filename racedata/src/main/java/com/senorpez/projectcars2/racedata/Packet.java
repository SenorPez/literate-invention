package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;

abstract class Packet {
    private final long packetNumber;
    private final long categoryPacketNumber;
    private final short partialPacketIndex;
    private final short partialPacketNumber;
    private final short packetType;
    private final short packetVersion;

    Packet(final ByteBuffer data) throws InvalidPacketTypeException {
        this.packetNumber = readUnsignedInt(data);
        this.categoryPacketNumber = readUnsignedInt(data);
        this.partialPacketIndex = readUnsignedByte(data);
        this.partialPacketNumber = readUnsignedByte(data);

        final short packetTypeValue = readUnsignedByte(data);
        if (packetTypeValue >= PacketType.PACKET_MAX.ordinal()
                || packetTypeValue < 0) {
            throw new InvalidPacketTypeException();
        } else {
            this.packetType = packetTypeValue;
        }
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

    static short readUnsignedByte(final ByteBuffer data) {
        final byte[] bytes = new byte[1];
        data.get(bytes);
        return (short) (bytes[0] & 0xFF);
    }

    static long readUnsignedInt(final ByteBuffer data) {
        final byte[] bytes = new byte[4];
        data.get(bytes);
        return (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24);
    }


}
