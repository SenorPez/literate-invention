package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ParticipantsPacketBuilder extends PacketBuilder {
    private final static int PARTICIPANT_NAME_LENGTH_MAX = 64;
    private final static int PARTICIPANTS_PER_PACKET = 16;

    private long expectedParticipantsChangedTimestamp = getBoundedLong();
    private List<String> expectedNames = IntStream.range(0, PARTICIPANTS_PER_PACKET).mapToObj(v -> generateString(PARTICIPANT_NAME_LENGTH_MAX)).collect(Collectors.toList());

    ParticipantsPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_PARTICIPANTS.ordinal());
    }

    long getExpectedParticipantsChangedTimestamp() {
        return expectedParticipantsChangedTimestamp;
    }

    ParticipantsPacketBuilder setExpectedParticipantsChangedTimestamp(final long expectedParticipantsChangedTimestamp) {
        this.expectedParticipantsChangedTimestamp = expectedParticipantsChangedTimestamp;
        return this;
    }

    List<String> getExpectedNames() {
        return expectedNames;
    }

    ParticipantsPacketBuilder setExpectedNames(final List<String> expectedNames) {
        this.expectedNames = expectedNames;
        return this;
    }

    @Override
    ByteBuffer build() {
        final int packetSize = PacketType.PACKET_PARTICIPANTS.getPacketLength();
        final ByteBuffer data = build(ByteBuffer.allocate(packetSize));

        writeUnsignedInt(expectedParticipantsChangedTimestamp, data);
        expectedNames.forEach(name -> writeString(name, PARTICIPANT_NAME_LENGTH_MAX, data));

        data.rewind();
        return data;
    }
}
