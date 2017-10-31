package com.senorpez.projectcars2.racedata;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.CarPhysicsPacketBuilder.MAX_UNSIGNED_SHORT;
import static com.senorpez.projectcars2.racedata.CarPhysicsPacketBuilder.MIN_UNSIGNED_SHORT;
import static com.senorpez.projectcars2.racedata.PacketBuilder.*;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

@RunWith(Enclosed.class)
public class CarPhysicsPacketTest {
    @RunWith(Parameterized.class)
    public static class TyreFlagsTests {
        private CarPhysicsPacket packet;

        @Parameter()
        public short value;

        @Parameters(name = "Tyre Flags: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 1 << 3).forEach(value1 -> output.add(new Object[]{(short) value1}));
            return output;
        }

        @Test
        public void isTyreAttached() throws Exception {
            final List<Short> values = Arrays.asList(value, value, value, value);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreFlags(values);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 1; /* 0000 0001 */
            final List<Boolean> valueCheck = values.stream().map(v -> (v & mask) == mask).collect(Collectors.toList());
            assertThat(packet.isTyreAttached(), is(valueCheck));
        }

        @Test
        public void isTyreInflated() throws Exception {
            final List<Short> values = Arrays.asList(value, value, value, value);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreFlags(values);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 2; /* 0000 0010 */
            final List<Boolean> valueCheck = values.stream().map(v -> (v & mask) == mask).collect(Collectors.toList());
            assertThat(packet.isTyreInflated(), is(valueCheck));
        }

        @Test
        public void isTyreIsOnGround() throws Exception {
            final List<Short> values = Arrays.asList(value, value, value, value);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreFlags(values);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 4; /* 0000 0100 */
            final List<Boolean> valueCheck = values.stream().map(v -> (v & mask) == mask).collect(Collectors.toList());
            assertThat(packet.isTyreIsOnGround(), is(valueCheck));
        }
    }

    @RunWith(Parameterized.class)
    public static class CarFlagsTests {
        private CarPhysicsPacket packet;

        @Parameter()
        public short value;

        @Parameters(name = "Car Flags: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream.range(0, 1 << 6).forEach(value1 -> output.add(new Object[]{(short) value1}));
            return output;
        }

        @Test
        public void isHeadlight() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 1; /* 0000 0001 */
            assertThat(packet.isHeadlight(), is((value & mask) == mask));
        }

