package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

class CarClass {
    private final int id;
    private final String name;

    CarClass(
            @JsonProperty("id") final Integer id,
            @JsonProperty("value") final Integer value,
            @JsonProperty("name") final String name) {
        this.id = id == null ? value : id;
        this.name = name;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }
}
