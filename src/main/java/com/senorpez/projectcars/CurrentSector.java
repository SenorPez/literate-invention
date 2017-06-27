package com.senorpez.projectcars;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum CurrentSector {
    SECTOR_INVALID,
    SECTOR_START,
    SECTOR_SECTOR1,
    SECTOR_SECTOR2,
    SECTOR_FINISH,
    SECTOR_STOP,
    SECTOR_MAX;

    private static final Map<Integer, CurrentSector> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(CurrentSector.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, currentSector -> currentSector)));
    }

    static CurrentSector valueOf(int stateValue) {
        return lookup.get(stateValue);
    }
}
