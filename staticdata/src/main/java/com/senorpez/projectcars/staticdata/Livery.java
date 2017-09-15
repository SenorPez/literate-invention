package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

class Livery {
    private final int id;
    private final String name;

    Livery(
            @JsonProperty("id") final int id,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }
}
