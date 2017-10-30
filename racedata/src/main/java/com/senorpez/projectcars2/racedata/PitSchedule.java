package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum PitSchedule {
    PIT_SCHEDULE_NONE,
    PIT_SCHEDULE_PLAYER_REQUESTED,
    PIT_SCHEDULE_ENGINEER_REQUESTED,
    PIT_SCHEDULE_DAMAGE_REQUESTED,
    PIT_SCHEDULE_MANDATORY,
    PIT_SCHEDULE_DRIVE_THROUGH,
    PIT_SCHEDULE_STOP_GO,
    PIT_SCHEDULE_PITSPOT_OCCUPIED,
    PIT_SCHEDULE_MAX;

    private static final Map<Integer, PitSchedule> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(PitSchedule.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static PitSchedule valueOf(final int value) {
        return lookup.get(value);
    }
}
