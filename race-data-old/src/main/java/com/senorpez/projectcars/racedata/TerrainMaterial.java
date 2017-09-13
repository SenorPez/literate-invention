package com.senorpez.projectcars.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TerrainMaterial {
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
    TERRAIN_MAX;

    private static final Map<Integer, TerrainMaterial> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(TerrainMaterial.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static TerrainMaterial valueOf(int stateValue) {
        return lookup.get(stateValue);
    }
}
