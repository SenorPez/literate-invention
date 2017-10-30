package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

class CarClass2 {
    private final int id;
    private final String name;
    private final String abbreviation;

    CarClass2(
            @JsonProperty("value") final Integer id,
            @JsonProperty("name") final String name,
            @JsonProperty("abbreviation") final String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    String getAbbreviation() {
        return abbreviation;
    }
}
