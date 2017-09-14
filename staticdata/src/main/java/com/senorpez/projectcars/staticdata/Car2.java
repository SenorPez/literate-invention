package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

class Car2 {
    private final int id;
    private final String manufacturer;
    private final String model;
    private final int year;
    private final String carClass;

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    @JsonCreator
    Car2(
            @JsonProperty("manufacturer") final String manufacturer,
            @JsonProperty("model") final String model,
            @JsonProperty("year") final int year,
            @JsonProperty("class") final String carClass) {
        this.id = idCounter.getAndIncrement();
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.carClass = carClass;
    }

    int getId() {
        return id;
    }

    String getManufacturer() {
        return manufacturer;
    }

    String getModel() {
        return model;
    }

    int getYear() {
        return year;
    }

    String getCarClass() {
        return carClass;
    }
}