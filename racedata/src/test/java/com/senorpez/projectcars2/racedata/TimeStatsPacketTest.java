package com.senorpez.projectcars2.racedata;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketBuilder.MAX_UNSIGNED_INTEGER;
import static com.senorpez.projectcars2.racedata.PacketBuilder.MIN_UNSIGNED_INTEGER;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TimeStatsPacketTest {
    private final static int UDP_STREAMER_PARTICIPANTS_SUPPORTED = 32;

    private TimeStatsPacket packet;

    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_TIME_STATS.ordinal());
        packet = new TimeStatsPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder()
                .setExpectedPacketType((short) 0);
        packet = new TimeStatsPacket(builder.build());
    }
    
    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new TimeStatsPacket(builder.build());
    }
    
    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new TimeStatsPacket(builder.build());
    }

    @Test
    public void getParticipantsChangedTimestamp() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(builder.getExpectedParticipantsChangedTimestamp()));
    }

    @Test
    public void getParticipantsChangedTimestamp_MaxValue() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MAX_UNSIGNED_INTEGER);
        packet = new TimeStatsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MAX_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp((byte) (MAX_UNSIGNED_INTEGER + 1));
        packet = new TimeStatsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MAX_UNSIGNED_INTEGER + 1)));
    }

    @Test
    public void getParticipantsChangedTimestamp_Min_Value() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MIN_UNSIGNED_INTEGER);
        packet = new TimeStatsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MIN_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp((byte) (MIN_UNSIGNED_INTEGER - 1));
        packet = new TimeStatsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MIN_UNSIGNED_INTEGER - 1)));
    }
    
    @Test
    public void getFastestLapTime() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> fastestLapTimes = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getFastestLapTime)
                .collect(Collectors.toList());
        final List<Float> expectedFastestLapTimes = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedFastestLapTime())
                .collect(Collectors.toList());
        assertThat(fastestLapTimes, is(expectedFastestLapTimes));
    }
    
    @Test
    public void getLastLapTime() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> LastLapTimes = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getLastLapTime)
                .collect(Collectors.toList());
        final List<Float> expectedLastLapTimes = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedLastLapTime())
                .collect(Collectors.toList());
        assertThat(LastLapTimes, is(expectedLastLapTimes));
    }
    
    @Test
    public void getLastSectorTime() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> LastSectorTimes = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getLastSectorTime)
                .collect(Collectors.toList());
        final List<Float> expectedLastSectorTimes = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedLastSectorTime())
                .collect(Collectors.toList());
        assertThat(LastSectorTimes, is(expectedLastSectorTimes));
    }
    
    @Test
    public void getFastestSector1Time() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> FastestSector1Times = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getFastestSector1Time)
                .collect(Collectors.toList());
        final List<Float> expectedFastestSector1Times = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedFastestSector1Time())
                .collect(Collectors.toList());
        assertThat(FastestSector1Times, is(expectedFastestSector1Times));
    }
    
    @Test
    public void getFastestSector2Time() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> FastestSector2Times = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getFastestSector2Time)
                .collect(Collectors.toList());
        final List<Float> expectedFastestSector2Times = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedFastestSector2Time())
                .collect(Collectors.toList());
        assertThat(FastestSector2Times, is(expectedFastestSector2Times));
    }
    
    @Test
    public void getFastestSector3Time() throws Exception {
        final TimeStatsPacketBuilder builder = new TimeStatsPacketBuilder();
        packet = new TimeStatsPacket(builder.build());
        final List<Float> FastestSector3Times = packet
                .getStats()
                .getParticipants()
                .stream()
                .map(TimeStatsPacket.ParticipantStats.ParticipantStatsInfo::getFastestSector3Time)
                .collect(Collectors.toList());
        final List<Float> expectedFastestSector3Times = IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> builder.getParticipantStatsBuilder().getParticipantStatsInfoBuilder().getExpectedFastestSector3Time())
                .collect(Collectors.toList());
        assertThat(FastestSector3Times, is(expectedFastestSector3Times));
    }

}
