package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class PacketTest {
    private final static Integer maxUnsignedShort = Integer.MAX_VALUE >>> 15;
    private final static Integer minUnsignedShort = 0;

    private final static Short maxCount = Byte.MAX_VALUE >>> 1;
    private final static Short minCount = 0;

    private final static Integer expectedBuildVersionNumber = 1000;
    private final static Short expectedPacketType = 0;
    private final static Short expectedCount = 42;
    private Packet packet;

    @Test
    public void getBuildVersionNumber() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getBuildVersionNumber(), is(expectedBuildVersionNumber));
    }

    @Test
    public void getBuildVersionNumber_MaxValue() throws Exception {
        packet = new Builder()
                .setBuildVersionNumber(maxUnsignedShort)
                .build();
        assertThat(packet.getBuildVersionNumber(), is(maxUnsignedShort));

        packet = new Builder()
                .setBuildVersionNumber(maxUnsignedShort + 1)
                .build();
        assertThat(packet.getBuildVersionNumber(), is(not(maxUnsignedShort + 1)));
    }

    @Test
    public void getBuildVersionNumber_MinValue() throws Exception {
        packet = new Builder()
                .setBuildVersionNumber(minUnsignedShort)
                .build();
        assertThat(packet.getBuildVersionNumber(), is(minUnsignedShort));

        packet = new Builder()
                .setBuildVersionNumber(minUnsignedShort - 1)
                .build();
        assertThat(packet.getBuildVersionNumber(), is(not(minUnsignedShort - 1)));
    }

    @Test
    public void getCount() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getCount(), is(expectedCount));
    }

    @Test
    public void getCount_MaxValue() throws Exception {
        packet = new Builder()
                .setCount(maxCount)
                .build();
        assertThat(packet.getCount(), is(maxCount));

        packet = new Builder()
                .setCount((short) (maxCount + 1))
                .build();
        assertThat(packet.getCount(), is(not((short) (maxCount + 1))));
    }

    @Test
    public void getCount_MinValue() throws Exception {
        packet = new Builder()
                .setCount(minCount)
                .build();
        assertThat(packet.getCount(), is(minCount));

        packet = new Builder()
                .setCount((short) (minCount - 1))
                .build();
        assertThat(packet.getCount(), is(not((short) (minCount - 1))));
    }

    private class Builder {
        private Integer expectedBuildVersionNumber = PacketTest.expectedBuildVersionNumber;
        private final Short expectedPacketType = PacketTest.expectedPacketType;
        private Short expectedCount = PacketTest.expectedCount;

        private Builder() {
        }

        private Builder setBuildVersionNumber(final Integer buildVersionNumber) {
            this.expectedBuildVersionNumber = buildVersionNumber;
            return this;
        }

        private Builder setCount(final Short count) {
            this.expectedCount = count;
            return this;
        }

        private Packet build() throws Exception {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1028);
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            final Integer packetTypeMask = 3; /* 0000 0011 */

            dataOutputStream.writeShort(expectedBuildVersionNumber);
            dataOutputStream.writeByte((expectedCount << 2) | (packetTypeMask & expectedPacketType));

            final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            return new PacketImpl(dataInputStream);
        }
    }

    private class PacketImpl extends Packet {
        PacketImpl(final DataInputStream data) throws IOException {
            super(data);
        }

        @Override
        public PacketType getPacketType() {
            return null;
        }
    }
}
