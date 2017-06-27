package com.senorpez.projectcars;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PitMode {
    PIT_MODE_NONE,
    PIT_MODE_DRIVING_INTO_PITS,
    PIT_MODE_IN_PIT,
    PIT_MODE_DRIVING_OUT_OF_PITS,
    PIT_MODE_IN_GARAGE,
    PIT_MODE_MAX;

    private static final Map<Integer, PitMode> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(PitMode.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static PitMode valueOf(int stateValue) {
        return lookup.get(stateValue);
    }
}
