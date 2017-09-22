package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

class Race {
    private final int id;
    private final Integer laps;
    private final Integer time;
    private final String type;

    private final static AtomicInteger raceId = new AtomicInteger(0);

    @JsonCreator
    Race(
            @JsonProperty("laps") final Integer laps,
            @JsonProperty("time") final Integer time,
            @JsonProperty("type") final String type) {
        this.id = raceId.incrementAndGet();
        this.laps = laps;
        this.time = time;
        this.type = type;
    }

    public Race(final int id, final Integer laps, final Integer time, final String type) {
        this.id = id;
        this.laps = laps;
        this.time = time;
        this.type = type;
    }

    int getId() {
        return id;
    }

    Integer getLaps() {
        return laps;
    }

    Integer getTime() {
        return time;
    }

    String getType() {
        return type;
    }

    static void resetId() {
        raceId.set(0);
    }
}
