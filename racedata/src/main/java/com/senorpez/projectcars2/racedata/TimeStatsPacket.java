package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class TimeStatsPacket extends Packet {
    private final static int UDP_STREAMER_PARTICIPANTS_SUPPORTED = 32;

    private final long participantsChangedTimestamp;
    private final ParticipantStats stats;
    
    TimeStatsPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);
        
        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_TIME_STATS) {
            throw new InvalidPacketTypeException();
        }

        this.participantsChangedTimestamp = readUnsignedInt(data);
        this.stats = new ParticipantStats(data);

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    long getParticipantsChangedTimestamp() {
        return participantsChangedTimestamp;
    }

    ParticipantStats getStats() {
        return stats;
    }

    class ParticipantStats {
        private final List<ParticipantStatsInfo> participants = new ArrayList<>();
        
        private ParticipantStats(final ByteBuffer data) {
            for (int i = 0; i < UDP_STREAMER_PARTICIPANTS_SUPPORTED; i++) {
                participants.add(new ParticipantStatsInfo(data));
            }
        }

        List<ParticipantStatsInfo> getParticipants() {
            return participants;
        }

        class ParticipantStatsInfo {
            private final float fastestLapTime;
            private final float lastLapTime;
            private final float lastSectorTime;
            private final float fastestSector1Time;
            private final float fastestSector2Time;
            private final float fastestSector3Time;

            private ParticipantStatsInfo(final ByteBuffer data) {
                fastestLapTime = data.getFloat();
                lastLapTime = data.getFloat();
                lastSectorTime = data.getFloat();
                fastestSector1Time = data.getFloat();
                fastestSector2Time = data.getFloat();
                fastestSector3Time = data.getFloat();
            }

            float getFastestLapTime() {
                return fastestLapTime;
            }

            float getLastLapTime() {
                return lastLapTime;
            }

            float getLastSectorTime() {
                return lastSectorTime;
            }

            float getFastestSector1Time() {
                return fastestSector1Time;
            }

            float getFastestSector2Time() {
                return fastestSector2Time;
            }

            float getFastestSector3Time() {
                return fastestSector3Time;
            }
        }
    }
}
