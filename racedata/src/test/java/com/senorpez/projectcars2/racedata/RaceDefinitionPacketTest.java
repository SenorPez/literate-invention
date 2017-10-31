package com.senorpez.projectcars2.racedata;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RaceDefinitionPacketTest {
    private RaceDefinitionPacket packet;

    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_RACE_DEFINITION.ordinal());
        packet = new RaceDefinitionPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedPacketType((short) 5);
        packet = new RaceDefinitionPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new RaceDefinitionPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new RaceDefinitionPacket(builder.build());
    }

    @Test
    public void getWorldFastestLapTime() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getWorldFastestLapTime(), is(builder.getExpectedWorldFastestLapTime()));
    }

    @Test
    public void getPersonalFastestLapTime() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getPersonalFastestLapTime(), is(builder.getExpectedPersonalFastestLapTime()));
    }

    @Test
    public void getPersonalFastestSector1Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getPersonalFastestSector1Time(), is(builder.getExpectedPersonalFastestSector1Time()));
    }

    @Test
    public void getPersonalFastestSector2Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getPersonalFastestSector2Time(), is(builder.getExpectedPersonalFastestSector2Time()));
    }

    @Test
    public void getPersonalFastestSector3Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getPersonalFastestSector3Time(), is(builder.getExpectedPersonalFastestSector3Time()));
    }

    @Test
    public void getWorldFastestSector1Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getWorldFastestSector1Time(), is(builder.getExpectedWorldFastestSector1Time()));
    }

    @Test
    public void getWorldFastestSector2Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getWorldFastestSector2Time(), is(builder.getExpectedWorldFastestSector2Time()));
    }

    @Test
    public void getWorldFastestSector3Time() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getWorldFastestSector3Time(), is(builder.getExpectedWorldFastestSector3Time()));
    }

    @Test
    public void getTrackLength() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTrackLength(), is(builder.getExpectedTrackLength()));
    }

    @Test
    public void getTrackLocation() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTrackLocation(), is(builder.getExpectedTrackLocation()));
    }

    @Test
    public void getTrackVariation() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTrackVariation(), is(builder.getExpectedTrackVariation()));
    }

    @Test
    public void getTranslatedTrackLocation() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTranslatedTrackLocation(), is(builder.getExpectedTranslatedTrackLocation()));
    }

    @Test
    public void getTranslatedTrackVariation() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTranslatedTrackVariation(), is(builder.getExpectedTranslatedTrackVariation()));
    }

    @Test
    public void getIsTimed_True() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(1 << 7);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.isTimedRace(), is(true));
    }

    @Test
    public void getIsTimed_False() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(0);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.isTimedRace(), is(false));
    }

    @Test
    public void getLaps_TimedRace() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(1 << 7);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(nullValue()));
    }

    @Test
    public void getTime_LapRace() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(0);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(nullValue()));
    }

    @Test
    public void getLaps_Value() throws Exception {
        final Random random = new Random();
        final int expectedLaps = random.nextInt(1 << 7);
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(expectedLaps);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(expectedLaps));
    }

    @Test
    public void getLaps_MaxValue() throws Exception {
        int expectedLaps = (1 << 7) - 1;
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(expectedLaps);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(expectedLaps));

        expectedLaps = (1 << 7);
        builder.setExpectedLapsTimeInEvent(expectedLaps);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(not(expectedLaps)));
    }

    @Test
    public void getLaps_MinValue() throws Exception {
        int expectedLaps = 0;
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(expectedLaps);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(expectedLaps));

        expectedLaps = -1;
        builder.setExpectedLapsTimeInEvent(expectedLaps);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getLaps(), is(not(expectedLaps)));
    }

    @Test
    public void getTime_Value() throws Exception {
        final Random random = new Random();
        final int expectedTime = random.nextInt(1 << 7);
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(expectedTime + (1 << 7));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(expectedTime * 5));
    }

    @Test
    public void getTime_MaxValue() throws Exception {
        int expectedTime = (1 << 7) - 1;
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(expectedTime + (1 << 7));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(expectedTime * 5));

        expectedTime = (1 << 7);
        builder.setExpectedLapsTimeInEvent(expectedTime + (1 << 7));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(not(expectedTime * 5)));
    }

    @Test
    public void getTime_MinValue() throws Exception {
        int expectedTime = 0;
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedLapsTimeInEvent(1 << 7);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(expectedTime * 5));

        expectedTime = -1;
        builder.setExpectedLapsTimeInEvent(expectedTime + (1 << 7));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getTime(), is(not(expectedTime * 5)));
    }

    @Test
    public void getEnforcedPitStopLap() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder();
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getEnforcedPitStopLap(), Is.is(builder.getExpectedEnforcedPitStopLap()));
    }

    @Test
    public void getEnforcedPitStopLap_MaxValue() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedEnforcedPitStopLap(Byte.MAX_VALUE);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getEnforcedPitStopLap(), is(Byte.MAX_VALUE));

        builder.setExpectedEnforcedPitStopLap((byte) (Byte.MAX_VALUE + 1));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getEnforcedPitStopLap(), is(not(Byte.MAX_VALUE + 1)));
    }

    @Test
    public void getEnforcedPitStopLap_Min_Value() throws Exception {
        final RaceDefinitionPacketBuilder builder = new RaceDefinitionPacketBuilder()
                .setExpectedEnforcedPitStopLap(Byte.MIN_VALUE);
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getEnforcedPitStopLap(), is(Byte.MIN_VALUE));

        builder.setExpectedEnforcedPitStopLap((byte) (Byte.MIN_VALUE - 1));
        packet = new RaceDefinitionPacket(builder.build());
        assertThat(packet.getEnforcedPitStopLap(), is(not(Byte.MIN_VALUE - 1)));
    }
}
