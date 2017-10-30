package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum Terrain {
    TERRAIN_ROAD,
    TERRAIN_LOW_GRIP_ROAD,
    TERRAIN_BUMPY_ROAD1,
    TERRAIN_BUMPY_ROAD2,
    TERRAIN_BUMPY_ROAD3,
    TERRAIN_MARBLES,
    TERRAIN_GRASSY_BERMS,
    TERRAIN_GRASS,
    TERRAIN_GRAVEL,
    TERRAIN_BUMPY_GRAVEL,
    TERRAIN_RUMBLE_STRIPS,
    TERRAIN_DRAINS,
    TERRAIN_TYREWALLS,
    TERRAIN_CEMENTWALLS,
    TERRAIN_GUARDRAILS,
    TERRAIN_SAND,
    TERRAIN_BUMPY_SAND,
    TERRAIN_DIRT,
    TERRAIN_BUMPY_DIRT,
    TERRAIN_DIRT_ROAD,
    TERRAIN_BUMPY_DIRT_ROAD,
    TERRAIN_PAVEMENT,
    TERRAIN_DIRT_BANK,
    TERRAIN_WOOD,
    TERRAIN_DRY_VERGE,
    TERRAIN_EXIT_RUMBLE_STRIPS,
    TERRAIN_GRASSCRETE,
    TERRAIN_LONG_GRASS,
    TERRAIN_SLOPE_GRASS,
    TERRAIN_COBBLES,
    TERRAIN_SAND_ROAD,
    TERRAIN_BAKED_CLAY,
    TERRAIN_ASTROTURF,
    TERRAIN_SNOWHALF,
    TERRAIN_SNOWFULL,
    TERRAIN_DAMAGED_ROAD1,
    TERRAIN_TRAIN_TRACK_ROAD,
    TERRAIN_BUMPYCOBBLES,
    TERRAIN_ARIES_ONLY,
    TERRAIN_ORION_ONLY,
    TERRAIN_B1RUMBLES,
    TERRAIN_B2RUMBLES,
    TERRAIN_ROUGH_SAND_MEDIUM,
    TERRAIN_ROUGH_SAND_HEAVY,
    TERRAIN_SNOWWALLS,
    TERRAIN_ICE_ROAD,
    TERRAIN_RUNOFF_ROAD,
    TERRAIN_ILLEGAL_STRIP,
    TERRAIN_MAX;

    private static final Map<Integer, Terrain> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(Terrain.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static Terrain valueOf(final int value) {
        return lookup.get(value);
    }

}
