package com.senorpez.projectcars2.racedata;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketBuilder.*;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ParticipantVehicleNamesPacketTest {
    private final static int VEHICLES_PER_PACKET = 16;
    private final static int VEHICLE_NAME_LENGTH_MAX = 64;

    private ParticipantVehicleNamesPacket packet;

    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES.ordinal());
        packet = new ParticipantVehicleNamesPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setExpectedPacketType((short) 0);
        packet = new ParticipantVehicleNamesPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new ParticipantVehicleNamesPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new ParticipantVehicleNamesPacket(builder.build());
    }

    @Test
    public void getIndex() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder();
        packet = new ParticipantVehicleNamesPacket(builder.build());
        final List<Integer> indexs = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getIndex)
                .collect(Collectors.toList());
        final List<Integer> expectedIndexs = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> builder.getVehicleInfoBuilder().getExpectedIndex())
                .collect(Collectors.toList());
        assertThat(indexs, is(expectedIndexs));
    }

    @Test
    public void getIndex_MaxValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder viBuilder = new ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder()
                .setExpectedIndex(MAX_UNSIGNED_SHORT);
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        List<Integer> indexs = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getIndex)
                .collect(Collectors.toList());
        List<Integer> expectedIndexs = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_SHORT)
                .collect(Collectors.toList());
        assertThat(indexs, is(expectedIndexs));

        viBuilder.setExpectedIndex(MAX_UNSIGNED_SHORT + 1);
        builder.setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        indexs = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getIndex)
                .collect(Collectors.toList());
        expectedIndexs = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_SHORT + 1)
                .collect(Collectors.toList());
        assertThat(indexs, is(not(expectedIndexs)));
    }

    @Test
    public void getIndex_MinValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder viBuilder = new ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder()
                .setExpectedIndex(MIN_UNSIGNED_SHORT);
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        List<Integer> indexs = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getIndex)
                .collect(Collectors.toList());
        List<Integer> expectedIndexs = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_SHORT)
                .collect(Collectors.toList());
        assertThat(indexs, is(expectedIndexs));

        viBuilder.setExpectedIndex(MIN_UNSIGNED_SHORT - 1);
        builder.setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        indexs = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getIndex)
                .collect(Collectors.toList());
        expectedIndexs = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_SHORT - 1)
                .collect(Collectors.toList());
        assertThat(indexs, is(not(expectedIndexs)));
    }

    @Test
    public void getCarClass() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder();
        packet = new ParticipantVehicleNamesPacket(builder.build());
        final List<Long> CarClasss = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getCarClass)
                .collect(Collectors.toList());
        final List<Long> expectedCarClasss = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> builder.getVehicleInfoBuilder().getExpectedCarClass())
                .collect(Collectors.toList());
        assertThat(CarClasss, is(expectedCarClasss));
    }

    @Test
    public void getCarClass_MaxValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder viBuilder = new ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder()
                .setExpectedCarClass(MAX_UNSIGNED_INTEGER);
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        List<Long> CarClasss = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getCarClass)
                .collect(Collectors.toList());
        List<Long> expectedCarClasss = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_INTEGER)
                .collect(Collectors.toList());
        assertThat(CarClasss, is(expectedCarClasss));

        viBuilder.setExpectedCarClass(MAX_UNSIGNED_INTEGER + 1);
        builder.setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        CarClasss = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getCarClass)
                .collect(Collectors.toList());
        expectedCarClasss = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_INTEGER + 1)
                .collect(Collectors.toList());
        assertThat(CarClasss, is(not(expectedCarClasss)));
    }

    @Test
    public void getCarClass_MinValue() throws Exception {
        final ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder viBuilder = new ParticipantVehicleNamesPacketBuilder.VehicleInfoBuilder()
                .setExpectedCarClass(MIN_UNSIGNED_INTEGER);
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder()
                .setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        List<Long> CarClasss = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getCarClass)
                .collect(Collectors.toList());
        List<Long> expectedCarClasss = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_INTEGER)
                .collect(Collectors.toList());
        assertThat(CarClasss, is(expectedCarClasss));

        viBuilder.setExpectedCarClass(MIN_UNSIGNED_INTEGER - 1);
        builder.setVehicleInfoBuilder(viBuilder);
        packet = new ParticipantVehicleNamesPacket(builder.build());
        CarClasss = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getCarClass)
                .collect(Collectors.toList());
        expectedCarClasss = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_INTEGER - 1)
                .collect(Collectors.toList());
        assertThat(CarClasss, is(not(expectedCarClasss)));
    }

    @Test
    public void getName() throws Exception {
        final ParticipantVehicleNamesPacketBuilder builder = new ParticipantVehicleNamesPacketBuilder();
        packet = new ParticipantVehicleNamesPacket(builder.build());
        final List<String> Names = packet
                .getVehicleInfo()
                .stream()
                .map(ParticipantVehicleNamesPacket.VehicleInfo::getName)
                .collect(Collectors.toList());
        final List<String> expectedNames = IntStream
                .range(0, VEHICLES_PER_PACKET)
                .mapToObj(v -> builder.getVehicleInfoBuilder().getExpectedName())
                .collect(Collectors.toList());
        assertThat(Names, is(expectedNames));
    }
}
