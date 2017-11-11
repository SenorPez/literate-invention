package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum CrashDamageState {
    CRASH_DAMAGE_NONE,
    CRASH_DAMAGE_OFFTRACK,
    CRASH_DAMAGE_LARGE_PROP,
    CRASH_DAMAGE_SPINNING,
    CRASH_DAMAGE_ROLLING,
    CRASH_DAMAGE_MAX;

    private static final Map<Integer, CrashDamageState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(CrashDamageState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static CrashDamageState valueOf(final int value) {
        return lookup.get(value);
    }
}
