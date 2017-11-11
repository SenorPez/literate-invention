package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum SessionState {
    SESSION_INVALID,
    SESSION_PRACTICE,
    SESSION_TEST,
    SESSION_QUALIFY,
    SESSION_FORMATION_LAP,
    SESSION_RACE,
    SESSION_TIME_ATTACK,
    SESSION_MAX;

    private static final Map<Integer, SessionState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(SessionState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static SessionState valueOf(final int value) {
        return lookup.get(value);
    }
}
