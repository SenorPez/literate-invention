package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ParticipantPacket extends Packet {
    private final static PacketType packetType = PacketType.PARTICIPANT;

    private final String carName;
    private final String carClass;
    private final String trackLocation;
    private final String trackVariation;
    private final List<String> names;
    private final List<Float> fastestLapTimes;

    ParticipantPacket(final ByteBuffer data) throws InvalidPacketException {
        super(data);
        if (!isCorrectPacketType(packetType)) {
            throw new InvalidPacketException();
        }

        this.carName = readString(data);
        this.carClass = readString(data);
        this.trackLocation = readString(data);
        this.trackVariation = readString(data);

        this.names = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj(value -> readString(data))
                        .collect(Collectors.toList()));

        this.fastestLapTimes = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj(value -> data.getFloat())
                        .collect(Collectors.toList()));

        if (data.remaining() > 0) {
            throw new InvalidPacketException();
        }
    }

    @Override
    PacketType getPacketType() {
        return packetType;
    }

    String getCarName() {
        return carName;
    }

    String getCarClass() {
        return carClass;
    }

    String getTrackLocation() {
        return trackLocation;
    }

    String getTrackVariation() {
        return trackVariation;
    }

    List<String> getNames() {
        return names;
    }

    List<Float> getFastestLapTimes() {
        return fastestLapTimes;
    }
}
