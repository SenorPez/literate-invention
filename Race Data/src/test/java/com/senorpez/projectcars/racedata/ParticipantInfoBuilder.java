package com.senorpez.projectcars.racedata;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ParticipantInfoBuilder extends PacketBuilder {
    private List<Short> expectedWorldPosition = IntStream.range(0, 3).mapToObj(v -> (short) random.nextInt(Short.MAX_VALUE)).collect(Collectors.toList());
    private Integer  worldPositionX = random.nextInt(4);
    private Integer worldPositionZ = random.nextInt(4);
    private Integer expectedCurrentLapDistance = random.nextInt(MAX_UNSIGNED_SHORT);
    private Short expectedRacePosition = (short) random.nextInt(128);
    private Boolean expectedIsActive = random.nextBoolean();
    private Short expectedLapsCompleted = (short) random.nextInt(128);
    private Boolean expectedIsLapInvalidated = random.nextBoolean();
    private Short expectedCurrentLap = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private CurrentSector expectedCurrentSector = CurrentSector.valueOf(random.nextInt(CurrentSector.SECTOR_MAX.ordinal()));
    private Boolean expectedIsSameClass = random.nextBoolean();
    private Float expectedLastSectorTime = random.nextFloat();

    ParticipantInfoBuilder() {}

    List<Float> getExpectedWorldPosition() {
        return Arrays.asList(
                expectedWorldPosition.get(0) + worldPositionX / 4.0f,
                (float) expectedWorldPosition.get(1),
                expectedWorldPosition.get(2) + worldPositionZ / 4.0f);
    }

    ParticipantInfoBuilder setExpectedWorldPosition(final List<Short> expectedWorldPosition) {
        this.expectedWorldPosition = expectedWorldPosition;
        return this;
    }

    ParticipantInfoBuilder setWorldPositionX(final Integer worldPositionX) {
        this.worldPositionX = worldPositionX;
        return this;
    }

    ParticipantInfoBuilder setWorldPositionZ(final Integer worldPositionZ) {
        this.worldPositionZ = worldPositionZ;
        return this;
    }

    Integer getExpectedCurrentLapDistance() {
        return expectedCurrentLapDistance;
    }

    ParticipantInfoBuilder setExpectedCurrentLapDistance(final Integer expectedCurrentLapDistance) {
        this.expectedCurrentLapDistance = expectedCurrentLapDistance;
        return this;
    }

    Short getExpectedRacePosition() {
        return expectedRacePosition;
    }

    ParticipantInfoBuilder setExpectedRacePosition(final Short expectedRacePosition) {
        this.expectedRacePosition = expectedRacePosition;
        return this;
    }

    Boolean getExpectedIsActive() {
        return expectedIsActive;
    }

    ParticipantInfoBuilder setExpectedIsActive(final Boolean expectedIsActive) {
        this.expectedIsActive = expectedIsActive;
        return this;
    }

    Short getExpectedLapsCompleted() {
        return expectedLapsCompleted;
    }

    ParticipantInfoBuilder setExpectedLapsCompleted(final Short expectedLapsCompleted) {
        this.expectedLapsCompleted = expectedLapsCompleted;
        return this;
    }

    Boolean getExpectedIsLapInvalidated() {
        return expectedIsLapInvalidated;
    }

    ParticipantInfoBuilder setExpectedIsLapInvalidated(final Boolean expectedIsLapInvalidated) {
        this.expectedIsLapInvalidated = expectedIsLapInvalidated;
        return this;
    }

    Short getExpectedCurrentLap() {
        return expectedCurrentLap;
    }

    ParticipantInfoBuilder setExpectedCurrentLap(final Short expectedCurrentLap) {
        this.expectedCurrentLap = expectedCurrentLap;
        return this;
    }

    CurrentSector getExpectedCurrentSector() {
        return expectedCurrentSector;
    }

    ParticipantInfoBuilder setExpectedCurrentSector(final CurrentSector expectedCurrentSector) {
        this.expectedCurrentSector = expectedCurrentSector;
        return this;
    }

    Boolean getExpectedIsSameClass() {
        return expectedIsSameClass;
    }

    ParticipantInfoBuilder setExpectedIsSameClass(final Boolean expectedIsSameClass) {
        this.expectedIsSameClass = expectedIsSameClass;
        return this;
    }

    Float getExpectedLastSectorTime() {
        return expectedLastSectorTime;
    }

    ParticipantInfoBuilder setExpectedLastSectorTime(final Float expectedLastSectorTime) {
        this.expectedLastSectorTime = expectedLastSectorTime;
        return this;
    }

    @Override
    public DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        expectedWorldPosition.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        dataOutputStream.writeShort(expectedCurrentLapDistance);
        final Integer isActive = expectedIsActive ? 1 : 0;
        dataOutputStream.writeByte((isActive << 7) | expectedRacePosition);
        final Integer isLapInvalidated = expectedIsLapInvalidated ? 1 : 0;
        dataOutputStream.writeByte((isLapInvalidated << 7) | expectedLapsCompleted);
        dataOutputStream.writeByte(expectedCurrentLap);
        final Integer currentSector = expectedCurrentSector.ordinal();
        final Integer isSameClass = expectedIsSameClass ? 1 : 0;
        dataOutputStream.writeByte((byte) ((worldPositionX << 6) | (worldPositionZ << 4) | (isSameClass << 3) | currentSector));
        dataOutputStream.write(flipFloat(expectedLastSectorTime));

        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
}
