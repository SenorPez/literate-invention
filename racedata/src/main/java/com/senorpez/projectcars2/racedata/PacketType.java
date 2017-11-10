package com.senorpez.projectcars2.racedata;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum PacketType {
    PACKET_CAR_PHYSICS(CarPhysicsPacket.class, 538),
    PACKET_RACE_DEFINITION(RaceDefinitionPacket.class, 308),
    PACKET_PARTICIPANTS(ParticipantsPacket.class, 1040),
    PACKET_TIMINGS(TimingsPacket.class, 993),
    PACKET_GAME_STATE(GameStatePacket.class, 24),
    PACKET_WEATHER_STATE,
    PACKET_VEHICLE_NAMES,
    PACKET_TIME_STATS(TimeStatsPacket.class, 784),
    PACKET_PARTICIPANT_VEHICLE_NAMES(
            new Object[][]{
                    {ParticipantVehicleNamesPacket.class, 1164},
                    {VehicleClassNamesPacket.class, 1452}
            }
    ),
    PACKET_MAX;

    private static final Map<Integer, PacketType> lookup = new HashMap<>();
    private final Map<Integer, Class<? extends Packet>> builderLookup = new HashMap<>();

    PacketType() { }

    PacketType(final Class<? extends Packet> clazz, final int packetSize) {
        builderLookup.put(packetSize, clazz);
    }

    PacketType(final Object[][] objects) {
        for (final Object[] obj : objects) {
            builderLookup.put((int) obj[1], (Class<? extends Packet>) obj[0]);
        }
    }

    Map<Integer, Class<? extends Packet>> getBuilderLookup() {
        return builderLookup;
    }

    int getPacketLength() {
        return (int) builderLookup.keySet().toArray()[0];
    }

    Integer getPacketLength(final Class<? extends Packet> clazz) {
        final Map.Entry entry = builderLookup
                .entrySet()
                .stream()
                .filter(val -> val.getValue() == clazz)
                .findFirst()
                .orElse(null);
        return entry == null ? null : (int) entry.getKey();
    }

    static {
        lookup.putAll(EnumSet.allOf(PacketType.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static PacketType valueOf(final int value) {
        return lookup.get(value);
    }

    static Packet getPacket(final ByteBuffer data) throws InvalidPacketDataException {
        final PacketType packetType = Arrays.stream(values())
                .filter(type -> type.getBuilderLookup().containsKey(data.remaining()))
                .findFirst()
                .orElseThrow(InvalidPacketDataException::new);
        try {
            final Class<? extends Packet> clazz = packetType.getBuilderLookup().get(data.remaining());
            return clazz.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new InvalidPacketDataException();
        }
    }
}
