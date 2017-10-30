package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum Tyre {
    TYRE_FRONT_LEFT,
    TYRE_FRONT_RIGHT,
    TYRE_REAR_LEFT,
    TYRE_REAR_RIGHT,
    TYRE_MAX;

    private static final Map<Integer, Tyre> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(Tyre.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static Tyre valueOf(final int value) {
        return lookup.get(value);
    }
}
