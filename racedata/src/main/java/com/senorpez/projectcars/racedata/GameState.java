package com.senorpez.projectcars.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum GameState {
    GAME_EXITED,
    GAME_FRONT_END,
    GAME_INGAME_PLAYING,
    GAME_INGAME_PAUSED,
    GAME_MAX;

    private static final Map<Integer, GameState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(GameState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, gameState -> gameState)));
    }

    public static GameState valueOf(final int stateValue) {
        return lookup.get(stateValue);
    }
}
