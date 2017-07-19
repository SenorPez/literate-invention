package com.senorpez.projectcars.racedata;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AdditionalParticipantPacket extends Packet {
    private final static PacketType packetType = PacketType.ADDITIONAL_PARTICIPANT;

    private final Short offset;
    private final List<String> names;

    AdditionalParticipantPacket(final DataInputStream data) throws IOException, InvalidPacketException {
        super(data);
        if (!isCorrectPacketType(packetType)) {
            throw new InvalidPacketException();
        }

        this.offset = (short) data.readUnsignedByte();

        final byte[] nameBuffer = new byte[64];
        this.names = Collections.unmodifiableList(
                IntStream.range(0, 16)
                        .mapToObj(value -> {
                            try {
                                data.readFully(nameBuffer);
                            } catch (IOException e) {
                                return "";
                            }
                            return new String(nameBuffer, UTF_8).split("\u0000", 2)[0];
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public PacketType getPacketType() {
        return packetType;
    }

    public Short getOffset() {
        return offset;
    }

    public List<String> getNames() {
        return names;
    }
}
