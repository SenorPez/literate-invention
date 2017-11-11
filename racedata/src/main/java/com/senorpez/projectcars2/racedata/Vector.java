package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum Vector {
    VEC_X,
    VEC_Y,
    VEC_Z;

    private static final Map<Integer, Vector> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(Vector.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static Vector valueOf(final int value) {
        return lookup.get(value);
    }
}
