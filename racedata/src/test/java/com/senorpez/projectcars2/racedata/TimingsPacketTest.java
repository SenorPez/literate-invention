package com.senorpez.projectcars2.racedata;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketBuilder.*;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Enclosed.class)
public class TimingsPacketTest {
    @RunWith(Parameterized.class)
    public static class RaceStateTests {
        private TimingsPacket packet;

        @Parameter
        public RaceState raceState;

        @Parameters(name = "Race State: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, RaceState.RACESTATE_MAX.ordinal())
                    .forEach(value -> output.add(new Object[]{RaceState.valueOf(value)}));
            return output;
        }

        @Test
        public void getRaceState() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedRaceState((short) raceState.ordinal());
            packet = new TimingsPacket(builder.build());
            final List<RaceState> RaceStates = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getRaceState)
                    .collect(Collectors.toList());
            final List<RaceState> expectedRaceStates = IntStream
                    .range(0, RaceStates.size())
                    .mapToObj(v -> {
                        final int raceStateValue = builder.getParticipantInfoBuilder().getExpectedRaceState() & 127;
                        return RaceState.valueOf(raceStateValue);
                    })
                    .collect(Collectors.toList());
            assertThat(RaceStates, is(expectedRaceStates));
        }
    }

    @RunWith(Parameterized.class)
    public static class FlagTests {
        private TimingsPacket packet;

        @Parameter
        public FlagColour flagColour;

        @Parameter(1)
        public FlagReason flagReason;

        @Parameters(name = "Color: {0}, Reason: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, FlagColour.FLAG_COLOUR_MAX.ordinal())
                    .forEach(colourVal -> IntStream
                            .range(0, FlagReason.FLAG_REASON_MAX.ordinal())
                            .forEach(reasonVal -> output.add(new Object[]{
                                    FlagColour.valueOf(colourVal),
                                    FlagReason.valueOf(reasonVal)
                            }))
                    );
            return output;
        }

        @Test
        public void getFlagColour() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder()
                    .setExpectedHighestFlag((short) (flagColour.ordinal() | (flagReason.ordinal() << 4)));
            packet = new TimingsPacket(builder.build());
            final List<FlagColour> flagColours = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getFlagColour)
                    .collect(Collectors.toList());
            final List<FlagColour> expectedFlagColours = IntStream
                    .range(0, flagColours.size())
                    .mapToObj(v -> FlagColour.valueOf((builder.getParticipantInfoBuilder().getExpectedHighestFlag() & 15)))
                    .collect(Collectors.toList());
            assertThat(flagColours, is(expectedFlagColours));
        }

        @Test
        public void getFlagReason() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder()
                    .setExpectedHighestFlag((short) (flagColour.ordinal() | (flagReason.ordinal() << 4)));
            packet = new TimingsPacket(builder.build());
            final List<FlagReason> flagReasons = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getFlagReason)
                    .collect(Collectors.toList());
            final List<FlagReason> expectedFlagReasons = IntStream
                    .range(0, flagReasons.size())
                    .mapToObj(v -> FlagReason.valueOf(((builder.getParticipantInfoBuilder().getExpectedHighestFlag() & 240) >>> 4)))
                    .collect(Collectors.toList());
            assertThat(flagReasons, is(expectedFlagReasons));
        }
    }

    @RunWith(Parameterized.class)
    public static class PitTests {
        private TimingsPacket packet;

        @Parameter
        public PitMode pitMode;

        @Parameter(1)
        public PitSchedule pitSchedule;

        @Parameters(name = "Mode: {0}, Schedule: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, PitMode.PIT_MODE_MAX.ordinal())
                    .forEach(modeVal -> IntStream
                            .range(0, PitSchedule.PIT_SCHEDULE_MAX.ordinal())
                            .forEach(scheduleVal -> output.add(new Object[]{
                                    PitMode.valueOf(modeVal),
                                    PitSchedule.valueOf(scheduleVal)
                            }))
                    );
            return output;
        }

        @Test
        public void getPitMode() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder()
                    .setExpectedPitModeSchedule((short) (pitMode.ordinal() | (pitSchedule.ordinal() << 4)));
            packet = new TimingsPacket(builder.build());
            final List<PitMode> pitModes = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getPitMode)
                    .collect(Collectors.toList());
            final List<PitMode> expectedPitModes = IntStream
                    .range(0, pitModes.size())
                    .mapToObj(v -> PitMode.valueOf((builder.getParticipantInfoBuilder().getExpectedPitModeSchedule()) & 15))
                    .collect(Collectors.toList());
            assertThat(pitModes, is(expectedPitModes));
        }

        @Test
        public void getPitSchedule() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder()
                    .setExpectedPitModeSchedule((short) (pitMode.ordinal() | (pitSchedule.ordinal() << 4)));
            packet = new TimingsPacket(builder.build());
            final List<PitSchedule> pitSchedules = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getPitSchedule)
                    .collect(Collectors.toList());
            final List<PitSchedule> expectedPitSchedules = IntStream
                    .range(0, pitSchedules.size())
                    .mapToObj(v -> PitSchedule.valueOf(((builder.getParticipantInfoBuilder().getExpectedPitModeSchedule()) & 240) >>> 4))
                    .collect(Collectors.toList());
            assertThat(pitSchedules, is(expectedPitSchedules));
        }
    }

    public static class SingleTests {
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
            final List<List<Float>> worldPositions = packet
                    .getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                    .collect(Collectors.toList());
            final List<List<Float>> expectedWorldPositions = IntStream
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
            builder.getParticipantInfoBuilder().setExpectedSector((short) 0);
            packet = new TimingsPacket(builder.build());
            List<List<Float>> worldPositions = packet
                    .getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                    .collect(Collectors.toList());
            final List<List<Float>> expectedWorldPositions = IntStream
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
            final List<List<Float>> overflowPositions = IntStream
                    .range(0, worldPositions.size())
                    .mapToObj(v -> Arrays.asList(
                            (float) (Short.MAX_VALUE + 1),
                            (float) (Short.MAX_VALUE + 1),
                            (float) (Short.MAX_VALUE + 1)
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
            builder.getParticipantInfoBuilder().setExpectedSector((short) 0);
            packet = new TimingsPacket(builder.build());
            List<List<Float>> worldPositions = packet
                    .getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getWorldPosition)
                    .collect(Collectors.toList());
            final List<List<Float>> expectedWorldPositions = IntStream
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
            final List<List<Float>> overflowPositions = IntStream
                    .range(0, worldPositions.size())
                    .mapToObj(v -> Arrays.asList(
                            (float) (Short.MIN_VALUE - 1),
                            (float) (Short.MIN_VALUE - 1),
                            (float) (Short.MIN_VALUE - 1)
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
            final List<Integer> Sectors = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getSector)
                    .collect(Collectors.toList());
            final List<Integer> expectedSectors = IntStream
                    .range(0, Sectors.size())
                    .mapToObj(v -> (7 & builder.getParticipantInfoBuilder().getExpectedSector()))
                    .collect(Collectors.toList());
            assertThat(Sectors, is(expectedSectors));
        }

        @Test
        public void getSector_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedSector((short) 7);
            packet = new TimingsPacket(builder.build());
            List<Integer> Sectors = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getSector)
                    .collect(Collectors.toList());
            List<Integer> expectedSectors = IntStream
                    .range(0, Sectors.size())
                    .mapToObj(v -> (7))
                    .collect(Collectors.toList());
            assertThat(Sectors, is(expectedSectors));

            builder.getParticipantInfoBuilder().setExpectedSector((short) 8);
            packet = new TimingsPacket(builder.build());
            Sectors = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getSector)
                    .collect(Collectors.toList());
            expectedSectors = IntStream
                    .range(0, Sectors.size())
                    .mapToObj(v -> 8)
                    .collect(Collectors.toList());
            assertThat(Sectors, is(not(expectedSectors)));
        }

        @Test
        public void getSector_MinValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedSector((short) 0);
            packet = new TimingsPacket(builder.build());
            List<Integer> Sectors = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getSector)
                    .collect(Collectors.toList());
            List<Integer> expectedSectors = IntStream
                    .range(0, Sectors.size())
                    .mapToObj(v -> 0)
                    .collect(Collectors.toList());
            assertThat(Sectors, is(expectedSectors));

            builder.getParticipantInfoBuilder().setExpectedSector((short) -1);
            packet = new TimingsPacket(builder.build());
            Sectors = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::getSector)
                    .collect(Collectors.toList());
            expectedSectors = IntStream
                    .range(0, Sectors.size())
                    .mapToObj(v -> -1)
                    .collect(Collectors.toList());
            assertThat(Sectors, is(not(expectedSectors)));
        }

        @Test(expected = InvalidFlagColourException.class)
        public void getFlagColor_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedHighestFlag((short) FlagColour.FLAG_COLOUR_MAX.ordinal());
            packet = new TimingsPacket(builder.build());
        }

        @Test(expected = InvalidFlagReasonException.class)
        public void getFlagReason_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedHighestFlag((short) (FlagReason.FLAG_REASON_MAX.ordinal() << 4));
            packet = new TimingsPacket(builder.build());
        }

        @Test(expected = InvalidPitModeException.class)
        public void getPitMode_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedPitModeSchedule((short) PitMode.PIT_MODE_MAX.ordinal());
            packet = new TimingsPacket(builder.build());
        }

        @Test(expected = InvalidPitScheduleException.class)
        public void getPitSchedule_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedPitModeSchedule((short) (PitSchedule.PIT_SCHEDULE_MAX.ordinal() << 4));
            packet = new TimingsPacket(builder.build());
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
        public void getIsInvalidated_True() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedRaceState((short) (1 << 3));
            packet = new TimingsPacket(builder.build());
            final List<Boolean> values = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::isLapInvalidated)
                    .collect(Collectors.toList());
            final List<Boolean> expected = IntStream
                    .range(0, values.size())
                    .mapToObj(v -> true)
                    .collect(Collectors.toList());
            assertThat(values, is(expected));
        }

        @Test
        public void getIsInvalidated_False() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedRaceState((short) 0);
            packet = new TimingsPacket(builder.build());
            final List<Boolean> values = packet.getParticipants()
                    .stream()
                    .map(TimingsPacket.ParticipantInfo::isLapInvalidated)
                    .collect(Collectors.toList());
            final List<Boolean> expected = IntStream
                    .range(0, values.size())
                    .mapToObj(v -> false)
                    .collect(Collectors.toList());
            assertThat(values, is(expected));
        }

        @Test(expected = InvalidRaceStateException.class)
        public void getRaceState_MaxValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedRaceState((short) RaceState.RACESTATE_MAX.ordinal());
            packet = new TimingsPacket(builder.build());
        }

        @Test(expected = InvalidRaceStateException.class)
        public void getRaceState_MinValue() throws Exception {
            final TimingsPacketBuilder builder = new TimingsPacketBuilder();
            builder.getParticipantInfoBuilder().setExpectedRaceState((short) -1);
            packet = new TimingsPacket(builder.build());
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
}

