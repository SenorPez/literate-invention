package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Livery {
    private final int id;
    private final String name;

    public Livery(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
