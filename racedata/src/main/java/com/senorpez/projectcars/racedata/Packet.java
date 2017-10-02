package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

abstract class Packet {
    private final Integer buildVersionNumber;
    private final Short packetTypeCount;

    Packet(final ByteBuffer data) {
        this.buildVersionNumber = readUnsignedShort(data);
        this.packetTypeCount = readUnsignedByte(data);
    }

    Integer getBuildVersionNumber() {
        return buildVersionNumber;
    }

    abstract PacketType getPacketType();

    Short getCount() {
        return Integer.valueOf(packetTypeCount >>> 2).shortValue();
    }

    Boolean isCorrectPacketType(final PacketType packetType) {
        final Integer mask = 3; /* 0000 0011 */
        return PacketType.valueOf(mask & packetTypeCount) == packetType;
    }

    static Short readUnsignedByte(final ByteBuffer data) {
        final byte[] bytes = new byte[1];
        data.get(bytes);
        return (short) (bytes[0] & 0xFF);
    }

    static Integer readUnsignedShort(final ByteBuffer data) {
        final byte[] bytes = new byte[2];
        data.get(bytes);
        return (bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8);
    }

    static String readString(final ByteBuffer data) {
        final byte[] bytes = new byte[64];
        data.get(bytes);
        return new String(bytes, UTF_8).split("\u0000", 2)[0];
    }
}
