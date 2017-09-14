package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class Car2 {
    private final int id;
    private final String manufacturer;
    private final String model;
    private final int year;
    private final String carClass;

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public Car2(
            @JsonProperty("manufacturer") String manufacturer,
            @JsonProperty("model") String model,
            @JsonProperty("year") int year,
            @JsonProperty("class") String carClass) {
        this.id = idCounter.getAndIncrement();
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.carClass = carClass;
    }

    public int getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getCarClass() {
        return carClass;
    }
}
