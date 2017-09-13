package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PacketTest {
    private Packet packet;

    @Test
    public void getBuildVersionNumber() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getBuildVersionNumber(), is(builder.getExpectedBuildVersionNumber()));
    }

    @Test
    public void getBuildVersionNumber_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedBuildVersionNumber(PacketBuilder.MAX_UNSIGNED_SHORT);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getBuildVersionNumber(), is(PacketBuilder.MAX_UNSIGNED_SHORT));

        builder.setExpectedBuildVersionNumber(PacketBuilder.MAX_UNSIGNED_SHORT + 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getBuildVersionNumber(), is(not(PacketBuilder.MAX_UNSIGNED_SHORT + 1)));
    }

    @Test
    public void getBuildVersionNumber_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedBuildVersionNumber(PacketBuilder.MIN_UNSIGNED_SHORT);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getBuildVersionNumber(), is(PacketBuilder.MIN_UNSIGNED_SHORT));

        builder.setExpectedBuildVersionNumber(PacketBuilder.MIN_UNSIGNED_SHORT - 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getBuildVersionNumber(), is(not(PacketBuilder.MIN_UNSIGNED_SHORT - 1)));
    }

    @Test
    public void getCount() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCount(), is(builder.getExpectedCount()));
    }

    @Test
    public void getCount_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedCount(PacketBuilder.MAX_COUNT);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCount(), is(PacketBuilder.MAX_COUNT));

        builder.setExpectedCount((short) (PacketBuilder.MAX_COUNT + 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCount(), is(not((short) (PacketBuilder.MAX_COUNT + 1))));
    }

    @Test
    public void getCount_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedCount(PacketBuilder.MIN_COUNT);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCount(), is(PacketBuilder.MIN_COUNT));

        builder.setExpectedCount((short) (PacketBuilder.MIN_COUNT - 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCount(), is(not((short) (PacketBuilder.MIN_COUNT - 1))));
    }

    private class PacketImpl extends Packet {
        private PacketImpl(final ByteBuffer data) throws IOException {
            super(data);
        }

        @Override
        PacketType getPacketType() {
            return null;
        }
    }
}
