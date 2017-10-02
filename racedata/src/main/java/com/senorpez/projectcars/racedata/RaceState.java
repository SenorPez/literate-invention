package com.senorpez.projectcars.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum RaceState {
    RACE_INVALID,
    RACE_NOT_STARTED,
    RACE_RACING,
    RACE_FINISHED,
    RACE_DISQUALIFIED,
    RACE_RETIRED,
    RACE_DNF,
    RACE_MAX;

    private static final Map<Integer, RaceState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(RaceState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, raceState -> raceState)));
    }

    static RaceState valueOf(final int stateValue) {
        return lookup.get(stateValue);
    }
}
