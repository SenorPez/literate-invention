package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Track {
    private final int id;
    private final String name;
    private final String location;
    private final String variation;
    private final float length;
    private final List<Float> pitEntry;
    private final List<Float> pitExit;
    private final int gridSize;

    Track(
            @JsonProperty("id") final int id,
            @JsonProperty("name") final String name,
            @JsonProperty("location") final String location,
            @JsonProperty("variation") final String variation,
            @JsonProperty("length") final float length,
            @JsonProperty("pitEntryX") final Float pitEntryX,
            @JsonProperty("pitEntryZ") final Float pitEntryZ,
            @JsonProperty("pitExitX") final Float pitExitX,
            @JsonProperty("pitExitZ") final Float pitExitZ,
            @JsonProperty("gridSize") final int gridSize) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.variation = variation;
        this.length = length;

        this.pitEntry = pitEntryX == null || pitEntryZ == null
                ? null
                : Collections.unmodifiableList(Arrays.asList(pitEntryX, pitEntryZ));

        this.pitExit = pitExitX == null || pitExitZ == null
                ? null
                : Collections.unmodifiableList(Arrays.asList(pitExitX, pitExitZ));

       this.gridSize = gridSize;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getLocation() {
        return location;
    }

    String getVariation() {
        return variation;
    }

    float getLength() {
        return length;
    }

    List<Float> getPitEntry() {
        return pitEntry;
    }

    List<Float> getPitExit() {
        return pitExit;
    }

    int getGridSize() {
        return gridSize;
    }
}
