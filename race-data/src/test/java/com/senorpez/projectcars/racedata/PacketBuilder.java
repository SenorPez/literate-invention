package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.UTF_8;

class PacketBuilder {
    final static Random random = new Random();

    final static Integer MAX_UNSIGNED_SHORT = Integer.MAX_VALUE >>> 15;
    final static Integer MIN_UNSIGNED_SHORT = 0;
    final static Short MAX_UNSIGNED_BYTE = Short.MAX_VALUE >>> 7;
    final static Short MIN_UNSIGNED_BYTE = 0;

    final static Short MAX_COUNT = Byte.MAX_VALUE >>> 1;
    final static Short MIN_COUNT = 0;

    private Integer expectedBuildVersionNumber = random.nextInt(MAX_UNSIGNED_SHORT + 1);
    private Short expectedPacketType = (short) random.nextInt(3);
    private Short expectedCount = (short) random.nextInt(MAX_COUNT + 1);

    PacketBuilder() {
    }

    Integer getExpectedBuildVersionNumber() {
        return expectedBuildVersionNumber;
    }

    PacketBuilder setExpectedBuildVersionNumber(final Integer expectedBuildVersionNumber) {
        this.expectedBuildVersionNumber = expectedBuildVersionNumber;
        return this;
    }

    <T extends PacketBuilder> T setExpectedPacketType(final Short expectedPacketType) {
        this.expectedPacketType = expectedPacketType;
        return (T) this;
    }

    Short getExpectedCount() {
        return expectedCount;
    }

    PacketBuilder setExpectedCount(final Short expectedCount) {
        this.expectedCount = expectedCount;
        return this;
    }

    ByteBuffer build() throws Exception {
        final ByteBuffer data = build(ByteBuffer.allocate(3).order(LITTLE_ENDIAN));
        data.rewind();
        return data;
    }

    ByteBuffer build(final ByteBuffer data) throws Exception {
        final Integer packetTypeMask = 3; /* 0000 0011 */
        writeUnsignedShort(expectedBuildVersionNumber, data);
        writeUnsignedByte((expectedCount << 2) | (packetTypeMask & expectedPacketType), data);
        return data;
    }

    static void writeUnsignedByte(final int value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Integer.BYTES).order(LITTLE_ENDIAN).putInt(value).array();
        data.put(valueBytes, 0, Byte.BYTES);
    }

    static void writeUnsignedShort(final int value, final ByteBuffer data) {
        final byte[] valueBytes = ByteBuffer.allocate(Integer.BYTES).order(LITTLE_ENDIAN).putInt(value).array();
        data.put(valueBytes, 0, Short.BYTES);
    }

    static void writeString(final String value, final ByteBuffer data) {
        final byte[] nameBuffer = new byte[64];
        final byte[] nameBytes = value.getBytes(UTF_8);
        System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
        data.put(nameBuffer);
    }
}


