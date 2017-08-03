package com.senorpez.projectcars.racedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

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

    DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = build(new ByteArrayOutputStream(1028));
        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    ByteArrayOutputStream build(final ByteArrayOutputStream stream) throws Exception {
        final DataOutputStream dataOutputStream = new DataOutputStream(stream);

        final Integer packetTypeMask = 3; /* 0000 0011 */
        dataOutputStream.writeShort(expectedBuildVersionNumber);
        dataOutputStream.writeByte((expectedCount << 2) | (packetTypeMask & expectedPacketType));
        return stream;
    }

    static byte[] flipFloat(final Float input) {
        final ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(input);
        buffer.rewind();

        final byte[] bytes = new byte[4];
        buffer.get(bytes);
        return bytes;
    }
}


