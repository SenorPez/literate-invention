package com.senorpez.projectcars.staticdata;

public class RaceBuilder {
    private int id = 0;
    private Integer laps = null;
    private Integer time = null;
    private String type = null;

    RaceBuilder() {
    }

    Race build() {
        return new Race(id, laps, time, type);
    }

    public RaceBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    RaceBuilder setLaps(final Integer laps) {
        this.laps = laps;
        return this;
    }

    RaceBuilder setTime(final Integer time) {
        this.time = time;
        return this;
    }

    public RaceBuilder setType(final String type) {
        this.type = type;
        return this;
    }
}
