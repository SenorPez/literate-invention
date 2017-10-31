package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class PacketBuilder {
    private final static Random random = new Random();

    final static long MAX_UNSIGNED_INTEGER = Long.MAX_VALUE >>> 31;
    final static long MIN_UNSIGNED_INTEGER = 0;
    final static short MAX_UNSIGNED_BYTE = Short.MAX_VALUE >>> 7;
    final static short MIN_UNSIGNED_BYTE = 0;

    private long expectedPacketNumber = getBoundedLong();
    private long expectedCategoryPacketNumber = getBoundedLong();
    private short expectedPartialPacketIndex = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedPartialPacketNumber = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedPacketType = (short) random.nextInt(PacketType.PACKET_MAX.ordinal());
    private short expectedPacketVersion = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    
    PacketBuilder() {}

    long getExpectedPacketNumber() {
        return expectedPacketNumber;
    }

    PacketBuilder setExpectedPacketNumber(final long expectedPacketNumber) {
        this.expectedPacketNumber = expectedPacketNumber;
        return this;
    }

    long getExpectedCategoryPacketNumber() {
        return expectedCategoryPacketNumber;
    }

    PacketBuilder setExpectedCategoryPacketNumber(final long expectedCategoryPacketNumber) {
        this.expectedCategoryPacketNumber = expectedCategoryPacketNumber;
        return this;
    }

    short getExpectedPartialPacketIndex() {
        return expectedPartialPacketIndex;
    }

    PacketBuilder setExpectedPartialPacketIndex(final short expectedPartialPacketIndex) {
        this.expectedPartialPacketIndex = expectedPartialPacketIndex;
        return this;
    }

    short getExpectedPartialPacketNumber() {
        return expectedPartialPacketNumber;
    }

    PacketBuilder setExpectedPartialPacketNumber(final short expectedPartialPacketNumber) {
        this.expectedPartialPacketNumber = expectedPartialPacketNumber;
        return this;
    }

    short getExpectedPacketType() {
        return expectedPacketType;
    }

    PacketBuilder setExpectedPacketType(final short expectedPacketType) {
        this.expectedPacketType = expectedPacketType;
        return this;
    }

    short getExpectedPacketVersion() {
        return expectedPacketVersion;
    }

    PacketBuilder setExpectedPacketVersion(final short expectedPacketVersion) {
        this.expectedPacketVersion = expectedPacketVersion;
        return this;
    }

    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(12).order(LITTLE_ENDIAN));
        data.rewind();
        return data;
    }

    ByteBuffer build(final ByteBuffer data) {
        writeUnsignedInt(expectedPacketNumber, data);
        writeUnsignedInt(expectedCategoryPacketNumber, data);
        writeUnsignedByte(expectedPartialPacketIndex, data);
        writeUnsignedByte(expectedPartialPacketNumber, data);
        writeUnsignedByte(expectedPacketType, data);
        writeUnsignedByte(expectedPacketVersion, data);
        return data;
    }

    static long getBoundedLong() {
        return (long) (Math.random() * (MAX_UNSIGNED_INTEGER));
    }

    static void writeUnsignedByte(final short value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Short.BYTES).order(LITTLE_ENDIAN).putShort(value).array();
        data.put(valueBytes, 0, Byte.BYTES);
    }

    static void writeUnsignedInt(final long value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Long.BYTES).order(LITTLE_ENDIAN).putLong(value).array();
        data.put(valueBytes, 0, Integer.BYTES);
    }
}
