package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ParticipantsPacket extends Packet {
    private final static int PARTICIPANT_NAME_LENGTH_MAX = 64;
    private final static int PARTICIPANTS_PER_PACKET = 16;

    private final long participantsChangedTimestamp;
    private final List<String> names;

    ParticipantsPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_PARTICIPANTS) {
            throw new InvalidPacketTypeException();
        }

        this.participantsChangedTimestamp = readUnsignedInt(data);
        this.names = Collections.unmodifiableList(IntStream.range(0, PARTICIPANTS_PER_PACKET).mapToObj(v -> readString(data, PARTICIPANT_NAME_LENGTH_MAX)).collect(Collectors.toList()));

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    long getParticipantsChangedTimestamp() {
        return participantsChangedTimestamp;
    }

    List<String> getNames() {
        return names;
    }
}
