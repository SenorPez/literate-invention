package com.senorpez.projectcars.racedata;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.racedata.PacketType.TELEMETRY_DATA;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@RunWith(Enclosed.class)
public class TelemetryDataPacketTest {
    @RunWith(Parameterized.class)
    public static class GameSessionStateTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public GameState gameState;

        @Parameter(value = 1)
        public SessionState sessionState;

        @Parameters(name = "Game State: {0}, Session State: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            EnumSet.allOf(GameState.class)
                    .forEach(gameState -> EnumSet.allOf(SessionState.class)
                            .forEach(sessionState -> output.add(new Object[]{gameState, sessionState})));
            return output;
        }

        @Test
        public void GetGameState() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedGameState(gameState)
                    .setExpectedSessionState(sessionState);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getGameState(), is(builder.getExpectedGameState()));
        }

        @Test
        public void GetSessionState() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedGameState(gameState)
                    .setExpectedSessionState(sessionState);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSessionState(), is(builder.getExpectedSessionState()));
        }
    }

    @RunWith(Parameterized.class)
    public static class RaceStateFlagsTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public RaceState raceState;

        @Parameter(value = 1)
        public Boolean isLapInvalidated;

        @Parameter(value = 2)
        public Boolean isAntiLockActive;

        @Parameter(value = 3)
        public Boolean isBoostActive;

        @Parameters(name = "Race State: {0}, LapInval: {1}, AntiLock: {2}, Boost: {3}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            EnumSet.allOf(RaceState.class)
                    .forEach(raceState -> {
                        output.add(new Object[]{raceState, true, true, true});
                        output.add(new Object[]{raceState, true, true, false});
                        output.add(new Object[]{raceState, true, false, true});
                        output.add(new Object[]{raceState, true, false, false});
                        output.add(new Object[]{raceState, false, true, true});
                        output.add(new Object[]{raceState, false, true, false});
                        output.add(new Object[]{raceState, false, false, true});
                        output.add(new Object[]{raceState, false, false, false});
                    });
            return output;
        }

        @Test
        public void GetRaceState() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedRaceState(raceState)
                    .setExpectedIsLapInvalidated(isLapInvalidated)
                    .setExpectedIsAntiLockActive(isAntiLockActive)
                    .setExpectedIsBoostActive(isBoostActive);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getRaceState(), is(builder.getExpectedRaceState()));
        }

        @Test
        public void IsLapInvalidated() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedRaceState(raceState)
                    .setExpectedIsLapInvalidated(isLapInvalidated)
                    .setExpectedIsAntiLockActive(isAntiLockActive)
                    .setExpectedIsBoostActive(isBoostActive);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.isLapInvalidated(), is(builder.getExpectedIsLapInvalidated()));
        }
        @Test
        public void IsAntiLockActive() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedRaceState(raceState)
                    .setExpectedIsLapInvalidated(isLapInvalidated)
                    .setExpectedIsAntiLockActive(isAntiLockActive)
                    .setExpectedIsBoostActive(isBoostActive);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.isAntiLockActive(), is(builder.getExpectedIsAntiLockActive()));
        }
        @Test
        public void IsBoostActive() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedRaceState(raceState)
                    .setExpectedIsLapInvalidated(isLapInvalidated)
                    .setExpectedIsAntiLockActive(isAntiLockActive)
                    .setExpectedIsBoostActive(isBoostActive);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.isBoostActive(), is(builder.getExpectedIsBoostActive()));
        }
    }

    @RunWith(Parameterized.class)
    public static class HighFlagTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public FlagColour flagColour;

        @Parameter(value = 1)
        public FlagReason flagReason;

        @Parameters(name = "Flag Colour: {0}, Flag Reason: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            EnumSet.allOf(FlagColour.class)
                    .forEach(flagColour -> EnumSet.allOf(FlagReason.class)
                            .forEach(flagReason -> output.add(new Object[]{flagColour, flagReason})));
            return output;
        }

        @Test
        public void GetHighestFlagColor() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedFlagColour(flagColour)
                    .setExpectedFlagReason(flagReason);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getHighestFlagColor(), is(builder.getExpectedFlagColour()));
        }

        @Test
        public void GetHighestFlagReason() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedFlagColour(flagColour)
                    .setExpectedFlagReason(flagReason);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getHighestFlagReason(), is(builder.getExpectedFlagReason()));
        }
    }

    @RunWith(Parameterized.class)
    public static class PitModeScheduleTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public PitMode pitMode;

        @Parameter(value = 1)
        public PitSchedule pitSchedule;

        @Parameters(name = "Pit Mode: {0}, Pit Schedule: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            EnumSet.allOf(PitMode.class)
                    .forEach(pitMode -> EnumSet.allOf(PitSchedule.class)
                            .forEach(pitSchedule -> output.add(new Object[]{pitMode, pitSchedule})));
            return output;
        }

        @Test
        public void GetPitMode() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedPitMode(pitMode)
                    .setExpectedPitSchedule(pitSchedule);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPitMode(), is(builder.getExpectedPitMode()));
        }

        @Test
        public void GetPitSchedule() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedPitMode(pitMode)
                    .setExpectedPitSchedule(pitSchedule);
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPitSchedule(), is(builder.getExpectedPitSchedule()));
        }
    }

    @RunWith(Parameterized.class)
    public static class CarFlagsTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public Short value;

        @Parameters(name = "Value: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 256).forEach(value1 -> output.add(new Object[]{(short) value1}));
            return output;
        }

        @Test
        public void IsHeadlight() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 1; /* 0000 0001 */
            assertThat(packet.isHeadlight(), is((value & mask) == 1));
        }

        @Test
        public void IsEngineActive() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 2; /* 0000 0010 */
            assertThat(packet.isEngineActive(), is((value & mask) == mask));
        }

        @Test
        public void isEngineWarning() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 4; /* 0000 0100 */
            assertThat(packet.isEngineWarning(), is((value & mask) == mask));
        }

        @Test
        public void isSpeedLimiter() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 8; /* 0000 1000 */
            assertThat(packet.isSpeedLimiter(), is((value & mask) == mask));
        }

        @Test
        public void isAbs() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 16; /* 0001 0000 */
            assertThat(packet.isAbs(), is((value & mask) == mask));
        }

        @Test
        public void isHandbrake() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 32; /* 0010 0000 */
            assertThat(packet.isHandbrake(), is((value & mask) == mask));
        }

        @Test
        public void isStability() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 64; /* 0100 0000 */
            assertThat(packet.isStability(), is((value & mask) == mask));
        }

        @Test
        public void isTractionControl() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 128; /* 1000 0000 */
            assertThat(packet.isTractionControl(), is((value & mask) == mask));
        }
    }

    @RunWith(Parameterized.class)
    public static class TyreFlagsTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public List<Short> value;

        @Parameters(name = "Value: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 8).forEach(value1 -> output.add(new Object[]{Arrays.asList((short) value1, (short) value1, (short) value1, (short) value1)}));
            return output;
        }

        @Test
        public void IsTyreAttached() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedTyreFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 1; /* 0000 0001 */
            assertThat(packet.isTyreAttached(), contains(value.stream()
                    .map(item -> (item & mask) == mask)
                    .toArray()));
        }

        @Test
        public void IsTyreInflated() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedTyreFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 2; /* 0000 0010 */
            assertThat(packet.isTyreInflated(), contains(value.stream()
                    .map(item -> (item & mask) == mask)
                    .toArray()));
        }

        @Test
        public void IsTyreIsOnGround() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedTyreFlags(value);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 4; /* 0000 0100 */
            assertThat(packet.isTyreIsOnGround(), contains(value.stream()
                    .map(item -> (item & mask) == mask)
                    .toArray()));
        }
    }

    @RunWith(Parameterized.class)
    public static class CrashDamageStateTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public CrashDamageState crashDamageState;

        @Parameter(value = 1)
        public Integer dPad;

        @Parameters(name = "Crash Damage State: {0}, DPad High: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            EnumSet.allOf(CrashDamageState.class)
                    .forEach(crashDamageState ->
                            IntStream.range(0, 16).forEach(value -> output.add(new Object[]{crashDamageState, value})));
            return output;
        }

        @Test
        public void GetCrashDamageState() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCrashState((short) ((dPad << 4) | crashDamageState.ordinal()));
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getCrashState(), is(crashDamageState));
        }

        @Test
        public void IsDPadButton5() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCrashState((short) ((dPad << 4) | crashDamageState.ordinal()));
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 16; /* 0001 0000 */
            assertThat(packet.isDPadButton5(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton6() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCrashState((short) ((dPad << 4) | crashDamageState.ordinal()));
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 32; /* 0010 0000 */
            assertThat(packet.isDPadButton6(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton7() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCrashState((short) ((dPad << 4) | crashDamageState.ordinal()));
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 64; /* 0010 0000 */
            assertThat(packet.isDPadButton7(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton8() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedCrashState((short) ((dPad << 4) | crashDamageState.ordinal()));
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 128; /* 0010 0000 */
            assertThat(packet.isDPadButton8(), is((dPad & mask) == mask));
        }
    }

    @RunWith(Parameterized.class)
    public static class DPadLowTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public Short dPad;

        @Parameters(name = "DPad Low: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 16).forEach(value -> output.add(new Object[]{(short) value}));
            return output;
        }

        @Test
        public void IsDPadButton1() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedDPad(dPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 1; /* 0000 0001 */
            assertThat(packet.isDPadButton1(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton2() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedDPad(dPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 2; /* 0000 0010 */
            assertThat(packet.isDPadButton2(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton3() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedDPad(dPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 4; /* 0000 0100 */
            assertThat(packet.isDPadButton3(), is((dPad & mask) == mask));
        }

        @Test
        public void IsDPadButton4() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedDPad(dPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 8; /* 0000 1000 */
            assertThat(packet.isDPadButton4(), is((dPad & mask) == mask));
        }
    }

    @RunWith(Parameterized.class)
    public static class JoypadTests {
        private TelemetryDataPacket packet;

        @Parameter()
        public Integer joyPad;

        @Parameters(name = "Joypad: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 65536).forEach(value -> output.add(new Object[]{value}));
            Collections.shuffle(output);
            return output.subList(0, 100);
        }

        @Test
        public void IsJoyPadButton1() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 1; /* 0000 0000 0000 0001*/
            assertThat(packet.isJoyPadButton1(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton2() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 2; /* 0000 0000 0000 0010*/
            assertThat(packet.isJoyPadButton2(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton3() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 4; /* 0000 0000 0000 0100*/
            assertThat(packet.isJoyPadButton3(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton4() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 8; /* 0000 0000 0000 1000*/
            assertThat(packet.isJoyPadButton4(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton5() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 16; /* 0000 0000 0001 0000*/
            assertThat(packet.isJoyPadButton5(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton6() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 32; /* 0000 0000 0010 0000*/
            assertThat(packet.isJoyPadButton6(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton7() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 64; /* 0000 0000 0100 0000*/
            assertThat(packet.isJoyPadButton7(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton8() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 128; /* 0000 0000 1000 0000*/
            assertThat(packet.isJoyPadButton8(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton9() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 256; /* 0000 0001 0000 0000*/
            assertThat(packet.isJoyPadButton9(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton10() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 512; /* 0000 0010 0000 0000*/
            assertThat(packet.isJoyPadButton10(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton11() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 1024; /* 0000 0100 0000 0000*/
            assertThat(packet.isJoyPadButton11(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton12() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 2048; /* 0000 1000 0000 0000*/
            assertThat(packet.isJoyPadButton12(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton13() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 4096; /* 0001 0000 0000 0000*/
            assertThat(packet.isJoyPadButton13(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton14() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 8192; /* 0010 0000 0000 0000*/
            assertThat(packet.isJoyPadButton14(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton15() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 16384; /* 0100 0000 0000 0000*/
            assertThat(packet.isJoyPadButton15(), is((joyPad & mask) == mask));
        }

        @Test
        public void IsJoyPadButton16() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedJoyPad(joyPad);
            packet = new TelemetryDataPacket(builder.build());
            final int mask = 32768; /* 1000 0000 0000 0000*/
            assertThat(packet.isJoyPadButton16(), is((joyPad & mask) == mask));
        }
    }

    public static class SingleTests {
        private TelemetryDataPacket packet;

        @Test
        public void GetPacketType() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPacketType(), is(TELEMETRY_DATA));
        }

        @Test(expected = InvalidPacketException.class)
        public void GetPacketType_WrongType() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder()
                    .setExpectedPacketType((short) 1);
            packet = new TelemetryDataPacket(builder.build());
        }

        @Test
        public void GetViewedParticipantIndex() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(builder.getExpectedViewedParticipantIndex()));
        }

        @Test
        public void GetNumParticipants() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getNumParticipants(), is(builder.getExpectedNumParticipants()));
        }

        @Test
        public void GetUnfilteredThrottle() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(builder.getExpectedUnfilteredThrottle() / 255.0f));
        }

        @Test
        public void GetUnfilteredBrake() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(builder.getExpectedUnfilteredBrake() / 255.0f));
        }

        @Test
        public void GetUnfilteredSteering() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(builder.getExpectedUnfilteredSteering() / 127.0f));
        }

        @Test
        public void GetUnfilteredClutch() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(builder.getExpectedUnfilteredClutch() / 255.0f));
        }

        @Test
        public void GetTrackLength() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTrackLength(), is(builder.getExpectedTrackLength()));
        }

        @Test
        public void GetLapsInEvent() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getLapsInEvent(), is(builder.getExpectedLapsInEvent()));
        }

        @Test
        public void GetBestLapTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getBestLapTime(), is(builder.getExpectedBestLapTime()));
        }

        @Test
        public void GetLastLapTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getLastLapTime(), is(builder.getExpectedLastLapTime()));
        }

        @Test
        public void GetCurrentTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getCurrentTime(), is(builder.getExpectedCurrentTime()));
        }

        @Test
        public void GetSplitTimeAhead() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSplitTimeAhead(), is(builder.getExpectedSplitTimeAhead()));
        }

        @Test
        public void GetSplitTimeBehind() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSplitTimeBehind(), is(builder.getExpectedSplitTimeBehind()));
        }

        @Test
        public void GetSplitTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSplitTime(), is(builder.getExpectedSplitTime()));
        }

        @Test
        public void GetEventTimeRemaining() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getEventTimeRemaining(), is(builder.getExpectedEventTimeRemaining()));
        }

        @Test
        public void GetPersonalFastestLapTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPersonalFastestLapTime(), is(builder.getExpectedPersonalFastestLapTime()));
        }

        @Test
        public void GetWorldFastestLapTime() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldFastestLapTime(), is(builder.getExpectedWorldFastestLapTime()));
        }

        @Test
        public void GetCurrentSector1Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getCurrentSector1Time(), is(builder.getExpectedCurrentSector1Time()));
        }

        @Test
        public void GetCurrentSector2Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getCurrentSector2Time(), is(builder.getExpectedCurrentSector2Time()));
        }

        @Test
        public void GetCurrentSector3Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getCurrentSector3Time(), is(builder.getExpectedCurrentSector3Time()));
        }

        @Test
        public void GetFastestSector1Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFastestSector1Time(), is(builder.getExpectedFastestSector1Time()));
        }

        @Test
        public void GetFastestSector2Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFastestSector2Time(), is(builder.getExpectedFastestSector2Time()));
        }

        @Test
        public void GetFastestSector3Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFastestSector3Time(), is(builder.getExpectedFastestSector3Time()));
        }

        @Test
        public void GetPersonalFastestSector1Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPersonalFastestSector1Time(), is(builder.getExpectedPersonalFastestSector1Time()));
        }

        @Test
        public void GetPersonalFastestSector2Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPersonalFastestSector2Time(), is(builder.getExpectedPersonalFastestSector2Time()));
        }

        @Test
        public void GetPersonalFastestSector3Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getPersonalFastestSector3Time(), is(builder.getExpectedPersonalFastestSector3Time()));
        }

        @Test
        public void GetWorldFastestSector1Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldFastestSector1Time(), is(builder.getExpectedWorldFastestSector1Time()));
        }

        @Test
        public void GetWorldFastestSector2Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldFastestSector2Time(), is(builder.getExpectedWorldFastestSector2Time()));
        }

        @Test
        public void GetWorldFastestSector3Time() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldFastestSector3Time(), is(builder.getExpectedWorldFastestSector3Time()));
        }

        @Test
        public void GetOilTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getOilTemp(), is(builder.getExpectedOilTemp()));
        }

        @Test
        public void GetOilPressure() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getOilPressure(), is(builder.getExpectedOilPressure()));
        }

        @Test
        public void GetWaterTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(builder.getExpectedWaterTemp()));
        }

        @Test
        public void GetWaterPressure() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(builder.getExpectedWaterPressure()));
        }

        @Test
        public void GetFuelPressure() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(builder.getExpectedFuelPressure()));
        }

        @Test
        public void GetFuelCapacity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(builder.getExpectedFuelCapacity()));
        }

        @Test
        public void GetBrake() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getBrake(), is(builder.getExpectedBrake() / 255.0f));
        }

        @Test
        public void GetFuelLevel() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getFuelLevel(), is(builder.getExpectedFuelLevel()));
        }

        @Test
        public void GetSpeed() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSpeed(), is(builder.getExpectedSpeed()));
        }

        @Test
        public void GetRpm() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getRpm(), is(builder.getExpectedRpm()));
        }

        @Test
        public void GetMaxRpm() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(builder.getExpectedMaxRpm()));
        }

        @Test
        public void GetThrottle() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getThrottle(), is(builder.getExpectedThrottle() / 255.0f));
        }

        @Test
        public void GetClutch() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getClutch(), is(builder.getExpectedClutch() / 255.0f));
        }

        @Test
        public void GetSteering() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSteering(), is(builder.getExpectedSteering() / 127.0f));
        }

        @Test
        public void GetGear() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getGear(), is(builder.getExpectedGear()));
        }

        @Test
        public void GetNumGears() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getNumGears(), is(builder.getExpectedNumGears()));
        }

        @Test
        public void GetOdometer() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getOdometer(), is(builder.getExpectedOdometer()));
        }

        @Test
        public void GetBoostAmount() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(builder.getExpectedBoostAmount()));
        }

        @Test
        public void GetOrientation() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getOrientation(), contains(builder.getExpectedOrientation().toArray()));
        }

        @Test
        public void GetLocalVelocity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getLocalVelocity(), contains(builder.getExpectedLocalVelocity().toArray()));
        }

        @Test
        public void GetWorldVelocity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldVelocity(), contains(builder.getExpectedWorldVelocity().toArray()));
        }

        @Test
        public void GetAngularVelocity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getAngularVelocity(), contains(builder.getExpectedAngularVelocity().toArray()));
        }

        @Test
        public void GetLocalAcceleration() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getLocalAcceleration(), contains(builder.getExpectedLocalAcceleration().toArray()));
        }

        @Test
        public void GetWorldAcceleration() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWorldAcceleration(), contains(builder.getExpectedWorldAcceleration().toArray()));
        }

        @Test
        public void GetExtentsCentre() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getExtentsCentre(), contains(builder.getExpectedExtentsCentre().toArray()));
        }

        @Test
        public void GetTerrain() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTerrain(), contains(builder.getExpectedTerrain().stream().map(TerrainMaterial::valueOf).toArray()));
        }

        @Test
        public void GetTyreY() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreY(), contains(builder.getExpectedTyreY().toArray()));
        }

        @Test
        public void GetTyreRps() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreRps(), contains(builder.getExpectedTyreRps().toArray()));
        }

        @Test
        public void GetTyreSlipSpeed() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreSlipSpeed(), contains(builder.getExpectedTyreSlipSpeed().toArray()));
        }

        @Test
        public void GetTyreTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreTemp(), contains(builder.getExpectedTyreTemp().toArray()));
        }

        @Test
        public void GetTyreGrip() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreGrip(), contains(builder.getExpectedTyreGrip().stream().map(value -> value / 255.0f).toArray()));
        }

        @Test
        public void GetTyreHeightAboveGround() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreHeightAboveGround(), contains(builder.getExpectedTyreHeightAboveGround().toArray()));
        }

        @Test
        public void GetTyreLateralStiffness() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreLateralStiffness(), contains(builder.getExpectedTyreLateralStiffness().toArray()));
        }

        @Test
        public void GetTyreWear() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreWear(), contains(builder.getExpectedTyreWear().stream().map(value -> value / 255.0f).toArray()));
        }

        @Test
        public void GetBrakeDamage() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getBrakeDamage(), contains(builder.getExpectedBrakeDamage().stream().map(value -> value / 255.0f).toArray()));
        }

        @Test
        public void GetSuspensionDamage() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), contains(builder.getExpectedSuspensionDamage().stream().map(value -> value / 255.0f).toArray()));
        }

        @Test
        public void GetBrakeTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getBrakeTemp(), contains(builder.getExpectedBrakeTemp().toArray()));
        }

        @Test
        public void GetTyreTreadTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), contains(builder.getExpectedTyreTreadTemp().toArray()));
        }

        @Test
        public void GetTyreLayerTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), contains(builder.getExpectedTyreLayerTemp().toArray()));
        }

        @Test
        public void GetTyreCarcassTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), contains(builder.getExpectedTyreCarcassTemp().toArray()));
        }

        @Test
        public void GetTyreRimTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), contains(builder.getExpectedTyreRimTemp().toArray()));
        }

        @Test
        public void GetTyreInternalAirTemp() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), contains(builder.getExpectedTyreInternalAirTemp().toArray()));
        }

        @Test
        public void GetWheelLocalPositionY() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWheelLocalPositionY(), contains(builder.getExpectedWheelLocalPositionY().toArray()));
        }

        @Test
        public void GetRideHeight() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getRideHeight(), contains(builder.getExpectedRideHeight().toArray()));
        }

        @Test
        public void GetSuspensionTravel() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSuspensionTravel(), contains(builder.getExpectedSuspensionTravel().toArray()));
        }

        @Test
        public void GetSuspensionVelocity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getSuspensionVelocity(), contains(builder.getExpectedSuspensionVelocity().toArray()));
        }

        @Test
        public void GetAirPressure() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getAirPressure(), contains(builder.getExpectedAirPressure().toArray()));
        }

        @Test
        public void GetEngineSpeed() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getEngineSpeed(), is(builder.getExpectedEngineSpeed()));
        }

        @Test
        public void GetEngineTorque() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getEngineTorque(), is(builder.getExpectedEngineTorque()));
        }

        @Test
        public void GetWings() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWings(), contains(builder.getExpectedWings().toArray()));
        }

        @Test
        public void GetEnforcedPitStopLap() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getEnforcedPitStopLap(), is(builder.getExpectedEnforcedPitStopLap()));
        }

        @Test
        public void GetAeroDamage() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(builder.getExpectedAeroDamage() / 255.0f));
        }

        @Test
        public void GetEngineDamage() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(builder.getExpectedEngineDamage() / 255.0f));
        }

        @Test
        public void GetAmbientTemperature() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(builder.getExpectedAmbientTemperature()));
        }


        @Test
        public void GetTrackTemperature() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(builder.getExpectedTrackTemperature()));
        }

        @Test
        public void GetRainDensity() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getRainDensity(), is(builder.getExpectedRainDensity()));
        }

        @Test
        public void GetWindSpeed() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWindSpeed(), is(builder.getExpectedWindSpeed()));
        }

        @Test
        public void GetWindDirectionX() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(builder.getExpectedWindDirectionX()));
        }

        @Test
        public void GetWindDirectionY() throws Exception {
            final TelemetryDataPacketBuilder builder = new TelemetryDataPacketBuilder();
            packet = new TelemetryDataPacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(builder.getExpectedWindDirectionY()));
        }
    }
}
