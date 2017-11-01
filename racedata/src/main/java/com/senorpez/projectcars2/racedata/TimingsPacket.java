package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TimingsPacket extends Packet {
    private final static int UDP_STREAMER_PARTICIPANTS_SUPPORTED = 32;

    private final byte numParticipants;
    private final long participantsChangedTimestamp;
    private final float eventTimeRemaining;
    private final float splitTimeAhead;
    private final float splitTimeBehind;
    private final float splitTime;
    private final List<ParticipantInfo> participants;

    TimingsPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_TIMINGS) {
            throw new InvalidPacketTypeException();
        }

        this.numParticipants = data.get();
        this.participantsChangedTimestamp = readUnsignedInt(data);
        this.eventTimeRemaining = data.getFloat();
        this.splitTimeAhead = data.getFloat();
        this.splitTimeBehind = data.getFloat();
        this.splitTime = data.getFloat();

        this.participants = Collections.unmodifiableList(IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .mapToObj(v -> new ParticipantInfo(data))
                .collect(Collectors.toList()));
        
        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    byte getNumParticipants() {
        return numParticipants;
    }

    long getParticipantsChangedTimestamp() {
        return participantsChangedTimestamp;
    }

    float getEventTimeRemaining() {
        return eventTimeRemaining;
    }

    float getSplitTimeAhead() {
        return splitTimeAhead;
    }

    float getSplitTimeBehind() {
        return splitTimeBehind;
    }

    float getSplitTime() {
        return splitTime;
    }

    List<ParticipantInfo> getParticipants() {
        return participants;
    }

    class ParticipantInfo {
        private final List<Short> worldPosition;
        private final List<Short> orientation;
        private final int currentLapDistance;
        private final short racePosition;
        private final short sector;
        private final short highestFlag;
        private final short pitModeSchedule;
        private final int carIndex;
        private final short raceState;
        private final short currentLap;
        private final float currentTime;
        private final float currentSectorTime;
        
        private ParticipantInfo(final ByteBuffer data) {
            this.worldPosition = IntStream.range(0, 3).mapToObj(v -> data.getShort()).collect(Collectors.toList());
            this.orientation = IntStream.range(0, 3).mapToObj(v -> data.getShort()).collect(Collectors.toList());
            this.currentLapDistance = readUnsignedShort(data);
            this.racePosition = readUnsignedByte(data);
            this.sector = readUnsignedByte(data);
            this.highestFlag = readUnsignedByte(data);
            this.pitModeSchedule = readUnsignedByte(data);
            this.carIndex = readUnsignedShort(data);
            this.raceState = readUnsignedByte(data);
            this.currentLap = readUnsignedByte(data);
            this.currentTime = data.getFloat();
            this.currentSectorTime = data.getFloat();
        }

        List<Short> getWorldPosition() {
            return worldPosition;
        }

        List<Short> getOrientation() {
            return orientation;
        }

        int getCurrentLapDistance() {
            return currentLapDistance;
        }

        boolean isActive() {
            final int mask = 128; /* 1000 0000 */
            return (racePosition & mask) == mask;
        }

        int getRacePosition() {
            final int mask = 127; /* 0111 1111 */
            return racePosition & mask;
        }

        short getSector() {
            return sector;
        }

        short getHighestFlag() {
            return highestFlag;
        }

        short getPitModeSchedule() {
            return pitModeSchedule;
        }

        boolean isHuman() {
            final int mask = 32768; /* 1000 0000 0000 0000 */
            return (carIndex & mask) == mask;
        }

        int getCarIndex() {
            final int mask = 32767; /* 0111 1111 1111 1111 */
            return carIndex & mask;
        }

        short getRaceState() {
            return raceState;
        }

        short getCurrentLap() {
            return currentLap;
        }

        float getCurrentTime() {
            return currentTime;
        }

        float getCurrentSectorTime() {
            return currentSectorTime;
        }
    }
}
