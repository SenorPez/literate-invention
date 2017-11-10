package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TimingsPacketBuilder extends PacketBuilder {
    private final static Random random = new Random();
    private final static int UDP_STREAMER_PARTICIPANTS_SUPPORTED = 32;

    private byte expectedNumParticipants = (byte) random.nextInt(Byte.MAX_VALUE);
    private long expectedParticipantsChangedTimestamp = getBoundedLong();
    private float expectedEventTimeRemaining = random.nextFloat();
    private float expectedSplitTimeAhead = random.nextFloat();
    private float expectedSplitTimeBehind = random.nextFloat();
    private float expectedSplitTime = random.nextFloat();

    private ParticipantInfoBuilder participantInfoBuilder = new ParticipantInfoBuilder();

    TimingsPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_TIMINGS.ordinal());
    }

    byte getExpectedNumParticipants() {
        return expectedNumParticipants;
    }

    TimingsPacketBuilder setExpectedNumParticipants(final byte expectedNumParticipants) {
        this.expectedNumParticipants = expectedNumParticipants;
        return this;
    }

    long getExpectedParticipantsChangedTimestamp() {
        return expectedParticipantsChangedTimestamp;
    }

    TimingsPacketBuilder setExpectedParticipantsChangedTimestamp(final long expectedParticipantsChangedTimestamp) {
        this.expectedParticipantsChangedTimestamp = expectedParticipantsChangedTimestamp;
        return this;
    }

    float getExpectedEventTimeRemaining() {
        return expectedEventTimeRemaining;
    }

    TimingsPacketBuilder setExpectedEventTimeRemaining(final float expectedEventTimeRemaining) {
        this.expectedEventTimeRemaining = expectedEventTimeRemaining;
        return this;
    }

    float getExpectedSplitTimeAhead() {
        return expectedSplitTimeAhead;
    }

    TimingsPacketBuilder setExpectedSplitTimeAhead(final float expectedSplitTimeAhead) {
        this.expectedSplitTimeAhead = expectedSplitTimeAhead;
        return this;
    }

    float getExpectedSplitTimeBehind() {
        return expectedSplitTimeBehind;
    }

    TimingsPacketBuilder setExpectedSplitTimeBehind(final float expectedSplitTimeBehind) {
        this.expectedSplitTimeBehind = expectedSplitTimeBehind;
        return this;
    }

    float getExpectedSplitTime() {
        return expectedSplitTime;
    }

    TimingsPacketBuilder setExpectedSplitTime(final float expectedSplitTime) {
        this.expectedSplitTime = expectedSplitTime;
        return this;
    }

    ParticipantInfoBuilder getParticipantInfoBuilder() {
        return participantInfoBuilder;
    }

    TimingsPacketBuilder setParticipantInfoBuilder(final ParticipantInfoBuilder participantInfoBuilder) {
        this.participantInfoBuilder = participantInfoBuilder;
        return this;
    }

    @Override
    ByteBuffer build() {
        final int packetSize = PacketType.PACKET_TIMINGS.getPacketLength();
        final ByteBuffer data = build(ByteBuffer.allocate(packetSize));

        data.put(expectedNumParticipants);
        writeUnsignedInt(expectedParticipantsChangedTimestamp, data);
        data.putFloat(expectedEventTimeRemaining);
        data.putFloat(expectedSplitTimeAhead);
        data.putFloat(expectedSplitTimeBehind);
        data.putFloat(expectedSplitTime);
        
        IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .forEach(v -> data.put(participantInfoBuilder.build()));

        data.rewind();
        return data;
    }

    static class ParticipantInfoBuilder extends PacketBuilder {
        private final static Random random = new Random();

        private List<Short> expectedWorldPosition = IntStream.range(0, 3).mapToObj(v -> (short) random.nextInt(Short.MAX_VALUE)).collect(Collectors.toList());
        private List<Short> expectedOrientation = IntStream.range(0, 3).mapToObj(v -> (short) random.nextInt(Short.MAX_VALUE)).collect(Collectors.toList());
        private int expectedCurrentLapDistance = random.nextInt(MAX_UNSIGNED_SHORT);
        private short expectedRacePosition = (short) random.nextInt(MAX_UNSIGNED_BYTE);
        private short expectedSector = (short) random.nextInt(MAX_UNSIGNED_BYTE);
        private short expectedHighestFlag = (short) random.nextInt(MAX_UNSIGNED_BYTE);
        private short expectedPitModeSchedule = (short) random.nextInt(MAX_UNSIGNED_BYTE);
        private int expectedCarIndex = random.nextInt(MAX_UNSIGNED_SHORT);
        private short expectedRaceState = (short) random.nextInt(RaceState.RACESTATE_MAX.ordinal());
        private short expectedCurrentLap = (short) random.nextInt(MAX_UNSIGNED_BYTE);
        private float expectedCurrentTime = random.nextFloat();
        private float expectedCurrentSectorTime = random.nextFloat();

        List<Float> getExpectedWorldPosition() {
            final int xMask = 96; /* 0110 0000 */
            final float xPrec = (expectedSector & xMask) / 4.0F;

            final int zMask = 24; /* 0001 1000 */
            final float zPrec = (expectedSector & zMask) / 4.0F;

            return Arrays.asList(
                    expectedWorldPosition.get(0) + xPrec,
                    (float) expectedWorldPosition.get(1),
                    expectedWorldPosition.get(2) + zPrec
            );
        }

        void setExpectedWorldPosition(final List<Short> expectedWorldPosition) {
            this.expectedWorldPosition = expectedWorldPosition;
        }

        List<Short> getExpectedOrientation() {
            return expectedOrientation;
        }

        void setExpectedOrientation(final List<Short> expectedOrientation) {
            this.expectedOrientation = expectedOrientation;
        }

        int getExpectedCurrentLapDistance() {
            return expectedCurrentLapDistance;
        }

        void setExpectedCurrentLapDistance(final int expectedCurrentLapDistance) {
            this.expectedCurrentLapDistance = expectedCurrentLapDistance;
        }

        short getExpectedRacePosition() {
            return expectedRacePosition;
        }

        void setExpectedRacePosition(final short expectedRacePosition) {
            this.expectedRacePosition = expectedRacePosition;
        }

        short getExpectedSector() {
            return expectedSector;
        }

        void setExpectedSector(final short expectedSector) {
            this.expectedSector = expectedSector;
        }

        short getExpectedHighestFlag() {
            return expectedHighestFlag;
        }

        void setExpectedHighestFlag(final short expectedHighestFlag) {
            this.expectedHighestFlag = expectedHighestFlag;
        }

        short getExpectedPitModeSchedule() {
            return expectedPitModeSchedule;
        }

        void setExpectedPitModeSchedule(final short expectedPitModeSchedule) {
            this.expectedPitModeSchedule = expectedPitModeSchedule;
        }

        int getExpectedCarIndex() {
            return expectedCarIndex;
        }

        void setExpectedCarIndex(final int expectedCarIndex) {
            this.expectedCarIndex = expectedCarIndex;
        }

        short getExpectedRaceState() {
            return expectedRaceState;
        }

        void setExpectedRaceState(final short expectedRaceState) {
            this.expectedRaceState = expectedRaceState;
        }

        short getExpectedCurrentLap() {
            return expectedCurrentLap;
        }

        void setExpectedCurrentLap(final short expectedCurrentLap) {
            this.expectedCurrentLap = expectedCurrentLap;
        }

        float getExpectedCurrentTime() {
            return expectedCurrentTime;
        }

        void setExpectedCurrentTime(final float expectedCurrentTime) {
            this.expectedCurrentTime = expectedCurrentTime;
        }

        float getExpectedCurrentSectorTime() {
            return expectedCurrentSectorTime;
        }

        void setExpectedCurrentSectorTime(final float expectedCurrentSectorTime) {
            this.expectedCurrentSectorTime = expectedCurrentSectorTime;
        }

        @Override
        ByteBuffer build() {
            final ByteBuffer data = ByteBuffer.allocate(30);

            expectedWorldPosition.forEach(data::putShort);
            expectedOrientation.forEach(data::putShort);
            writeUnsignedShort(expectedCurrentLapDistance, data);
            writeUnsignedByte(expectedRacePosition, data);
            writeUnsignedByte(expectedSector, data);
            writeUnsignedByte(expectedHighestFlag, data);
            writeUnsignedByte(expectedPitModeSchedule, data);
            writeUnsignedShort(expectedCarIndex, data);
            writeUnsignedByte(expectedRaceState, data);
            writeUnsignedByte(expectedCurrentLap, data);
            data.putFloat(expectedCurrentTime);
            data.putFloat(expectedCurrentSectorTime);

            data.rewind();
            return data;
        }
    }
}
