package com.senorpez.projectcars2.racedata;

import org.junit.Test;

import java.nio.ByteBuffer;

import static com.senorpez.projectcars2.racedata.PacketBuilder.MAX_UNSIGNED_INTEGER;
import static com.senorpez.projectcars2.racedata.PacketBuilder.MIN_UNSIGNED_INTEGER;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ParticipantsPacketTest {
    private ParticipantsPacket packet;

    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_PARTICIPANTS.ordinal());
        packet = new ParticipantsPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder()
                .setExpectedPacketType((short) 5);
        packet = new ParticipantsPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new ParticipantsPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new ParticipantsPacket(builder.build());
    }


    @Test
    public void getParticipantsChangedTimestamp() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder();
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(builder.getExpectedParticipantsChangedTimestamp()));
    }

    @Test
    public void getParticipantsChangedTimestamp_MaxValue() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MAX_UNSIGNED_INTEGER);
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MAX_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp((byte) (MAX_UNSIGNED_INTEGER + 1));
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MAX_UNSIGNED_INTEGER + 1)));
    }

    @Test
    public void getParticipantsChangedTimestamp_Min_Value() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MIN_UNSIGNED_INTEGER);
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MIN_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp((byte) (MIN_UNSIGNED_INTEGER - 1));
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MIN_UNSIGNED_INTEGER - 1)));
    }

    @Test
    public void getNames() throws Exception {
        final ParticipantsPacketBuilder builder = new ParticipantsPacketBuilder();
        packet = new ParticipantsPacket(builder.build());
        assertThat(packet.getNames(), is(builder.getExpectedNames()));
    }
}
