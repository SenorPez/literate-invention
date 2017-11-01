package com.senorpez.projectcars2.racedata;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketBuilder.*;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TimingsPacketTest {
    private TimingsPacket packet;
    
    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_TIMINGS.ordinal());
        packet = new TimingsPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedPacketType((short) 0);
        packet = new TimingsPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new TimingsPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new TimingsPacket(builder.build());
    }

    @Test
    public void getNumParticipants() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getNumParticipants(), is(builder.getExpectedNumParticipants()));
    }

    @Test
    public void getNumParticipants_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedNumParticipants(Byte.MAX_VALUE);
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getNumParticipants(), is(Byte.MAX_VALUE));

        builder.setExpectedNumParticipants((byte) (Byte.MAX_VALUE + 1));
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getNumParticipants(), is(not(Byte.MAX_VALUE + 1)));
    }

    @Test
    public void getNumParticipants_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedNumParticipants(Byte.MIN_VALUE);
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getNumParticipants(), is(Byte.MIN_VALUE));

        builder.setExpectedNumParticipants((byte) (Byte.MIN_VALUE - 1));
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getNumParticipants(), is(not(Byte.MIN_VALUE - 1)));
    }

    @Test
    public void getParticipantsChangedTimestamp() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(builder.getExpectedParticipantsChangedTimestamp()));
    }

    @Test
    public void getParticipantsChangedTimestamp_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MAX_UNSIGNED_INTEGER);
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MAX_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp((byte) (MAX_UNSIGNED_INTEGER + 1));
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MAX_UNSIGNED_INTEGER + 1)));
    }

    @Test
    public void getParticipantsChangedTimestamp_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder()
                .setExpectedParticipantsChangedTimestamp(MIN_UNSIGNED_INTEGER);
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(MIN_UNSIGNED_INTEGER));

        builder.setExpectedParticipantsChangedTimestamp(MIN_UNSIGNED_INTEGER - 1);
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getParticipantsChangedTimestamp(), is(not(MIN_UNSIGNED_INTEGER - 1)));
    }

    @Test
    public void getEventTimeRemaining() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getEventTimeRemaining(), is(builder.getExpectedEventTimeRemaining()));
    }

    @Test
    public void getSplitTimeAhead() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getSplitTimeAhead(), is(builder.getExpectedSplitTimeAhead()));
    }

    @Test
    public void getSplitTimeBehind() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getSplitTimeBehind(), is(builder.getExpectedSplitTimeBehind()));
    }

    @Test
    public void getSplitTime() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        assertThat(packet.getSplitTime(), is(builder.getExpectedSplitTime()));
    }

    @Test
    public void getWorldPosition() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<List<Short>> worldPositions = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                .collect(Collectors.toList());
        final List<List<Short>> expectedWorldPositions = IntStream
                .range(0, worldPositions.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedWorldPosition())
                .collect(Collectors.toList());
        assertThat(worldPositions, is(expectedWorldPositions));
    }

    @Test
    public void getWorldPosition_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedWorldPosition(Arrays.asList(
                Short.MAX_VALUE,
                Short.MAX_VALUE,
                Short.MAX_VALUE)
        );
        packet = new TimingsPacket(builder.build());
        List<List<Short>> worldPositions = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                .collect(Collectors.toList());
        final List<List<Short>> expectedWorldPositions = IntStream
                .range(0, worldPositions.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedWorldPosition())
                .collect(Collectors.toList());
        assertThat(worldPositions, is(expectedWorldPositions));

        builder.getParticipantInfoBuilder().setExpectedWorldPosition(Arrays.asList(
                (short) (Short.MAX_VALUE + 1),
                (short) (Short.MAX_VALUE + 1),
                (short) (Short.MAX_VALUE + 1)
        ));
        packet = new TimingsPacket(builder.build());
        worldPositions = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                .collect(Collectors.toList());
        final List<List<Integer>> overflowPositions = IntStream
                .range(0, worldPositions.size())
                .mapToObj(v -> Arrays.asList(
                        (Short.MAX_VALUE + 1),
                        (Short.MAX_VALUE + 1),
                        (Short.MAX_VALUE + 1)
                ))
                .collect(Collectors.toList());
        assertThat(worldPositions, is(not(overflowPositions)));
    }

    @Test
    public void getWorldPosition_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedWorldPosition(Arrays.asList(
                Short.MIN_VALUE,
                Short.MIN_VALUE,
                Short.MIN_VALUE)
        );
        packet = new TimingsPacket(builder.build());
        List<List<Short>> worldPositions = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                .collect(Collectors.toList());
        final List<List<Short>> expectedWorldPositions = IntStream
                .range(0, worldPositions.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedWorldPosition())
                .collect(Collectors.toList());
        assertThat(worldPositions, is(expectedWorldPositions));

        builder.getParticipantInfoBuilder().setExpectedWorldPosition(Arrays.asList(
                (short) (Short.MIN_VALUE - 1),
                (short) (Short.MIN_VALUE - 1),
                (short) (Short.MIN_VALUE - 1)
        ));
        packet = new TimingsPacket(builder.build());
        worldPositions = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                .collect(Collectors.toList());
        final List<List<Integer>> overflowPositions = IntStream
                .range(0, worldPositions.size())
                .mapToObj(v -> Arrays.asList(
                        (Short.MIN_VALUE - 1),
                        (Short.MIN_VALUE - 1),
                        (Short.MIN_VALUE - 1)
                ))
                .collect(Collectors.toList());
        assertThat(worldPositions, is(not(overflowPositions)));
    }

    @Test
    public void getOrientation() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<List<Short>> Orientations = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getOrientation)
                .collect(Collectors.toList());
        final List<List<Short>> expectedOrientations = IntStream
                .range(0, Orientations.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedOrientation())
                .collect(Collectors.toList());
        assertThat(Orientations, is(expectedOrientations));
    }

    @Test
    public void getOrientation_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedOrientation(Arrays.asList(
                Short.MAX_VALUE,
                Short.MAX_VALUE,
                Short.MAX_VALUE)
        );
        packet = new TimingsPacket(builder.build());
        List<List<Short>> Orientations = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getOrientation)
                .collect(Collectors.toList());
        final List<List<Short>> expectedOrientations = IntStream
                .range(0, Orientations.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedOrientation())
                .collect(Collectors.toList());
        assertThat(Orientations, is(expectedOrientations));

        builder.getParticipantInfoBuilder().setExpectedOrientation(Arrays.asList(
                (short) (Short.MAX_VALUE + 1),
                (short) (Short.MAX_VALUE + 1),
                (short) (Short.MAX_VALUE + 1)
        ));
        packet = new TimingsPacket(builder.build());
        Orientations = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getOrientation)
                .collect(Collectors.toList());
        final List<List<Integer>> overflowPositions = IntStream
                .range(0, Orientations.size())
                .mapToObj(v -> Arrays.asList(
                        (Short.MAX_VALUE + 1),
                        (Short.MAX_VALUE + 1),
                        (Short.MAX_VALUE + 1)
                ))
                .collect(Collectors.toList());
        assertThat(Orientations, is(not(overflowPositions)));
    }

    @Test
    public void getOrientation_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedOrientation(Arrays.asList(
                Short.MIN_VALUE,
                Short.MIN_VALUE,
                Short.MIN_VALUE)
        );
        packet = new TimingsPacket(builder.build());
        List<List<Short>> Orientations = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getOrientation)
                .collect(Collectors.toList());
        final List<List<Short>> expectedOrientations = IntStream
                .range(0, Orientations.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedOrientation())
                .collect(Collectors.toList());
        assertThat(Orientations, is(expectedOrientations));

        builder.getParticipantInfoBuilder().setExpectedOrientation(Arrays.asList(
                (short) (Short.MIN_VALUE - 1),
                (short) (Short.MIN_VALUE - 1),
                (short) (Short.MIN_VALUE - 1)
        ));
        packet = new TimingsPacket(builder.build());
        Orientations = packet
                .getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getOrientation)
                .collect(Collectors.toList());
        final List<List<Integer>> overflowPositions = IntStream
                .range(0, Orientations.size())
                .mapToObj(v -> Arrays.asList(
                        (Short.MIN_VALUE - 1),
                        (Short.MIN_VALUE - 1),
                        (Short.MIN_VALUE - 1)
                ))
                .collect(Collectors.toList());
        assertThat(Orientations, is(not(overflowPositions)));
    }

    @Test
    public void getCurrentLapDistance() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Integer> currentLapDistances = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLapDistance)
                .collect(Collectors.toList());
        final List<Integer> expectedCurrentLapDistances = IntStream
                .range(0, currentLapDistances.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedCurrentLapDistance())
                .collect(Collectors.toList());
        assertThat(currentLapDistances, is(expectedCurrentLapDistances));
    }

    @Test
    public void getCurrentLapDistance_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCurrentLapDistance(MAX_UNSIGNED_SHORT);
        packet = new TimingsPacket(builder.build());
        List<Integer> currentLapDistances = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLapDistance)
                .collect(Collectors.toList());
        List<Integer> expectedCurrentLapDistances = IntStream
                .range(0, currentLapDistances.size())
                .mapToObj(v -> MAX_UNSIGNED_SHORT)
                .collect(Collectors.toList());
        assertThat(currentLapDistances, is(expectedCurrentLapDistances));

        builder.getParticipantInfoBuilder().setExpectedCurrentLapDistance(MAX_UNSIGNED_SHORT + 1);
        packet = new TimingsPacket(builder.build());
        currentLapDistances = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLapDistance)
                .collect(Collectors.toList());
        expectedCurrentLapDistances = IntStream
                .range(0, currentLapDistances.size())
                .mapToObj(v -> MAX_UNSIGNED_SHORT + 1)
                .collect(Collectors.toList());
        assertThat(currentLapDistances, is(not(expectedCurrentLapDistances)));
    }

    @Test
    public void getCurrentLapDistance_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCurrentLapDistance(MIN_UNSIGNED_SHORT);
        packet = new TimingsPacket(builder.build());
        List<Integer> currentLapDistances = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLapDistance)
                .collect(Collectors.toList());
        List<Integer> expectedCurrentLapDistances = IntStream
                .range(0, currentLapDistances.size())
                .mapToObj(v -> MIN_UNSIGNED_SHORT)
                .collect(Collectors.toList());
        assertThat(currentLapDistances, is(expectedCurrentLapDistances));

        builder.getParticipantInfoBuilder().setExpectedCurrentLapDistance(MIN_UNSIGNED_SHORT - 1);
        packet = new TimingsPacket(builder.build());
        currentLapDistances = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLapDistance)
                .collect(Collectors.toList());
        expectedCurrentLapDistances = IntStream
                .range(0, currentLapDistances.size())
                .mapToObj(v -> MIN_UNSIGNED_SHORT - 1)
                .collect(Collectors.toList());
        assertThat(currentLapDistances, is(not(expectedCurrentLapDistances)));
    }

    @Test
    public void getIsActive_True() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) (1 << 7));
        packet = new TimingsPacket(builder.build());
        final List<Boolean> actives = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::isActive)
                .collect(Collectors.toList());
        final List<Boolean> expected = IntStream
                .range(0, actives.size())
                .mapToObj(v -> true)
                .collect(Collectors.toList());
        assertThat(actives, is(expected));
    }

    @Test
    public void getIsActive_False() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) 0);
        packet = new TimingsPacket(builder.build());
        final List<Boolean> actives = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::isActive)
                .collect(Collectors.toList());
        final List<Boolean> expected = IntStream
                .range(0, actives.size())
                .mapToObj(v -> false)
                .collect(Collectors.toList());
        assertThat(actives, is(expected));
    }

    @Test
    public void getRacePosition() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        final Random random = new Random();
        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) random.nextInt((1 << 7)));
        packet = new TimingsPacket(builder.build());
        final List<Integer> RacePositions = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRacePosition)
                .collect(Collectors.toList());
        final List<Integer> expectedRacePositions = IntStream
                .range(0, RacePositions.size())
                .mapToObj(v -> (int) builder.getParticipantInfoBuilder().getExpectedRacePosition())
                .collect(Collectors.toList());
        assertThat(RacePositions, is(expectedRacePositions));
    }

    @Test
    public void getRacePosition_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) ((1 << 7) - 1));
        packet = new TimingsPacket(builder.build());
        List<Integer> RacePositions = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRacePosition)
                .collect(Collectors.toList());
        List<Integer> expectedRacePositions = IntStream
                .range(0, RacePositions.size())
                .mapToObj(v -> (1 << 7) - 1)
                .collect(Collectors.toList());
        assertThat(RacePositions, is(expectedRacePositions));

        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) (1 << 7));
        packet = new TimingsPacket(builder.build());
        RacePositions = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRacePosition)
                .collect(Collectors.toList());
        expectedRacePositions = IntStream
                .range(0, RacePositions.size())
                .mapToObj(v -> (1 << 7))
                .collect(Collectors.toList());
        assertThat(RacePositions, is(not(expectedRacePositions)));
    }

    @Test
    public void getRacePosition_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) 0);
        packet = new TimingsPacket(builder.build());
        List<Integer> RacePositions = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRacePosition)
                .collect(Collectors.toList());
        List<Integer> expectedRacePositions = IntStream
                .range(0, RacePositions.size())
                .mapToObj(v -> 0)
                .collect(Collectors.toList());
        assertThat(RacePositions, is(expectedRacePositions));

        builder.getParticipantInfoBuilder().setExpectedRacePosition((short) -1);
        packet = new TimingsPacket(builder.build());
        RacePositions = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRacePosition)
                .collect(Collectors.toList());
        expectedRacePositions = IntStream
                .range(0, RacePositions.size())
                .mapToObj(v -> -1)
                .collect(Collectors.toList());
        assertThat(RacePositions, is(not(expectedRacePositions)));
    }

    @Test
    public void getSector() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Short> Sectors = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getSector)
                .collect(Collectors.toList());
        final List<Short> expectedSectors = IntStream
                .range(0, Sectors.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedSector())
                .collect(Collectors.toList());
        assertThat(Sectors, is(expectedSectors));
    }

    @Test
    public void getSector_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedSector(MAX_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> Sectors = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getSector)
                .collect(Collectors.toList());
        List<Short> expectedSectors = IntStream
                .range(0, Sectors.size())
                .mapToObj(v -> MAX_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(Sectors, is(expectedSectors));

        builder.getParticipantInfoBuilder().setExpectedSector((short) (MAX_UNSIGNED_BYTE + 1));
        packet = new TimingsPacket(builder.build());
        Sectors = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getSector)
                .collect(Collectors.toList());
        expectedSectors = IntStream
                .range(0, Sectors.size())
                .mapToObj(v -> (short) (MAX_UNSIGNED_BYTE + 1))
                .collect(Collectors.toList());
        assertThat(Sectors, is(not(expectedSectors)));
    }

    @Test
    public void getSector_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedSector(MIN_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> Sectors = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getSector)
                .collect(Collectors.toList());
        List<Short> expectedSectors = IntStream
                .range(0, Sectors.size())
                .mapToObj(v -> MIN_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(Sectors, is(expectedSectors));

        builder.getParticipantInfoBuilder().setExpectedSector((short) (MIN_UNSIGNED_BYTE - 1));
        packet = new TimingsPacket(builder.build());
        Sectors = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getSector)
                .collect(Collectors.toList());
        expectedSectors = IntStream
                .range(0, Sectors.size())
                .mapToObj(v -> (short) (MIN_UNSIGNED_BYTE - 1))
                .collect(Collectors.toList());
        assertThat(Sectors, is(not(expectedSectors)));
    }

    @Test
    public void getHighestFlag() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Short> HighestFlags = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getHighestFlag)
                .collect(Collectors.toList());
        final List<Short> expectedHighestFlags = IntStream
                .range(0, HighestFlags.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedHighestFlag())
                .collect(Collectors.toList());
        assertThat(HighestFlags, is(expectedHighestFlags));
    }

    @Test
    public void getHighestFlag_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedHighestFlag(MAX_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> HighestFlags = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getHighestFlag)
                .collect(Collectors.toList());
        List<Short> expectedHighestFlags = IntStream
                .range(0, HighestFlags.size())
                .mapToObj(v -> MAX_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(HighestFlags, is(expectedHighestFlags));

        builder.getParticipantInfoBuilder().setExpectedHighestFlag((short) (MAX_UNSIGNED_BYTE + 1));
        packet = new TimingsPacket(builder.build());
        HighestFlags = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getHighestFlag)
                .collect(Collectors.toList());
        expectedHighestFlags = IntStream
                .range(0, HighestFlags.size())
                .mapToObj(v -> (short) (MAX_UNSIGNED_BYTE + 1))
                .collect(Collectors.toList());
        assertThat(HighestFlags, is(not(expectedHighestFlags)));
    }

    @Test
    public void getHighestFlag_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedHighestFlag(MIN_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> HighestFlags = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getHighestFlag)
                .collect(Collectors.toList());
        List<Short> expectedHighestFlags = IntStream
                .range(0, HighestFlags.size())
                .mapToObj(v -> MIN_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(HighestFlags, is(expectedHighestFlags));

        builder.getParticipantInfoBuilder().setExpectedHighestFlag((short) (MIN_UNSIGNED_BYTE - 1));
        packet = new TimingsPacket(builder.build());
        HighestFlags = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getHighestFlag)
                .collect(Collectors.toList());
        expectedHighestFlags = IntStream
                .range(0, HighestFlags.size())
                .mapToObj(v -> (short) (MIN_UNSIGNED_BYTE - 1))
                .collect(Collectors.toList());
        assertThat(HighestFlags, is(not(expectedHighestFlags)));
    }

    @Test
    public void getPitModeSchedule() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Short> PitModeSchedules = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getPitModeSchedule)
                .collect(Collectors.toList());
        final List<Short> expectedPitModeSchedules = IntStream
                .range(0, PitModeSchedules.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedPitModeSchedule())
                .collect(Collectors.toList());
        assertThat(PitModeSchedules, is(expectedPitModeSchedules));
    }

    @Test
    public void getPitModeSchedule_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedPitModeSchedule(MAX_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> PitModeSchedules = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getPitModeSchedule)
                .collect(Collectors.toList());
        List<Short> expectedPitModeSchedules = IntStream
                .range(0, PitModeSchedules.size())
                .mapToObj(v -> MAX_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(PitModeSchedules, is(expectedPitModeSchedules));

        builder.getParticipantInfoBuilder().setExpectedPitModeSchedule((short) (MAX_UNSIGNED_BYTE + 1));
        packet = new TimingsPacket(builder.build());
        PitModeSchedules = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getPitModeSchedule)
                .collect(Collectors.toList());
        expectedPitModeSchedules = IntStream
                .range(0, PitModeSchedules.size())
                .mapToObj(v -> (short) (MAX_UNSIGNED_BYTE + 1))
                .collect(Collectors.toList());
        assertThat(PitModeSchedules, is(not(expectedPitModeSchedules)));
    }

    @Test
    public void getPitModeSchedule_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedPitModeSchedule(MIN_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> PitModeSchedules = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getPitModeSchedule)
                .collect(Collectors.toList());
        List<Short> expectedPitModeSchedules = IntStream
                .range(0, PitModeSchedules.size())
                .mapToObj(v -> MIN_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(PitModeSchedules, is(expectedPitModeSchedules));

        builder.getParticipantInfoBuilder().setExpectedPitModeSchedule((short) (MIN_UNSIGNED_BYTE - 1));
        packet = new TimingsPacket(builder.build());
        PitModeSchedules = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getPitModeSchedule)
                .collect(Collectors.toList());
        expectedPitModeSchedules = IntStream
                .range(0, PitModeSchedules.size())
                .mapToObj(v -> (short) (MIN_UNSIGNED_BYTE - 1))
                .collect(Collectors.toList());
        assertThat(PitModeSchedules, is(not(expectedPitModeSchedules)));
    }

    @Test
    public void getIsHuman_True() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCarIndex(1 << 15);
        packet = new TimingsPacket(builder.build());
        final List<Boolean> actives = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::isHuman)
                .collect(Collectors.toList());
        final List<Boolean> expected = IntStream
                .range(0, actives.size())
                .mapToObj(v -> true)
                .collect(Collectors.toList());
        assertThat(actives, is(expected));
    }

    @Test
    public void getIsHuman_False() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCarIndex(0);
        packet = new TimingsPacket(builder.build());
        final List<Boolean> actives = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::isHuman)
                .collect(Collectors.toList());
        final List<Boolean> expected = IntStream
                .range(0, actives.size())
                .mapToObj(v -> false)
                .collect(Collectors.toList());
        assertThat(actives, is(expected));
    }

    @Test
    public void getCarIndex() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        final Random random = new Random();
        builder.getParticipantInfoBuilder().setExpectedCarIndex(random.nextInt(1 << 15));
        packet = new TimingsPacket(builder.build());
        final List<Integer> CarIndexs = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCarIndex)
                .collect(Collectors.toList());
        final List<Integer> expectedCarIndexs = IntStream
                .range(0, CarIndexs.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedCarIndex())
                .collect(Collectors.toList());
        assertThat(CarIndexs, is(expectedCarIndexs));
    }

    @Test
    public void getCarIndex_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCarIndex((1 << 15) - 1);
        packet = new TimingsPacket(builder.build());
        List<Integer> CarIndexs = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCarIndex)
                .collect(Collectors.toList());
        List<Integer> expectedCarIndexs = IntStream
                .range(0, CarIndexs.size())
                .mapToObj(v -> (1 << 15) - 1)
                .collect(Collectors.toList());
        assertThat(CarIndexs, is(expectedCarIndexs));

        builder.getParticipantInfoBuilder().setExpectedCarIndex(1 << 15);
        packet = new TimingsPacket(builder.build());
        CarIndexs = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCarIndex)
                .collect(Collectors.toList());
        expectedCarIndexs = IntStream
                .range(0, CarIndexs.size())
                .mapToObj(v -> (1 << 15))
                .collect(Collectors.toList());
        assertThat(CarIndexs, is(not(expectedCarIndexs)));
    }

    @Test
    public void getCarIndex_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCarIndex(0);
        packet = new TimingsPacket(builder.build());
        List<Integer> CarIndexs = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCarIndex)
                .collect(Collectors.toList());
        List<Integer> expectedCarIndexs = IntStream
                .range(0, CarIndexs.size())
                .mapToObj(v -> 0)
                .collect(Collectors.toList());
        assertThat(CarIndexs, is(expectedCarIndexs));

        builder.getParticipantInfoBuilder().setExpectedCarIndex(-1);
        packet = new TimingsPacket(builder.build());
        CarIndexs = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCarIndex)
                .collect(Collectors.toList());
        expectedCarIndexs = IntStream
                .range(0, CarIndexs.size())
                .mapToObj(v -> -1)
                .collect(Collectors.toList());
        assertThat(CarIndexs, is(not(expectedCarIndexs)));
    }

    @Test
    public void getRaceState() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Short> RaceStates = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRaceState)
                .collect(Collectors.toList());
        final List<Short> expectedRaceStates = IntStream
                .range(0, RaceStates.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedRaceState())
                .collect(Collectors.toList());
        assertThat(RaceStates, is(expectedRaceStates));
    }

    @Test
    public void getRaceState_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRaceState(MAX_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> RaceStates = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRaceState)
                .collect(Collectors.toList());
        List<Short> expectedRaceStates = IntStream
                .range(0, RaceStates.size())
                .mapToObj(v -> MAX_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(RaceStates, is(expectedRaceStates));

        builder.getParticipantInfoBuilder().setExpectedRaceState((short) (MAX_UNSIGNED_BYTE + 1));
        packet = new TimingsPacket(builder.build());
        RaceStates = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRaceState)
                .collect(Collectors.toList());
        expectedRaceStates = IntStream
                .range(0, RaceStates.size())
                .mapToObj(v -> (short) (MAX_UNSIGNED_BYTE + 1))
                .collect(Collectors.toList());
        assertThat(RaceStates, is(not(expectedRaceStates)));
    }

    @Test
    public void getRaceState_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedRaceState(MIN_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> RaceStates = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRaceState)
                .collect(Collectors.toList());
        List<Short> expectedRaceStates = IntStream
                .range(0, RaceStates.size())
                .mapToObj(v -> MIN_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(RaceStates, is(expectedRaceStates));

        builder.getParticipantInfoBuilder().setExpectedRaceState((short) (MIN_UNSIGNED_BYTE - 1));
        packet = new TimingsPacket(builder.build());
        RaceStates = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getRaceState)
                .collect(Collectors.toList());
        expectedRaceStates = IntStream
                .range(0, RaceStates.size())
                .mapToObj(v -> (short) (MIN_UNSIGNED_BYTE - 1))
                .collect(Collectors.toList());
        assertThat(RaceStates, is(not(expectedRaceStates)));
    }
    
    @Test
    public void getCurrentLap() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Short> CurrentLaps = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList());
        final List<Short> expectedCurrentLaps = IntStream
                .range(0, CurrentLaps.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedCurrentLap())
                .collect(Collectors.toList());
        assertThat(CurrentLaps, is(expectedCurrentLaps));
    }

    @Test
    public void getCurrentLap_MaxValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCurrentLap(MAX_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> CurrentLaps = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList());
        List<Short> expectedCurrentLaps = IntStream
                .range(0, CurrentLaps.size())
                .mapToObj(v -> MAX_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(CurrentLaps, is(expectedCurrentLaps));

        builder.getParticipantInfoBuilder().setExpectedCurrentLap((short) (MAX_UNSIGNED_BYTE + 1));
        packet = new TimingsPacket(builder.build());
        CurrentLaps = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList());
        expectedCurrentLaps = IntStream
                .range(0, CurrentLaps.size())
                .mapToObj(v -> (short) (MAX_UNSIGNED_BYTE + 1))
                .collect(Collectors.toList());
        assertThat(CurrentLaps, is(not(expectedCurrentLaps)));
    }

    @Test
    public void getCurrentLap_MinValue() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        builder.getParticipantInfoBuilder().setExpectedCurrentLap(MIN_UNSIGNED_BYTE);
        packet = new TimingsPacket(builder.build());
        List<Short> CurrentLaps = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList());
        List<Short> expectedCurrentLaps = IntStream
                .range(0, CurrentLaps.size())
                .mapToObj(v -> MIN_UNSIGNED_BYTE)
                .collect(Collectors.toList());
        assertThat(CurrentLaps, is(expectedCurrentLaps));

        builder.getParticipantInfoBuilder().setExpectedCurrentLap((short) (MIN_UNSIGNED_BYTE - 1));
        packet = new TimingsPacket(builder.build());
        CurrentLaps = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList());
        expectedCurrentLaps = IntStream
                .range(0, CurrentLaps.size())
                .mapToObj(v -> (short) (MIN_UNSIGNED_BYTE - 1))
                .collect(Collectors.toList());
        assertThat(CurrentLaps, is(not(expectedCurrentLaps)));
    }
    
    @Test
    public void getCurrentTime() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Float> CurrentTimes = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentTime)
                .collect(Collectors.toList());
        final List<Float> expectedCurrentTimes = IntStream
                .range(0, CurrentTimes.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedCurrentTime())
                .collect(Collectors.toList());
        assertThat(CurrentTimes, is(expectedCurrentTimes));
    }

    @Test
    public void getCurrentSectorTime() throws Exception {
        final TimingsPacketBuilder builder = new TimingsPacketBuilder();
        packet = new TimingsPacket(builder.build());
        final List<Float> CurrentSectorTimes = packet.getParticipants()
                .stream()
                .map(TimingsPacket.ParticipantInfo::getCurrentSectorTime)
                .collect(Collectors.toList());
        final List<Float> expectedCurrentSectorTimes = IntStream
                .range(0, CurrentSectorTimes.size())
                .mapToObj(v -> builder.getParticipantInfoBuilder().getExpectedCurrentSectorTime())
                .collect(Collectors.toList());
        assertThat(CurrentSectorTimes, is(expectedCurrentSectorTimes));
    }
}

