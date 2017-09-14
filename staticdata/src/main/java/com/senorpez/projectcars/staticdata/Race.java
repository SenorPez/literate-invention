package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class Race {
    private final int id;
    private final int laps;
    private final int time;
    private final String type;

    private final static AtomicInteger raceId = new AtomicInteger(0);

    public Race(
            @JsonProperty("laps") int laps,
            @JsonProperty("time") int time,
            @JsonProperty("type") String type) {
        this.id = raceId.incrementAndGet();
        this.laps = laps;
        this.time = time;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getLaps() {
        return laps;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    static void resetId() {
        raceId.set(0);
    }
}
