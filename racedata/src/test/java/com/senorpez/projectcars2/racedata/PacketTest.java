package com.senorpez.projectcars2.racedata;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PacketTest {
    private Packet packet;

    @Test
    public void getPacketNumber() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketNumber(), is(builder.getExpectedPacketNumber()));
    }

    @Test
    public void getPacketNumber_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketNumber(PacketBuilder.MAX_UNSIGNED_INTEGER);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketNumber(), is(PacketBuilder.MAX_UNSIGNED_INTEGER));

        builder.setExpectedPacketNumber(PacketBuilder.MAX_UNSIGNED_INTEGER + 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketNumber(), is(not(PacketBuilder.MAX_UNSIGNED_INTEGER + 1)));
    }

    @Test
    public void getPacketNumber_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketNumber(PacketBuilder.MIN_UNSIGNED_INTEGER);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketNumber(), is(PacketBuilder.MIN_UNSIGNED_INTEGER));

        builder.setExpectedPacketNumber(PacketBuilder.MIN_UNSIGNED_INTEGER - 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketNumber(), is(not(PacketBuilder.MIN_UNSIGNED_INTEGER - 1)));
    }

    @Test
    public void getCategoryPacketNumber() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCategoryPacketNumber(), is(builder.getExpectedCategoryPacketNumber()));
    }

    @Test
    public void getCategoryPacketNumber_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedCategoryPacketNumber(PacketBuilder.MAX_UNSIGNED_INTEGER);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCategoryPacketNumber(), is(PacketBuilder.MAX_UNSIGNED_INTEGER));

        builder.setExpectedCategoryPacketNumber(PacketBuilder.MAX_UNSIGNED_INTEGER + 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCategoryPacketNumber(), is(not(PacketBuilder.MAX_UNSIGNED_INTEGER + 1)));
    }

    @Test
    public void getCategoryPacketNumber_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedCategoryPacketNumber(PacketBuilder.MIN_UNSIGNED_INTEGER);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCategoryPacketNumber(), is(PacketBuilder.MIN_UNSIGNED_INTEGER));

        builder.setExpectedCategoryPacketNumber(PacketBuilder.MIN_UNSIGNED_INTEGER - 1);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getCategoryPacketNumber(), is(not(PacketBuilder.MIN_UNSIGNED_INTEGER - 1)));
    }

    @Test
    public void getPartialPacketIndex() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketIndex(), is(builder.getExpectedPartialPacketIndex()));
    }

    @Test
    public void getPartialPacketIndex_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPartialPacketIndex(PacketBuilder.MAX_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketIndex(), is(PacketBuilder.MAX_UNSIGNED_BYTE));

        builder.setExpectedPartialPacketIndex((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketIndex(), is(not(PacketBuilder.MAX_UNSIGNED_BYTE + 1)));
    }

    @Test
    public void getPartialPacketIndex_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPartialPacketIndex(PacketBuilder.MIN_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketIndex(), is(PacketBuilder.MIN_UNSIGNED_BYTE));

        builder.setExpectedPartialPacketIndex((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketIndex(), is(not(PacketBuilder.MIN_UNSIGNED_BYTE - 1)));
    }

    @Test
    public void getPartialPacketNumber() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketNumber(), is(builder.getExpectedPartialPacketNumber()));
    }

    @Test
    public void getPartialPacketNumber_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPartialPacketNumber(PacketBuilder.MAX_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketNumber(), is(PacketBuilder.MAX_UNSIGNED_BYTE));

        builder.setExpectedPartialPacketNumber((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketNumber(), is(not(PacketBuilder.MAX_UNSIGNED_BYTE + 1)));
    }

    @Test
    public void getPartialPacketNumber_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPartialPacketNumber(PacketBuilder.MIN_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketNumber(), is(PacketBuilder.MIN_UNSIGNED_BYTE));

        builder.setExpectedPartialPacketNumber((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPartialPacketNumber(), is(not(PacketBuilder.MIN_UNSIGNED_BYTE - 1)));
    }

    @Test
    public void getPacketType() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketType(), is(builder.getExpectedPacketType()));
    }

    @Test
    public void getPacketType_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketType(PacketBuilder.MAX_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketType(), is(PacketBuilder.MAX_UNSIGNED_BYTE));

        builder.setExpectedPacketType((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketType(), is(not(PacketBuilder.MAX_UNSIGNED_BYTE + 1)));
    }

    @Test
    public void getPacketType_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketType(PacketBuilder.MIN_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketType(), is(PacketBuilder.MIN_UNSIGNED_BYTE));

        builder.setExpectedPacketType((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketType(), is(not(PacketBuilder.MIN_UNSIGNED_BYTE - 1)));
    }

    @Test
    public void getPacketVersion() throws Exception {
        final PacketBuilder builder = new PacketBuilder();
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketVersion(), is(builder.getExpectedPacketVersion()));
    }

    @Test
    public void getPacketVersion_MaxValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketVersion(PacketBuilder.MAX_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketVersion(), is(PacketBuilder.MAX_UNSIGNED_BYTE));

        builder.setExpectedPacketVersion((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketVersion(), is(not(PacketBuilder.MAX_UNSIGNED_BYTE + 1)));
    }

    @Test
    public void getPacketVersion_MinValue() throws Exception {
        final PacketBuilder builder = new PacketBuilder()
                .setExpectedPacketVersion(PacketBuilder.MIN_UNSIGNED_BYTE);
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketVersion(), is(PacketBuilder.MIN_UNSIGNED_BYTE));

        builder.setExpectedPacketVersion((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1));
        packet = new PacketImpl(builder.build());
        assertThat(packet.getPacketVersion(), is(not(PacketBuilder.MIN_UNSIGNED_BYTE - 1)));
    }

    private class PacketImpl extends Packet {
        private PacketImpl(final ByteBuffer data) {
            super(data);
        }
    }
}
