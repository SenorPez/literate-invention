package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum FlagColour {
    FLAG_COLOUR_NONE,
    FLAG_COLOUR_GREEN,
    FLAG_COLOUR_BLUE,
    FLAG_COLOUR_WHITE_SLOW_CAR,
    FLAG_COLOUR_WHITE_FINAL_LAP,
    FLAG_COLOUR_RED,
    FLAG_COLOUR_YELLOW,
    FLAG_COLOUR_DOUBLE_YELLOW,
    FLAG_COLOUR_BLACK_AND_WHITE,
    FLAG_COLOUR_BLACK_ORANGE_CIRCLE,
    FLAG_COLOUR_BLACK,
    FLAG_COLOUR_CHEQUERED,
    FLAG_COLOUR_MAX;

    private static final Map<Integer, FlagColour> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(FlagColour.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static FlagColour valueOf(final int value) {
        return lookup.get(value);
    }
}
