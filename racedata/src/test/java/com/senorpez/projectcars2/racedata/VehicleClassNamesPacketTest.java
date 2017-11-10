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

public class VehicleClassNamesPacketTest {
    private final static int CLASS_NAME_LENGTH_MAX = 20;
    private final static int CLASSES_SUPPORTED_PER_PACKET = 60;

    private VehicleClassNamesPacket packet;

    @Test(expected = InvalidPacketDataException.class)
    public void throwInvalidPacketData() throws Exception {
        final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
        data.put(10, (byte) PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES.ordinal());
        packet = new VehicleClassNamesPacket(data);
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketData_WrongPacketType() throws Exception {
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder()
                .setExpectedPacketType((short) 0);
        packet = new VehicleClassNamesPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MaxValue() throws Exception {
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder()
                .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
        packet = new VehicleClassNamesPacket(builder.build());
    }

    @Test(expected = InvalidPacketTypeException.class)
    public void throwInvalidPacketTypeException_MinValue() throws Exception {
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder()
                .setExpectedPacketType((short) -1);
        packet = new VehicleClassNamesPacket(builder.build());
    }

    @Test
    public void getClassIndex() throws Exception {
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder();
        packet = new VehicleClassNamesPacket(builder.build());
        final List<Long> ClassIndexs = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getClassIndex)
                .collect(Collectors.toList());
        final List<Long> expectedClassIndexs = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> builder.getClassInfoBuilder().getExpectedClassIndex())
                .collect(Collectors.toList());
        assertThat(ClassIndexs, is(expectedClassIndexs));
    }

    @Test
    public void getClassIndex_MaxValue() throws Exception {
        final VehicleClassNamesPacketBuilder.ClassInfoBuilder clBuilder = new VehicleClassNamesPacketBuilder.ClassInfoBuilder()
                .setExpectedClassIndex(MAX_UNSIGNED_INTEGER);
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder()
                .setClassInfoBuilder(clBuilder);
        packet = new VehicleClassNamesPacket(builder.build());
        List<Long> ClassIndexs = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getClassIndex)
                .collect(Collectors.toList());
        List<Long> expectedClassIndexs = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_INTEGER)
                .collect(Collectors.toList());
        assertThat(ClassIndexs, is(expectedClassIndexs));

        clBuilder.setExpectedClassIndex(MAX_UNSIGNED_INTEGER + 1);
        builder.setClassInfoBuilder(clBuilder);
        packet = new VehicleClassNamesPacket(builder.build());
        ClassIndexs = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getClassIndex)
                .collect(Collectors.toList());
        expectedClassIndexs = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> MAX_UNSIGNED_INTEGER + 1)
                .collect(Collectors.toList());
        assertThat(ClassIndexs, is(not(expectedClassIndexs)));
    }

    @Test
    public void getClassIndex_MinValue() throws Exception {
        final VehicleClassNamesPacketBuilder.ClassInfoBuilder clBuilder = new VehicleClassNamesPacketBuilder.ClassInfoBuilder()
                .setExpectedClassIndex(MIN_UNSIGNED_INTEGER);
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder()
                .setClassInfoBuilder(clBuilder);
        packet = new VehicleClassNamesPacket(builder.build());
        List<Long> ClassIndexs = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getClassIndex)
                .collect(Collectors.toList());
        List<Long> expectedClassIndexs = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_INTEGER)
                .collect(Collectors.toList());
        assertThat(ClassIndexs, is(expectedClassIndexs));

        clBuilder.setExpectedClassIndex(MIN_UNSIGNED_INTEGER - 1);
        builder.setClassInfoBuilder(clBuilder);
        packet = new VehicleClassNamesPacket(builder.build());
        ClassIndexs = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getClassIndex)
                .collect(Collectors.toList());
        expectedClassIndexs = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> MIN_UNSIGNED_INTEGER - 1)
                .collect(Collectors.toList());
        assertThat(ClassIndexs, is(not(expectedClassIndexs)));
    }

    @Test
    public void getName() throws Exception {
        final VehicleClassNamesPacketBuilder builder = new VehicleClassNamesPacketBuilder();
        packet = new VehicleClassNamesPacket(builder.build());
        final List<String> Names = packet
                .getClassInfo()
                .stream()
                .map(VehicleClassNamesPacket.ClassInfo::getName)
                .collect(Collectors.toList());
        final List<String> expectedNames = IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .mapToObj(v -> builder.getClassInfoBuilder().getExpectedName())
                .collect(Collectors.toList());
        assertThat(Names, is(expectedNames));
    }
}
