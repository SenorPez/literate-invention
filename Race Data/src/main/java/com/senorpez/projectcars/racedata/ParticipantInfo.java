package com.senorpez.projectcars.racedata;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ParticipantInfo {
    private final List<Short> worldPosition;
    private final Integer currentLapDistance;
    private final Short racePosition;
    private final Short lapsCompleted;
    private final Short currentLap;
    private final Short sector;
    private final Float lastSectorTime;

    ParticipantInfo(final DataInputStream data) throws IOException {
        this.worldPosition = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Short>) value -> data.readShort()).collect(Collectors.toList()));
        this.currentLapDistance = data.readUnsignedShort();
        this.racePosition = (short) data.readUnsignedByte();
        this.lapsCompleted = (short) data.readUnsignedByte();
        this.currentLap = (short) data.readUnsignedByte();
        this.sector = (short) data.readUnsignedByte();
        this.lastSectorTime = TelemetryDataPacket.getLittleFloat(data);
    }

    List<Float> getWorldPosition() {
        return Collections.unmodifiableList(
                Stream
                        .of(
                                worldPosition.get(0) + ((sector >>> 6) / 4.0f),
                                (float) worldPosition.get(1),
                                worldPosition.get(2) + (((48 & sector) >>> 4) / 4.0f))
                        .collect(Collectors.toList()));
    }

    Integer getCurrentLapDistance() {
        return currentLapDistance;
    }

    Short getRacePosition() {
        final Integer mask = 127; /* 0111 1111 */
        return Integer.valueOf(mask & racePosition).shortValue();
    }

    Boolean isActive() {
        final Integer mask = 128; /* 1000 0000 */
        return (mask & racePosition) != 0;
    }

    Short getLapsCompleted() {
        final Integer mask = 127; /* 0111 1111 */
        return Integer.valueOf(mask & lapsCompleted).shortValue();
    }

    Boolean isLapInvalidated() {
        final Integer mask = 128; /* 1000 0000 */
        return (mask & lapsCompleted) != 0;
    }

    Short getCurrentLap() {
        return currentLap;
    }

    CurrentSector getCurrentSector() {
        final Integer mask = 7; /* 0000 0111 */
        return CurrentSector.valueOf(mask & sector);
    }

    Boolean isSameClass() {
        final Integer mask = 8; /* 0000 1000 */
        return (mask & sector) != 0;
    }

    Float getLastSectorTime() {
        return lastSectorTime;
    }
}
