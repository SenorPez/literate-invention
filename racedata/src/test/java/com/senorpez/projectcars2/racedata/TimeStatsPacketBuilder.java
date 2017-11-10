package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.stream.IntStream;

class TimeStatsPacketBuilder extends PacketBuilder {
    private final static int UDP_STREAMER_PARTICIPANTS_SUPPORTED = 32;

    private long expectedParticipantsChangedTimestamp;

    private ParticipantStatsBuilder participantStatsBuilder = new ParticipantStatsBuilder();

    TimeStatsPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_TIME_STATS.ordinal());
    }

    long getExpectedParticipantsChangedTimestamp() {
        return expectedParticipantsChangedTimestamp;
    }

    TimeStatsPacketBuilder setExpectedParticipantsChangedTimestamp(final long expectedParticipantsChangedTimestamp) {
        this.expectedParticipantsChangedTimestamp = expectedParticipantsChangedTimestamp;
        return this;
    }

    ParticipantStatsBuilder getParticipantStatsBuilder() {
        return participantStatsBuilder;
    }

    TimeStatsPacketBuilder setParticipantStatsBuilder(final ParticipantStatsBuilder participantStatsBuilder) {
        this.participantStatsBuilder = participantStatsBuilder;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(784));

        writeUnsignedInt(expectedParticipantsChangedTimestamp, data);

        IntStream
                .range(0, UDP_STREAMER_PARTICIPANTS_SUPPORTED)
                .forEach(v -> data.put(participantStatsBuilder.getParticipantStatsInfoBuilder().build()));

        data.rewind();
        return data;
    }

    static class ParticipantStatsBuilder extends PacketBuilder {
        private final static Random random = new Random();

        private ParticipantStatsInfoBuilder participantStatsInfoBuilder = new ParticipantStatsInfoBuilder();

        ParticipantStatsInfoBuilder getParticipantStatsInfoBuilder() {
            return participantStatsInfoBuilder;
        }

        ParticipantStatsBuilder setParticipantStatsInfoBuilder(final ParticipantStatsInfoBuilder participantStatsInfoBuilder) {
            this.participantStatsInfoBuilder = participantStatsInfoBuilder;
            return this;
        }
    }

    static class ParticipantStatsInfoBuilder extends PacketBuilder {
        private final static Random random = new Random();

        private float expectedFastestLapTime = random.nextFloat();
        private float expectedLastLapTime = random.nextFloat();
        private float expectedLastSectorTime = random.nextFloat();
        private float expectedFastestSector1Time = random.nextFloat();
        private float expectedFastestSector2Time = random.nextFloat();
        private float expectedFastestSector3Time = random.nextFloat();

        float getExpectedFastestLapTime() {
            return expectedFastestLapTime;
        }

        ParticipantStatsInfoBuilder setExpectedFastestLapTime(final float expectedFastestLapTime) {
            this.expectedFastestLapTime = expectedFastestLapTime;
            return this;
        }

        float getExpectedLastLapTime() {
            return expectedLastLapTime;
        }

        ParticipantStatsInfoBuilder setExpectedLastLapTime(final float expectedLastLapTime) {
            this.expectedLastLapTime = expectedLastLapTime;
            return this;
        }

        float getExpectedLastSectorTime() {
            return expectedLastSectorTime;
        }

        ParticipantStatsInfoBuilder setExpectedLastSectorTime(final float expectedLastSectorTime) {
            this.expectedLastSectorTime = expectedLastSectorTime;
            return this;
        }

        float getExpectedFastestSector1Time() {
            return expectedFastestSector1Time;
        }

        ParticipantStatsInfoBuilder setExpectedFastestSector1Time(final float expectedFastestSector1Time) {
            this.expectedFastestSector1Time = expectedFastestSector1Time;
            return this;
        }

        float getExpectedFastestSector2Time() {
            return expectedFastestSector2Time;
        }

        ParticipantStatsInfoBuilder setExpectedFastestSector2Time(final float expectedFastestSector2Time) {
            this.expectedFastestSector2Time = expectedFastestSector2Time;
            return this;
        }

        float getExpectedFastestSector3Time() {
            return expectedFastestSector3Time;
        }

        ParticipantStatsInfoBuilder setExpectedFastestSector3Time(final float expectedFastestSector3Time) {
            this.expectedFastestSector3Time = expectedFastestSector3Time;
            return this;
        }

        @Override
        ByteBuffer build() {
            final ByteBuffer data = ByteBuffer.allocate(24);

            data.putFloat(expectedFastestLapTime);
            data.putFloat(expectedLastLapTime);
            data.putFloat(expectedLastSectorTime);
            data.putFloat(expectedFastestSector1Time);
            data.putFloat(expectedFastestSector2Time);
            data.putFloat(expectedFastestSector3Time);

            data.rewind();
            return data;
        }
    }
}
