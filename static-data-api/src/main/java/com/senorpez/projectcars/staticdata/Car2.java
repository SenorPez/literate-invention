package com.senorpez.projectcars.staticdata;

class Car2 {
    private final String manufacturer;
    private final String model;
    private final int year;
    private final String carClass;

    Car2(final String manufacturer, final String model, final int year, final String carClass) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.carClass = carClass;
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