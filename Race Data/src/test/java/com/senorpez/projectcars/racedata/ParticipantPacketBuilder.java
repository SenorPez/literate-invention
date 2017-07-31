package com.senorpez.projectcars.racedata;

import java.io.*;
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
    DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = build(new ByteArrayOutputStream(1347));

        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.write(toBytes(expectedCarName));
        dataOutputStream.write(toBytes(expectedCarClass));
        dataOutputStream.write(toBytes(expectedTrackLocation));
        dataOutputStream.write(toBytes(expectedTrackVariation));
        expectedNames.forEach(name -> {
            try {
                dataOutputStream.write(toBytes(name));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedFastestLapTimes.forEach(lapTime -> {
            try {
                dataOutputStream.writeFloat(lapTime);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    private byte[] toBytes(final String input) {
        final byte[] nameBuffer = new byte[64];
        final byte[] nameBytes = input.getBytes(UTF_8);
        System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
        return nameBuffer;
    }
}
