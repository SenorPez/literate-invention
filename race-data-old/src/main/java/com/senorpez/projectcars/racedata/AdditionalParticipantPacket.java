package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AdditionalParticipantPacket extends Packet {
    private final static PacketType packetType = PacketType.ADDITIONAL_PARTICIPANT;

    private final Short offset;
    private final List<String> names;

    AdditionalParticipantPacket(final ByteBuffer data) throws InvalidPacketException {
        super(data);
        if (!isCorrectPacketType(packetType)) {
            throw new InvalidPacketException();
        }

        this.offset = readUnsignedByte(data);
        this.names = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj(value -> readString(data))
                        .collect(Collectors.toList()));

        if (data.remaining() > 0) {
            throw new InvalidPacketException();
        }
    }

    @Override
    PacketType getPacketType() {
        return packetType;
    }

    Short getOffset() {
        return offset;
    }

    List<String> getNames() {
        return names;
    }
}
