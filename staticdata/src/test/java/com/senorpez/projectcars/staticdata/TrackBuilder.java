package com.senorpez.projectcars.staticdata;

import java.util.Arrays;
import java.util.List;

public class TrackBuilder {
    private int id = 0;
    private String name = null;
    private String location = null;
    private String variation = null;
    private float length = 0;
    private List<Float> pitEntry = Arrays.asList(null, null);
    private List<Float> pitExit = Arrays.asList(null, null);
    private int gridSize = 0;

    TrackBuilder() {
    }

    Track build() {
        return new Track(id, name, location, variation, length, pitEntry.get(0), pitEntry.get(1), pitExit.get(0), pitExit.get(1), gridSize);
    }

    Track2 build2() {
        return new Track2(id, name, gridSize);
    }

    public TrackBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public TrackBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    TrackBuilder setLocation(final String location) {
        this.location = location;
        return this;
    }

    TrackBuilder setVariation(final String variation) {
        this.variation = variation;
        return this;
    }

    TrackBuilder setLength(final float length) {
        this.length = length;
        return this;
    }

    TrackBuilder setPitEntry(final List<Float> pitEntry) {
        this.pitEntry = pitEntry;
        return this;
    }

    TrackBuilder setPitExit(final List<Float> pitExit) {
        this.pitExit = pitExit;
        return this;
    }

    TrackBuilder setGridSize(final int gridSize) {
        this.gridSize = gridSize;
        return this;
    }
}
