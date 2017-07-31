package com.senorpez.projectcars.racedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;

class PacketBuilder {
    private final static Random random = new Random();

    final static Integer MAX_UNSIGNED_SHORT = Integer.MAX_VALUE >>> 15;
    final static Integer MIN_UNSIGNED_SHORT = 0;

    final static Short MAX_COUNT = Byte.MAX_VALUE >>> 1;
    final static Short MIN_COUNT = 0;

    private Integer expectedBuildVersionNumber = random.nextInt(MAX_UNSIGNED_SHORT + 1);
    private final static Short expectedPacketType = (short) random.nextInt(3);
    private Short expectedCount = (short) random.nextInt(MAX_COUNT + 1);

    private Packet packet;

    PacketBuilder() {
    }

    Integer getExpectedBuildVersionNumber() {
        return expectedBuildVersionNumber;
    }

    PacketBuilder setExpectedBuildVersionNumber(final Integer expectedBuildVersionNumber) {
        this.expectedBuildVersionNumber = expectedBuildVersionNumber;
        return this;
    }

    Short getExpectedCount() {
        return expectedCount;
    }

    PacketBuilder setExpectedCount(final Short expectedCount) {
        this.expectedCount = expectedCount;
        return this;
    }

    DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1028);
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        final Integer packetTypeMask = 3; /* 0000 0011 */

        dataOutputStream.writeShort(expectedBuildVersionNumber);
        dataOutputStream.writeByte((expectedCount << 2) | (packetTypeMask & expectedPacketType));
        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
}


