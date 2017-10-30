package com.senorpez.projectcars2.racedata;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum GameState {
    GAME_EXITED,
    GAME_FRONT_END,
    GAME_INGAME_PLAYING,
    GAME_INGAME_PAUSED,
    GAME_INGAME_INMENU_TIME_TICKING,
    GAME_INGAME_RESTARTING,
    GAME_INGAME_REPLY,
    GAME_FONRT_END_REPLAY,
    GAME_MAX;

    private static final Map<Integer, GameState> lookup = new HashMap<>();

    static {
        lookup.putAll(EnumSet.allOf(GameState.class)
                .stream()
                .collect(Collectors.toMap(Enum::ordinal, Function.identity())));
    }

    static GameState valueOf(final int value) {
        return lookup.get(value);
    }
}
