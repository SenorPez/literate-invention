package com.senorpez.projectcars.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FlagReason {
    FLAG_REASON_NONE,
    FLAG_REASON_SOLO_CRASH,
    FLAG_REASON_VEHICLE_CRASH,
    FLAG_REASON_VEHICLE_OBSTRUCTION,
    FLAG_REASON_MAX;

    private static final Map<Integer, FlagReason> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(FlagReason.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static FlagReason valueOf(int stateValue) {
        return lookup.get(stateValue);
    }
}
