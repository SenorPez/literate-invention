package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Track2 {
    private final int id;
    private final String name;
    private final int gridSize;

    Track2(
            @JsonProperty("id") final int id,
            @JsonProperty("name") final String name,
            @JsonProperty("gridsize") final int gridSize) {
        this.id = id;
        this.name = name;
        this.gridSize = gridSize;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGridSize() {
        return gridSize;
    }
}
