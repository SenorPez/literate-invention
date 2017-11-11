package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum RaceState {
    RACESTATE_INVALID,
    RACESTATE_NOT_STARTED,
    RACESTATE_RACING,
    RACESTATE_FINISHED,
    RACESTATE_DISQUALIFIED,
    RACESTATE_RETIRED,
    RACESTATE_DNF,
    RACESTATE_MAX;

    private static final Map<Integer, RaceState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(RaceState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static RaceState valueOf(final int value) {
        return lookup.get(value);
    }
}
