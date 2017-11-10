package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PacketType {
    PACKET_CAR_PHYSICS,
    PACKET_RACE_DEFINITION,
    PACKET_PARTICIPANTS,
    PACKET_TIMINGS,
    PACKET_GAME_STATE,
    PACKET_WEATHER_STATE,
    PACKET_VEHICLE_NAMES,
    PACKET_TIME_STATS,
    PACKET_PARTICIPANT_VEHICLE_NAMES,
    PACKET_MAX;

    private static final Map<Integer, PacketType> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(PacketType.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static PacketType valueOf(final int value) {
        return lookup.get(value);
    }
}
