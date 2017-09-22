package com.senorpez.projectcars.staticdata;

import java.util.List;

public class TrackBuilder {
    private int id = 0;
    private String name = null;
    private String location = null;
    private String variation = null;
    private float length = 0;
    private List<Float> pitEntry = null;
    private List<Float> pitExit = null;
    private int gridSize = 0;

    TrackBuilder() {
    }

    Track build() {
        return new Track(id, name, location, variation, length, pitEntry.get(0), pitEntry.get(1), pitExit.get(0), pitExit.get(1), gridSize);
    }

    public TrackBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public TrackBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    TrackBuilder setLocation() {
        this.location = "Glencairn";
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
