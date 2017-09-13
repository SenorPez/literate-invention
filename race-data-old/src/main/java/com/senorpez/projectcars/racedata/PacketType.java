package com.senorpez.projectcars.racedata;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public enum PacketType {
    TELEMETRY_DATA(1367, TelemetryDataPacket.class),
    PARTICIPANT(1347, ParticipantPacket.class),
    ADDITIONAL_PARTICIPANT(1028, AdditionalParticipantPacket.class);

    private final Short packetLength;
    private final Class<? extends Packet> clazz;

    PacketType(final int packetLength, final Class<? extends Packet> clazz) {
        this.packetLength = (short) packetLength;
        this.clazz = clazz;
    }

    static PacketType valueOf(final Integer packetTypeNumeric) {
        return PacketType.values()[packetTypeNumeric];
    }

    static Packet getPacket(final ByteBuffer data) throws InvalidPacketException {
        final PacketType packetType = Arrays.stream(values())
                .filter(type -> type.packetLength.equals((short) data.remaining()))
                .findAny()
                .orElseThrow(InvalidPacketException::new);
        try {
            return packetType.clazz.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new InvalidPacketException();
        }
    }
}
