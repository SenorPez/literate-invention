package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class RaceDefinitionPacketBuilder extends PacketBuilder {
    private final static Random random = new Random();
    private final static int TRACKNAME_LENGTH_MAX = 64;

    private float expectedWorldFastestLapTime = random.nextFloat();
    private float expectedPersonalFastestLapTime = random.nextFloat();
    private float expectedPersonalFastestSector1Time = random.nextFloat();
    private float expectedPersonalFastestSector2Time = random.nextFloat();
    private float expectedPersonalFastestSector3Time = random.nextFloat();
    private float expectedWorldFastestSector1Time = random.nextFloat();
    private float expectedWorldFastestSector2Time = random.nextFloat();
    private float expectedWorldFastestSector3Time = random.nextFloat();
    private float expectedTrackLength = random.nextFloat();
    private String expectedTrackLocation = generateString(TRACKNAME_LENGTH_MAX);
    private String expectedTrackVariation = generateString(TRACKNAME_LENGTH_MAX);
    private String expectedTranslatedTrackLocation = generateString(TRACKNAME_LENGTH_MAX);
    private String expectedTranslatedTrackVariation = generateString(TRACKNAME_LENGTH_MAX);
    private int expectedLapsTimeInEvent = random.nextInt(MAX_UNSIGNED_SHORT);
    private byte expectedEnforcedPitStopLap = (byte) random.nextInt(Byte.MAX_VALUE);

    RaceDefinitionPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_RACE_DEFINITION.ordinal());
    }

    float getExpectedWorldFastestLapTime() {
        return expectedWorldFastestLapTime;
    }

    RaceDefinitionPacketBuilder setExpectedWorldFastestLapTime(final float expectedWorldFastestLapTime) {
        this.expectedWorldFastestLapTime = expectedWorldFastestLapTime;
        return this;
    }

    float getExpectedPersonalFastestLapTime() {
        return expectedPersonalFastestLapTime;
    }

    RaceDefinitionPacketBuilder setExpectedPersonalFastestLapTime(final float expectedPersonalFastestLapTime) {
        this.expectedPersonalFastestLapTime = expectedPersonalFastestLapTime;
        return this;
    }

    float getExpectedPersonalFastestSector1Time() {
        return expectedPersonalFastestSector1Time;
    }

    RaceDefinitionPacketBuilder setExpectedPersonalFastestSector1Time(final float expectedPersonalFastestSector1Time) {
        this.expectedPersonalFastestSector1Time = expectedPersonalFastestSector1Time;
        return this;
    }

    float getExpectedPersonalFastestSector2Time() {
        return expectedPersonalFastestSector2Time;
    }

    RaceDefinitionPacketBuilder setExpectedPersonalFastestSector2Time(final float expectedPersonalFastestSector2Time) {
        this.expectedPersonalFastestSector2Time = expectedPersonalFastestSector2Time;
        return this;
    }

    float getExpectedPersonalFastestSector3Time() {
        return expectedPersonalFastestSector3Time;
    }

    RaceDefinitionPacketBuilder setExpectedPersonalFastestSector3Time(final float expectedPersonalFastestSector3Time) {
        this.expectedPersonalFastestSector3Time = expectedPersonalFastestSector3Time;
        return this;
    }

    float getExpectedWorldFastestSector1Time() {
        return expectedWorldFastestSector1Time;
    }

    RaceDefinitionPacketBuilder setExpectedWorldFastestSector1Time(final float expectedWorldFastestSector1Time) {
        this.expectedWorldFastestSector1Time = expectedWorldFastestSector1Time;
        return this;
    }

    float getExpectedWorldFastestSector2Time() {
        return expectedWorldFastestSector2Time;
    }

    RaceDefinitionPacketBuilder setExpectedWorldFastestSector2Time(final float expectedWorldFastestSector2Time) {
        this.expectedWorldFastestSector2Time = expectedWorldFastestSector2Time;
        return this;
    }

    float getExpectedWorldFastestSector3Time() {
        return expectedWorldFastestSector3Time;
    }

    RaceDefinitionPacketBuilder setExpectedWorldFastestSector3Time(final float expectedWorldFastestSector3Time) {
        this.expectedWorldFastestSector3Time = expectedWorldFastestSector3Time;
        return this;
    }

    float getExpectedTrackLength() {
        return expectedTrackLength;
    }

    RaceDefinitionPacketBuilder setExpectedTrackLength(final float expectedTrackLength) {
        this.expectedTrackLength = expectedTrackLength;
        return this;
    }

    String getExpectedTrackLocation() {
        return expectedTrackLocation;
    }

    RaceDefinitionPacketBuilder setExpectedTrackLocation(final String expectedTrackLocation) {
        this.expectedTrackLocation = expectedTrackLocation;
        return this;
    }

    String getExpectedTrackVariation() {
        return expectedTrackVariation;
    }

    RaceDefinitionPacketBuilder setExpectedTrackVariation(final String expectedTrackVariation) {
        this.expectedTrackVariation = expectedTrackVariation;
        return this;
    }

    String getExpectedTranslatedTrackLocation() {
        return expectedTranslatedTrackLocation;
    }

    RaceDefinitionPacketBuilder setExpectedTranslatedTrackLocation(final String expectedTranslatedTrackLocation) {
        this.expectedTranslatedTrackLocation = expectedTranslatedTrackLocation;
        return this;
    }

    String getExpectedTranslatedTrackVariation() {
        return expectedTranslatedTrackVariation;
    }

    RaceDefinitionPacketBuilder setExpectedTranslatedTrackVariation(final String expectedTranslatedTrackVariation) {
        this.expectedTranslatedTrackVariation = expectedTranslatedTrackVariation;
        return this;
    }

    int getExpectedLapsTimeInEvent() {
        return expectedLapsTimeInEvent;
    }

    RaceDefinitionPacketBuilder setExpectedLapsTimeInEvent(final int expectedLapsTimeInEvent) {
        this.expectedLapsTimeInEvent = expectedLapsTimeInEvent;
        return this;
    }

    byte getExpectedEnforcedPitStopLap() {
        return expectedEnforcedPitStopLap;
    }

    RaceDefinitionPacketBuilder setExpectedEnforcedPitStopLap(final byte expectedEnforcedPitStopLap) {
        this.expectedEnforcedPitStopLap = expectedEnforcedPitStopLap;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(307).order(LITTLE_ENDIAN));

        data.putFloat(expectedWorldFastestLapTime);
        data.putFloat(expectedPersonalFastestLapTime);
        data.putFloat(expectedPersonalFastestSector1Time);
        data.putFloat(expectedPersonalFastestSector2Time);
        data.putFloat(expectedPersonalFastestSector3Time);
        data.putFloat(expectedWorldFastestSector1Time);
        data.putFloat(expectedWorldFastestSector2Time);
        data.putFloat(expectedWorldFastestSector3Time);
        data.putFloat(expectedTrackLength);
        writeString(expectedTrackLocation, TRACKNAME_LENGTH_MAX, data);
        writeString(expectedTrackVariation, TRACKNAME_LENGTH_MAX, data);
        writeString(expectedTranslatedTrackLocation, TRACKNAME_LENGTH_MAX, data);
        writeString(expectedTranslatedTrackVariation, TRACKNAME_LENGTH_MAX, data);
        writeUnsignedShort(expectedLapsTimeInEvent, data);
        data.put(expectedEnforcedPitStopLap);

        data.rewind();
        return data;
    }
}
