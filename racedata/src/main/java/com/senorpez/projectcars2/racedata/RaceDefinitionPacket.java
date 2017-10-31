package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;

class RaceDefinitionPacket extends Packet {
    private final static int TRACKNAME_LENGTH_MAX = 64;
    
    private final float worldFastestLapTime;
    private final float personalFastestLapTime;
    private final float personalFastestSector1Time;
    private final float personalFastestSector2Time;
    private final float personalFastestSector3Time;
    private final float worldFastestSector1Time;
    private final float worldFastestSector2Time;
    private final float worldFastestSector3Time;
    private final float trackLength;
    private final String trackLocation;
    private final String trackVariation;
    private final String translatedTrackLocation;
    private final String translatedTrackVariation;
    private final int lapsTimeInEvent;
    private final byte enforcedPitStopLap;
    
    RaceDefinitionPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_RACE_DEFINITION) {
            throw new InvalidPacketTypeException();
        }

        this.worldFastestLapTime = data.getFloat();
        this.personalFastestLapTime = data.getFloat();
        this.personalFastestSector1Time = data.getFloat();
        this.personalFastestSector2Time = data.getFloat();
        this.personalFastestSector3Time = data.getFloat();
        this.worldFastestSector1Time = data.getFloat();
        this.worldFastestSector2Time = data.getFloat();
        this.worldFastestSector3Time = data.getFloat();
        this.trackLength = data.getFloat();
        this.trackLocation = readString(data, TRACKNAME_LENGTH_MAX);
        this.trackVariation = readString(data, TRACKNAME_LENGTH_MAX);
        this.translatedTrackLocation = readString(data, TRACKNAME_LENGTH_MAX);
        this.translatedTrackVariation = readString(data, TRACKNAME_LENGTH_MAX);
        this.lapsTimeInEvent = readUnsignedShort(data);
        this.enforcedPitStopLap = data.get();

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    float getWorldFastestLapTime() {
        return worldFastestLapTime;
    }

    float getPersonalFastestLapTime() {
        return personalFastestLapTime;
    }

    float getPersonalFastestSector1Time() {
        return personalFastestSector1Time;
    }

    float getPersonalFastestSector2Time() {
        return personalFastestSector2Time;
    }

    float getPersonalFastestSector3Time() {
        return personalFastestSector3Time;
    }

    float getWorldFastestSector1Time() {
        return worldFastestSector1Time;
    }

    float getWorldFastestSector2Time() {
        return worldFastestSector2Time;
    }

    float getWorldFastestSector3Time() {
        return worldFastestSector3Time;
    }

    float getTrackLength() {
        return trackLength;
    }

    String getTrackLocation() {
        return trackLocation;
    }

    String getTrackVariation() {
        return trackVariation;
    }

    String getTranslatedTrackLocation() {
        return translatedTrackLocation;
    }

    String getTranslatedTrackVariation() {
        return translatedTrackVariation;
    }

    boolean isTimedRace() {
        final int mask = 128; /* 1000 0000 */
        return (lapsTimeInEvent & mask) == mask;
    }

    Integer getLaps() {
        final int mask = 127; /* 0111 1111 */
        return isTimedRace() ? null : lapsTimeInEvent & mask;
    }

    Integer getTime() {
        final int mask = 127; /* 0111 1111 */
        return isTimedRace() ? (lapsTimeInEvent & mask) * 5 : null;
    }

    byte getEnforcedPitStopLap() {
        return enforcedPitStopLap;
    }
}
