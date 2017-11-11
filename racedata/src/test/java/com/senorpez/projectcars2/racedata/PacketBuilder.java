package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.stream.IntStream;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.UTF_8;

class PacketBuilder {
    private final static Random random = new Random();
    private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    final static long MAX_UNSIGNED_INTEGER = Long.MAX_VALUE >>> 31;
    final static long MIN_UNSIGNED_INTEGER = 0;
    final static int MAX_UNSIGNED_SHORT = Integer.MAX_VALUE >>> 15;
    final static int MIN_UNSIGNED_SHORT = 0;
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

    @SuppressWarnings("unchecked")
    <B extends PacketBuilder> B setExpectedPacketType(final short expectedPacketType) {
        this.expectedPacketType = expectedPacketType;
        return (B) this;
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

    static void writeUnsignedShort(final int value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Integer.BYTES).order(LITTLE_ENDIAN).putInt(value).array();
        data.put(valueBytes, 0, Short.BYTES);
    }

    static void writeUnsignedInt(final long value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Long.BYTES).order(LITTLE_ENDIAN).putLong(value).array();
        data.put(valueBytes, 0, Integer.BYTES);
    }

    static void writeString(final String value, final int length, final ByteBuffer data) {
        final byte[] nameBuffer = new byte[length];
        final byte[] nameBytes = value.getBytes(UTF_8);
        System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
        data.put(nameBuffer);
    }

    static String generateString(final int maxLength) {
        final int length = random.nextInt(maxLength);
        final StringBuilder nameBuilder = new StringBuilder();
        IntStream.rangeClosed(0, length).forEach(value -> nameBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
        return nameBuilder.toString();
    }
}
