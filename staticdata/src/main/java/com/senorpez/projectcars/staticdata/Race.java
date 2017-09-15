package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

class Race {
    private final int id;
    private final int laps;
    private final int time;
    private final String type;

    private final static AtomicInteger raceId = new AtomicInteger(0);

    Race(
            @JsonProperty("laps") final int laps,
            @JsonProperty("time") final int time,
            @JsonProperty("type") final String type) {
        this.id = raceId.incrementAndGet();
        this.laps = laps;
        this.time = time;
        this.type = type;
    }

    int getId() {
        return id;
    }

    int getLaps() {
        return laps;
    }

    int getTime() {
        return time;
    }

    String getType() {
        return type;
    }

    static void resetId() {
        raceId.set(0);
    }
}
