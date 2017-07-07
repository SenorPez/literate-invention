package com.senorpez.projectcars.racedata;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ParticipantPacket extends Packet {
    private final static PacketType packetType = PacketType.PARTICIPANT;

    private final String carName;
    private final String carClass;
    private final String trackLocation;
    private final String trackVariation;
    private final List<String> names;
    private final List<Float> fastestLapTimes;

    public ParticipantPacket(DataInputStream data) throws IOException, InvalidPacketException {
        super(data);
        if (!isCorrectPacketType(packetType)) {
            throw new InvalidPacketException();
        }

        byte[] nameBuffer = new byte[64];

        data.readFully(nameBuffer);
        this.carName = new String(nameBuffer, UTF_8).split("\u0000", 2)[0];
        data.readFully(nameBuffer);
        this.carClass = new String(nameBuffer, UTF_8).split("\u0000", 2)[0];
        data.readFully(nameBuffer);
        this.trackLocation = new String(nameBuffer, UTF_8).split("\u0000", 2)[0];
        data.readFully(nameBuffer);
        this.trackVariation = new String(nameBuffer, UTF_8).split("\u0000", 2)[0];

        this.names = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj(value -> {
                            try {
                                data.readFully(nameBuffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return new String(nameBuffer, UTF_8).split("\u0000", 2)[0];
                        }).collect(Collectors.toList()));
        this.fastestLapTimes = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj((IntFunctionThrows<Float>) value -> data.readFloat())
                        .collect(Collectors.toList()));
    }

    @Override
    public PacketType getPacketType() {
        return packetType;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarClass() {
        return carClass;
    }

    public String getTrackLocation() {
        return trackLocation;
    }

    public String getTrackVariation() {
        return trackVariation;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Float> getFastestLapTimes() {
        return fastestLapTimes;
    }
}
