package com.senorpez.projectcars;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public enum PacketType {
    TELEMETRY_DATA(1367, TelemetryDataPacket.class),
    PARTICIPANT(1347, ParticipantPacket.class),
    ADDITIONAL_PARTICIPANT(1028, AdditionalParticipantPacket.class);

    private final Short packetLength;
    private final Class<? extends Packet> clazz;

    PacketType(int packetLength, Class<? extends Packet> clazz) {
        this.packetLength = (short) packetLength;
        this.clazz = clazz;
    }

    static PacketType valueOf(Integer packetTypeNumeric) {
        return PacketType.values()[packetTypeNumeric];
    }

    Packet getPacket(DataInputStream telemetryData) {
        try {
            if (telemetryData.available() > 0) {
                return clazz.getDeclaredConstructor(DataInputStream.class).newInstance(telemetryData);

            }
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Short getPacketLength() {
        return packetLength;
    }
}
