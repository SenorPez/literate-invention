package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

class ParticipantPacketBuilder extends PacketBuilder {
    private String expectedCarName = StringGenerator.GetString();
    private String expectedCarClass = StringGenerator.GetString();
    private String expectedTrackLocation = StringGenerator.GetString();
    private String expectedTrackVariation = StringGenerator.GetString();
    private List<String> expectedNames = Collections.unmodifiableList(
            IntStream.range(0, 16)
                    .mapToObj(value -> StringGenerator.GetString(random.nextInt(62)))
                    .collect(Collectors.toList()));
    private final List<Float> expectedFastestLapTimes = Collections.unmodifiableList(
            IntStream.range(0, 16)
                    .mapToObj(value -> random.nextFloat() * random.nextInt(10))
                    .collect(Collectors.toList()));

    ParticipantPacketBuilder() {
        super();
        this.setExpectedPacketType((short) 1);
    }

    String getExpectedCarName() {
        return expectedCarName;
    }

     ParticipantPacketBuilder setExpectedCarName(final String expectedCarName) {
        this.expectedCarName = expectedCarName;
        return this;
    }

    String getExpectedCarClass() {
        return expectedCarClass;
    }

    ParticipantPacketBuilder setExpectedCarClass(final String expectedCarClass) {
        this.expectedCarClass = expectedCarClass;
        return this;
    }

    String getExpectedTrackLocation() {
        return expectedTrackLocation;
    }

    ParticipantPacketBuilder setExpectedTrackLocation(final String expectedTrackLocation) {
        this.expectedTrackLocation = expectedTrackLocation;
        return this;
    }

    String getExpectedTrackVariation() {
        return expectedTrackVariation;
    }

    ParticipantPacketBuilder setExpectedTrackVariation(final String expectedTrackVariation) {
        this.expectedTrackVariation = expectedTrackVariation;
        return this;
    }

    List<String> getExpectedNames() {
        return expectedNames;
    }

    ParticipantPacketBuilder setExpectedNames(final List<String> expectedNames) {
        this.expectedNames = expectedNames;
        return this;
    }

    List<Float> getExpectedFastestLapTimes() {
        return expectedFastestLapTimes;
    }

    @Override
    ByteBuffer build() throws Exception {
        final ByteBuffer data = build(ByteBuffer.allocate(1347).order(ByteOrder.LITTLE_ENDIAN));

        writeString(expectedCarName, data);
        writeString(expectedCarClass, data);
        writeString(expectedTrackLocation, data);
        writeString(expectedTrackVariation, data);
        expectedNames.forEach(name -> writeString(name, data));
        expectedFastestLapTimes.forEach(data::putFloat);

        data.rewind();
        return data;
    }

    private byte[] toBytes(final String input) {
        final byte[] nameBuffer = new byte[64];
        final byte[] nameBytes = input.getBytes(UTF_8);
        System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
        return nameBuffer;
    }
}
