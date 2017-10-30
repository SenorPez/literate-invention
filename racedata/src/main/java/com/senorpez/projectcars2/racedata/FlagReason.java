package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum FlagReason {
    FLAG_REASON_NONE,
    FLAG_REASON_SOLO_CRASH,
    FLAG_REASON_VEHICLE_CRASH,
    FLAG_REASON_VEHICLE_OBSTRUCTION;

    private static final Map<Integer, FlagReason> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(FlagReason.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static FlagReason valueOf(final int value) {
        return lookup.get(value);
    }
}
