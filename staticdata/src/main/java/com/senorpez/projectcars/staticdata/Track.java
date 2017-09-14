package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Track {
    private final int id;
    private final String name;
    private final String location;
    private final String variation;
    private final float length;
    private final List<Float> pitEntry;
    private final List<Float> pitExit;
    private final int gridSize;

    public Track(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("location") String location,
            @JsonProperty("variation") String variation,
            @JsonProperty("length") float length,
            @JsonProperty("pitEntryX") Float pitEntryX,
            @JsonProperty("pitEntryZ") Float pitEntryZ,
            @JsonProperty("pitExitX") Float pitExitX,
            @JsonProperty("pitExitZ") Float pitExitZ,
            @JsonProperty("gridSize") int gridSize) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getVariation() {
        return variation;
    }

    public float getLength() {
        return length;
    }

    public List<Float> getPitEntry() {
        return pitEntry;
    }

    public List<Float> getPitExit() {
        return pitExit;
    }

    public int getGridSize() {
        return gridSize;
    }
}
