package com.senorpez.projectcars.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PitSchedule {
    PIT_SCHEDULE_NONE,
    PIT_SCHEDULE_STANDARD,
    PIT_SCHEDULE_DRIVE_THROUGH,
    PIT_SCHEDULE_STOP_GO,
    PIT_SCHEDULE_MAX;

    private static final Map<Integer, PitSchedule> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(PitSchedule.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static PitSchedule valueOf(int stateValue) {
        return lookup.get(stateValue);
    }
}