        @Test
        public void isEngineActive() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 2; /* 0000 0010 */
            assertThat(packet.isEngineActive(), is((value & mask) == mask));
        }

        @Test
        public void isEngineWarning() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 4; /* 0000 0100 */
            assertThat(packet.isEngineWarning(), is((value & mask) == mask));
        }

        @Test
        public void isSpeedLimiter() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 8; /* 0000 1000 */
            assertThat(packet.isSpeedLimiter(), is((value & mask) == mask));
        }

        @Test
        public void isAbs() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 16; /* 0001 0000 */
            assertThat(packet.isAbs(), is((value & mask) == mask));
        }

        @Test
        public void isHandbrake() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCarFlags(value);
            packet = new CarPhysicsPacket(builder.build());
            final int mask = 32; /* 0010 0000 */
            assertThat(packet.isHandbrake(), is((value & mask) == mask));
        }
    }

    @RunWith(Parameterized.class)
    public static class CrashDamageStateTests {
        private CarPhysicsPacket packet;

        @Parameter()
        public CrashDamageState crashDamageState;

        @Parameters(name = "Crash Damage State: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, CrashDamageState.CRASH_DAMAGE_MAX.ordinal())
                    .forEach(value -> output.add(new Object[]{CrashDamageState.valueOf(value)}));
            return output;
        }

        @Test
        public void getCrashDamageState() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCrashState((short) crashDamageState.ordinal());
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getCrashDamageState(), is(crashDamageState));
        }
    }

    @RunWith(Parameterized.class)
    public static class TerrainTests {
        private CarPhysicsPacket packet;

        @Parameter()
        public Terrain terrain;

        @Parameters(name = "Terrain: {0}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, Terrain.TERRAIN_MAX.ordinal())
                    .forEach(value -> output.add(new Object[]{Terrain.valueOf(value)}));
            return output;
        }

        @Test
        public void getTerrain() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTerrain(Arrays.asList((short) terrain.ordinal(), (short) terrain.ordinal(), (short) terrain.ordinal(), (short) terrain.ordinal()));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTerrain(), is(Arrays.asList(terrain, terrain, terrain, terrain)));
        }
    }

    public static class SingleTests {
        private CarPhysicsPacket packet;

        @Test(expected = InvalidPacketDataException.class)
        public void throwInvalidPacketData() throws Exception {
            final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
            packet = new CarPhysicsPacket(data);
        }

        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketType_WrongPacketType() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedPacketType((short) 2);
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketTypeException_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketTypeException_MinValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedPacketType((short) -1);
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test
        public void getViewedParticipantIndex() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(builder.getExpectedViewedParticipantIndex()));
        }

        @Test
        public void getViewedParticipantIndex_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedViewedParticipantIndex(Byte.MAX_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(Byte.MAX_VALUE));

            builder.setExpectedViewedParticipantIndex((byte) (Byte.MAX_VALUE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getViewedParticipantIndex_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedViewedParticipantIndex(Byte.MIN_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(Byte.MIN_VALUE));

            builder.setExpectedViewedParticipantIndex((byte) (Byte.MIN_VALUE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getViewedParticipantIndex(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getUnfilteredThrottle() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(builder.getExpectedUnfilteredThrottle()));
        }

        @Test
        public void getUnfilteredThrottle_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredThrottle(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredThrottle((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getUnfilteredThrottle_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredThrottle(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredThrottle((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredThrottle(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getUnfilteredBrake() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(builder.getExpectedUnfilteredBrake()));
        }

        @Test
        public void getUnfilteredBrake_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredBrake(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredBrake((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getUnfilteredBrake_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredBrake(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredBrake((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredBrake(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getUnfilteredSteering() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(builder.getExpectedUnfilteredSteering()));
        }

        @Test
        public void getUnfilteredSteering_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredSteering(Byte.MAX_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(Byte.MAX_VALUE));

            builder.setExpectedUnfilteredSteering((byte) (Byte.MAX_VALUE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getUnfilteredSteering_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredSteering(Byte.MIN_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(Byte.MIN_VALUE));

            builder.setExpectedUnfilteredSteering((byte) (Byte.MIN_VALUE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredSteering(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getUnfilteredClutch() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(builder.getExpectedUnfilteredClutch()));
        }

        @Test
        public void getUnfilteredClutch_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredClutch(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredClutch((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getUnfilteredClutch_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedUnfilteredClutch(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedUnfilteredClutch((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getUnfilteredClutch(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getOilTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilTemp(), is(builder.getExpectedOilTemp()));
        }

        @Test
        public void getOilTemp_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedOilTemp(Short.MAX_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilTemp(), is(Short.MAX_VALUE));

            builder.setExpectedOilTemp((byte) (Short.MAX_VALUE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilTemp(), is(not(Short.MAX_VALUE + 1)));
        }

        @Test
        public void getOilTemp_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedOilTemp(Short.MIN_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilTemp(), is(Short.MIN_VALUE));

            builder.setExpectedOilTemp((byte) (Short.MIN_VALUE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilTemp(), is(not(Short.MIN_VALUE - 1)));
        }

        @Test
        public void getOilPressure() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilPressure(), is(builder.getExpectedOilPressure()));
        }

        @Test
        public void getOilPressure_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedOilPressure(MAX_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilPressure(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedOilPressure((byte) (MAX_UNSIGNED_SHORT + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilPressure(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }

        @Test
        public void getOilPressure_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedOilPressure(MIN_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilPressure(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedOilPressure((byte) (MIN_UNSIGNED_SHORT - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOilPressure(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test
        public void getWaterTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(builder.getExpectedWaterTemp()));
        }

        @Test
        public void getWaterTemp_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWaterTemp(Short.MAX_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(Short.MAX_VALUE));

            builder.setExpectedWaterTemp((byte) (Short.MAX_VALUE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(not(Short.MAX_VALUE + 1)));
        }

        @Test
        public void getWaterTemp_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWaterTemp(Short.MIN_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(Short.MIN_VALUE));

            builder.setExpectedWaterTemp((byte) (Short.MIN_VALUE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterTemp(), is(not(Short.MIN_VALUE - 1)));
        }

        @Test
        public void getWaterPressure() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(builder.getExpectedWaterPressure()));
        }

        @Test
        public void getWaterPressure_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWaterPressure(MAX_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedWaterPressure((byte) (MAX_UNSIGNED_SHORT + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }

        @Test
        public void getWaterPressure_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWaterPressure(MIN_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedWaterPressure((byte) (MIN_UNSIGNED_SHORT - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWaterPressure(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test
        public void getFuelPressure() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(builder.getExpectedFuelPressure()));
        }

        @Test
        public void getFuelPressure_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedFuelPressure(MAX_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedFuelPressure((byte) (MAX_UNSIGNED_SHORT + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }

        @Test
        public void getFuelPressure_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedFuelPressure(MIN_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedFuelPressure((byte) (MIN_UNSIGNED_SHORT - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelPressure(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test
        public void getFuelCapacity() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(builder.getExpectedFuelCapacity()));
        }

        @Test
        public void getFuelCapacity_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedFuelCapacity(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedFuelCapacity((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getFuelCapacity_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedFuelCapacity(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedFuelCapacity((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelCapacity(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getBrake() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrake(), is(builder.getExpectedBrake()));
        }

        @Test
        public void getBrake_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrake(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrake(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedBrake((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrake(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getBrake_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrake(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrake(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedBrake((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrake(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getThrottle() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getThrottle(), is(builder.getExpectedThrottle()));
        }

        @Test
        public void getThrottle_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedThrottle(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getThrottle(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedThrottle((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getThrottle(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getThrottle_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedThrottle(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getThrottle(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedThrottle((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getThrottle(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getClutch() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getClutch(), is(builder.getExpectedClutch()));
        }

        @Test
        public void getClutch_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedClutch(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getClutch(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedClutch((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getClutch(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getClutch_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedClutch(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getClutch(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedClutch((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getClutch(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getFuelLevel() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getFuelLevel(), is(builder.getExpectedFuelLevel()));
        }

        @Test
        public void getSpeed() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSpeed(), is(builder.getExpectedSpeed()));
        }

        @Test
        public void getRpm() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRpm(), is(builder.getExpectedRpm()));
        }

        @Test
        public void getRpm_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedRpm(MAX_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRpm(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedRpm((byte) (MAX_UNSIGNED_SHORT + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRpm(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }

        @Test
        public void getRpm_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedRpm(MIN_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRpm(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedRpm((byte) (MIN_UNSIGNED_SHORT - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRpm(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test
        public void getMaxRpm() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(builder.getExpectedMaxRpm()));
        }

        @Test
        public void getMaxRpm_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedMaxRpm(MAX_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedMaxRpm((byte) (MAX_UNSIGNED_SHORT + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }

        @Test
        public void getMaxRpm_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedMaxRpm(MIN_UNSIGNED_SHORT);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedMaxRpm((byte) (MIN_UNSIGNED_SHORT - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getMaxRpm(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test
        public void getSteering() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSteering(), is(builder.getExpectedSteering()));
        }

        @Test
        public void getSteering_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSteering(Byte.MAX_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSteering(), is(Byte.MAX_VALUE));

            builder.setExpectedSteering((byte) (Byte.MAX_VALUE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSteering(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getSteering_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSteering(Byte.MIN_VALUE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSteering(), is(Byte.MIN_VALUE));

            builder.setExpectedSteering((byte) (Byte.MIN_VALUE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSteering(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getGearNumGears() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getGearNumGears(), is(builder.getExpectedGearNumGears()));
        }

        @Test
        public void getGearNumGears_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedGearNumGears(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getGearNumGears(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedGearNumGears((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getGearNumGears(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getGearNumGears_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedGearNumGears(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getGearNumGears(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedGearNumGears((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getGearNumGears(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getBoostAmount() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(builder.getExpectedBoostAmount()));
        }

        @Test
        public void getBoostAmount_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBoostAmount(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedBoostAmount((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getBoostAmount_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBoostAmount(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedBoostAmount((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBoostAmount(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test(expected = InvalidCrashDamageStateException.class)
        public void getCrashState_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCrashState((short) CrashDamageState.CRASH_DAMAGE_MAX.ordinal());
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test(expected = InvalidCrashDamageStateException.class)
        public void getCrashState_MinValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedCrashState((short) -1);
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test
        public void getOdometer() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOdometer(), is(builder.getExpectedOdometer()));
        }

        @Test
        public void getOrientation() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getOrientation(), contains(builder.getExpectedOrientation().toArray()));
        }

        @Test
        public void getLocalVelocity() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getLocalVelocity(), contains(builder.getExpectedLocalVelocity().toArray()));
        }

        @Test
        public void getWorldVelocity() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWorldVelocity(), contains(builder.getExpectedWorldVelocity().toArray()));
        }

        @Test
        public void getAngularVelocity() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAngularVelocity(), contains(builder.getExpectedAngularVelocity().toArray()));
        }

        @Test
        public void getLocalAcceleration() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getLocalAcceleration(), contains(builder.getExpectedLocalAcceleration().toArray()));
        }

        @Test
        public void getWorldAcceleration() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWorldAcceleration(), contains(builder.getExpectedWorldAcceleration().toArray()));
        }

        @Test
        public void getExtentsCentre() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getExtentsCentre(), contains(builder.getExpectedExtentsCentre().toArray()));
        }

        @Test(expected = InvalidTerrainException.class)
        public void getTerrain_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTerrain(Arrays.asList(
                            (short) Terrain.TERRAIN_MAX.ordinal(),
                            (short) Terrain.TERRAIN_MAX.ordinal(),
                            (short) Terrain.TERRAIN_MAX.ordinal(),
                            (short) Terrain.TERRAIN_MAX.ordinal()
                    ));
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test(expected = InvalidTerrainException.class)
        public void getTerrain_MinValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTerrain(Arrays.asList(
                            (short) -1,
                            (short) -1,
                            (short) -1,
                            (short) -1
                    ));
            packet = new CarPhysicsPacket(builder.build());
        }

        @Test
        public void getTyreY() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreY(), contains(builder.getExpectedTyreY().toArray()));
        }

        @Test
        public void getTyreRps() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRps(), contains(builder.getExpectedTyreRps().toArray()));
        }

        @Test
        public void getTyreTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTemp(), contains(builder.getExpectedTyreTemp().toArray()));
        }

        @Test
        public void getTyreTemp_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTemp(), contains(builder.getExpectedTyreTemp().toArray()));

            values = Arrays.asList(
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1)
            );
            builder.setExpectedTyreTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTemp(), is(not(builder.getExpectedTyreTemp().toArray())));
        }

        @Test
        public void getTyreTemp_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTemp(), is(builder.getExpectedTyreTemp()));

            values = Arrays.asList(
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1)
            );
            builder.setExpectedTyreTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTemp(), is(not(builder.getExpectedTyreTemp())));
        }

        @Test
        public void getTyreHeightAboveGround() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreHeightAboveGround(), contains(builder.getExpectedTyreHeightAboveGround().toArray()));
        }

        @Test
        public void getTyreWear() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreWear(), contains(builder.getExpectedTyreWear().toArray()));
        }

        @Test
        public void getTyreWear_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreWear(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreWear(), contains(builder.getExpectedTyreWear().toArray()));

            values = Arrays.asList(
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1)
            );
            builder.setExpectedTyreWear(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreWear(), is(not(builder.getExpectedTyreWear().toArray())));
        }

        @Test
        public void getTyreWear_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreWear(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreWear(), is(builder.getExpectedTyreWear()));

            values = Arrays.asList(
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1)
            );
            builder.setExpectedTyreWear(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreWear(), is(not(builder.getExpectedTyreWear())));
        }

        @Test
        public void getBrakeDamage() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeDamage(), contains(builder.getExpectedBrakeDamage().toArray()));
        }

        @Test
        public void getBrakeDamage_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrakeDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeDamage(), contains(builder.getExpectedBrakeDamage().toArray()));

            values = Arrays.asList(
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1)
            );
            builder.setExpectedBrakeDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeDamage(), is(not(builder.getExpectedBrakeDamage().toArray())));
        }

        @Test
        public void getBrakeDamage_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrakeDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeDamage(), is(builder.getExpectedBrakeDamage()));

            values = Arrays.asList(
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1)
            );
            builder.setExpectedBrakeDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeDamage(), is(not(builder.getExpectedBrakeDamage())));
        }

        @Test
        public void getSuspensionDamage() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), contains(builder.getExpectedSuspensionDamage().toArray()));
        }

        @Test
        public void getSuspensionDamage_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSuspensionDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), contains(builder.getExpectedSuspensionDamage().toArray()));

            values = Arrays.asList(
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1)
            );
            builder.setExpectedSuspensionDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), is(not(builder.getExpectedSuspensionDamage().toArray())));
        }

        @Test
        public void getSuspensionDamage_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSuspensionDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), is(builder.getExpectedSuspensionDamage()));

            values = Arrays.asList(
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1)
            );
            builder.setExpectedSuspensionDamage(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionDamage(), is(not(builder.getExpectedSuspensionDamage())));
        }

        @Test
        public void getBrakeTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeTemp(), contains(builder.getExpectedBrakeTemp().toArray()));
        }

        @Test
        public void getBrakeTemp_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrakeTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeTemp(), contains(builder.getExpectedBrakeTemp().toArray()));

            values = Arrays.asList(
                    (short) (Short.MAX_VALUE + 1),
                    (short) (Short.MAX_VALUE + 1),
                    (short) (Short.MAX_VALUE + 1),
                    (short) (Short.MAX_VALUE + 1)
            );
            builder.setExpectedBrakeTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeTemp(), is(not(builder.getExpectedBrakeTemp().toArray())));
        }

        @Test
        public void getBrakeTemp_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedBrakeTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeTemp(), is(builder.getExpectedBrakeTemp()));

            values = Arrays.asList(
                    (short) (Short.MIN_VALUE - 1),
                    (short) (Short.MIN_VALUE - 1),
                    (short) (Short.MIN_VALUE - 1),
                    (short) (Short.MIN_VALUE - 1)
            );
            builder.setExpectedBrakeTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getBrakeTemp(), is(not(builder.getExpectedBrakeTemp().toArray())));
        }

        @Test
        public void getTyreTreadTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), contains(builder.getExpectedTyreTreadTemp().toArray()));
        }

        @Test
        public void getTyreTreadTemp_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTreadTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), contains(builder.getExpectedTyreTreadTemp().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreTreadTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), is(not(builder.getExpectedTyreTreadTemp().toArray())));
        }

        @Test
        public void getTyreTreadTemp_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTreadTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), is(builder.getExpectedTyreTreadTemp()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreTreadTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTreadTemp(), is(not(builder.getExpectedTyreTreadTemp())));
        }

        @Test
        public void getTyreLayerTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), contains(builder.getExpectedTyreLayerTemp().toArray()));
        }

        @Test
        public void getTyreLayerTemp_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreLayerTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), contains(builder.getExpectedTyreLayerTemp().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreLayerTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), is(not(builder.getExpectedTyreLayerTemp().toArray())));
        }

        @Test
        public void getTyreLayerTemp_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreLayerTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), is(builder.getExpectedTyreLayerTemp()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreLayerTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreLayerTemp(), is(not(builder.getExpectedTyreLayerTemp())));
        }

        @Test
        public void getTyreCarcassTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), contains(builder.getExpectedTyreCarcassTemp().toArray()));
        }

        @Test
        public void getTyreCarcassTemp_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreCarcassTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), contains(builder.getExpectedTyreCarcassTemp().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreCarcassTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), is(not(builder.getExpectedTyreCarcassTemp().toArray())));
        }

        @Test
        public void getTyreCarcassTemp_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreCarcassTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), is(builder.getExpectedTyreCarcassTemp()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreCarcassTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCarcassTemp(), is(not(builder.getExpectedTyreCarcassTemp())));
        }

        @Test
        public void getTyreRimTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), contains(builder.getExpectedTyreRimTemp().toArray()));
        }

        @Test
        public void getTyreRimTemp_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreRimTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), contains(builder.getExpectedTyreRimTemp().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreRimTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), is(not(builder.getExpectedTyreRimTemp().toArray())));
        }

        @Test
        public void getTyreRimTemp_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreRimTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), is(builder.getExpectedTyreRimTemp()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreRimTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreRimTemp(), is(not(builder.getExpectedTyreRimTemp())));
        }

        @Test
        public void getTyreInternalAirTemp() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), contains(builder.getExpectedTyreInternalAirTemp().toArray()));
        }

        @Test
        public void getTyreInternalAirTemp_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreInternalAirTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), contains(builder.getExpectedTyreInternalAirTemp().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreInternalAirTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), is(not(builder.getExpectedTyreInternalAirTemp().toArray())));
        }

        @Test
        public void getTyreInternalAirTemp_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreInternalAirTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), is(builder.getExpectedTyreInternalAirTemp()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreInternalAirTemp(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreInternalAirTemp(), is(not(builder.getExpectedTyreInternalAirTemp())));
        }

        @Test
        public void getTyreTempLeft() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempLeft(), contains(builder.getExpectedTyreTempLeft().toArray()));
        }

        @Test
        public void getTyreTempLeft_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempLeft(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempLeft(), contains(builder.getExpectedTyreTempLeft().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreTempLeft(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempLeft(), is(not(builder.getExpectedTyreTempLeft().toArray())));
        }

        @Test
        public void getTyreTempLeft_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempLeft(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempLeft(), is(builder.getExpectedTyreTempLeft()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreTempLeft(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempLeft(), is(not(builder.getExpectedTyreTempLeft())));
        }

        @Test
        public void getTyreTempCenter() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempCenter(), contains(builder.getExpectedTyreTempCenter().toArray()));
        }

        @Test
        public void getTyreTempCenter_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempCenter(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempCenter(), contains(builder.getExpectedTyreTempCenter().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreTempCenter(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempCenter(), is(not(builder.getExpectedTyreTempCenter().toArray())));
        }

        @Test
        public void getTyreTempCenter_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempCenter(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempCenter(), is(builder.getExpectedTyreTempCenter()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreTempCenter(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempCenter(), is(not(builder.getExpectedTyreTempCenter())));
        }

        @Test
        public void getTyreTempRight() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempRight(), contains(builder.getExpectedTyreTempRight().toArray()));
        }

        @Test
        public void getTyreTempRight_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempRight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempRight(), contains(builder.getExpectedTyreTempRight().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedTyreTempRight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempRight(), is(not(builder.getExpectedTyreTempRight().toArray())));
        }

        @Test
        public void getTyreTempRight_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedTyreTempRight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempRight(), is(builder.getExpectedTyreTempRight()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedTyreTempRight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreTempRight(), is(not(builder.getExpectedTyreTempRight())));
        }

        @Test
        public void getWheelLocalPositionY() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWheelLocalPositionY(), contains(builder.getExpectedWheelLocalPositionY().toArray()));
        }

        @Test
        public void getRideHeight() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getRideHeight(), contains(builder.getExpectedRideHeight().toArray()));
        }

        @Test
        public void getSuspensionTravel() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionTravel(), contains(builder.getExpectedSuspensionTravel().toArray()));
        }

        @Test
        public void getSuspensionVelocity() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionVelocity(), contains(builder.getExpectedSuspensionVelocity().toArray()));
        }

        @Test
        public void getSuspensionRideHeight() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionRideHeight(), contains(builder.getExpectedSuspensionRideHeight().toArray()));
        }

        @Test
        public void getSuspensionRideHeight_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSuspensionRideHeight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionRideHeight(), contains(builder.getExpectedSuspensionRideHeight().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedSuspensionRideHeight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionRideHeight(), is(not(builder.getExpectedSuspensionRideHeight().toArray())));
        }

        @Test
        public void getSuspensionRideHeight_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedSuspensionRideHeight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionRideHeight(), is(builder.getExpectedSuspensionRideHeight()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedSuspensionRideHeight(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getSuspensionRideHeight(), is(not(builder.getExpectedSuspensionRideHeight())));
        }

        @Test
        public void getAirPressure() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAirPressure(), contains(builder.getExpectedAirPressure().toArray()));
        }

        @Test
        public void getAirPressure_MaxValue() throws Exception {
            List<Integer> values = Arrays.asList(MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT, MAX_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedAirPressure(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAirPressure(), contains(builder.getExpectedAirPressure().toArray()));

            values = Arrays.asList(
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1,
                    MAX_UNSIGNED_SHORT + 1
            );
            builder.setExpectedAirPressure(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAirPressure(), is(not(builder.getExpectedAirPressure().toArray())));
        }

        @Test
        public void getAirPressure_Min_Value() throws Exception {
            List<Integer> values = Arrays.asList(MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT, MIN_UNSIGNED_SHORT);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedAirPressure(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAirPressure(), is(builder.getExpectedAirPressure()));

            values = Arrays.asList(
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1,
                    MIN_UNSIGNED_SHORT - 1
            );
            builder.setExpectedAirPressure(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAirPressure(), is(not(builder.getExpectedAirPressure())));
        }

        @Test
        public void getEngineSpeed() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineSpeed(), is(builder.getExpectedEngineSpeed()));
        }

        @Test
        public void getEngineTorque() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineTorque(), is(builder.getExpectedEngineTorque()));
        }

        @Test
        public void getWings() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWings(), contains(builder.getExpectedWings().toArray()));
        }

        @Test
        public void getWings_MaxValue() throws Exception {
            List<Short> values = Arrays.asList(MAX_UNSIGNED_BYTE, MAX_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWings(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWings(), contains(builder.getExpectedWings().toArray()));

            values = Arrays.asList(
                    (short) (MAX_UNSIGNED_BYTE + 1),
                    (short) (MAX_UNSIGNED_BYTE + 1)
            );
            builder.setExpectedWings(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWings(), is(not(builder.getExpectedWings().toArray())));
        }

        @Test
        public void getWings_Min_Value() throws Exception {
            List<Short> values = Arrays.asList(MIN_UNSIGNED_BYTE, MIN_UNSIGNED_BYTE);
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedWings(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWings(), is(builder.getExpectedWings()));

            values = Arrays.asList(
                    (short) (MIN_UNSIGNED_BYTE - 1),
                    (short) (MIN_UNSIGNED_BYTE - 1)
            );
            builder.setExpectedWings(values);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getWings(), is(not(builder.getExpectedWings())));
        }

        @Test
        public void getHandbrake() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getHandbrake(), is(builder.getExpectedHandbrake()));
        }

        @Test
        public void getHandbrake_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedHandbrake(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getHandbrake(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedHandbrake((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getHandbrake(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getHandbrake_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedHandbrake(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getHandbrake(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedHandbrake((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getHandbrake(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getAeroDamage() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(builder.getExpectedAeroDamage()));
        }

        @Test
        public void getAeroDamage_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedAeroDamage(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedAeroDamage((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getAeroDamage_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedAeroDamage(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedAeroDamage((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getAeroDamage(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getEngineDamage() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(builder.getExpectedEngineDamage()));
        }

        @Test
        public void getEngineDamage_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedEngineDamage(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedEngineDamage((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getEngineDamage_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedEngineDamage(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedEngineDamage((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getEngineDamage(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getJoypad0() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getJoypad0(), is(builder.getExpectedJoypad0()));
        }

        @Test
        public void getJoypad0_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedJoypad0(MAX_UNSIGNED_INTEGER);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getJoypad0(), is(MAX_UNSIGNED_INTEGER));

            builder.setExpectedJoypad0((byte) (MAX_UNSIGNED_INTEGER + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getJoypad0(), is(not(MAX_UNSIGNED_INTEGER + 1)));
        }

        @Test
        public void getJoypad0_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedJoypad0(MIN_UNSIGNED_INTEGER);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getJoypad0(), is(MIN_UNSIGNED_INTEGER));

            builder.setExpectedJoypad0((byte) (MIN_UNSIGNED_INTEGER - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getJoypad0(), is(not(MIN_UNSIGNED_INTEGER - 1)));
        }

        @Test
        public void getDPad() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getdPad(), is(builder.getExpectedDPad()));
        }

        @Test
        public void getDPad_MaxValue() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedDPad(MAX_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getdPad(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedDPad((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getdPad(), is(not(MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getDPad_Min_Value() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder()
                    .setExpectedDPad(MIN_UNSIGNED_BYTE);
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getdPad(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedDPad((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getdPad(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }

        @Test
        public void getTyreCompound() throws Exception {
            final CarPhysicsPacketBuilder builder = new CarPhysicsPacketBuilder();
            packet = new CarPhysicsPacket(builder.build());
            assertThat(packet.getTyreCompound(), contains(builder.getExpectedTyreCompound().toArray()));
        }
    }
}