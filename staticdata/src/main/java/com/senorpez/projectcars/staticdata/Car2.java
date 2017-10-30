package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

class Car2 implements CommonCar {
    private final int id;
    private final String name;
    private final CarClass2 carClass;
    private final String manufacturer;
    private final String country;
    private final int year;
    private final String drivetrain;
    private final int topSpeed;
    private final float acceleration;
    private final int power;
    private final int weight;
    private final int gears;
    private final String transmission;
    private final String engineType;
    private final boolean antilockBrakeSystem;
    private final boolean tractionControl;
    private final boolean stabilityControl;
    private final int controlDifficulty;
    private final int corneringSpeed;
    private final String dlc;

    private final Set<Livery> liveries;

    public Car2(
            @JsonProperty("id") final int id,
            @JsonProperty("name") final String name,
            @JsonProperty("class") final String carClass,
            @JsonProperty("manufacturer") final String manufacturer,
            @JsonProperty("country") final String country,
            @JsonProperty("year") final int year,
            @JsonProperty("drivetrain") final String drivetrain,
            @JsonProperty("topSpeed") final int topSpeed,
            @JsonProperty("acceleration") final float acceleration,
            @JsonProperty("power") final int power,
            @JsonProperty("weight") final int weight,
            @JsonProperty("gears") final int gears,
            @JsonProperty("transmission") final String transmission,
            @JsonProperty("engineType") final String engineType,
            @JsonProperty("antilockBrakeSystem") final boolean antilockBrakeSystem,
            @JsonProperty("tractionControl") final boolean tractionControl,
            @JsonProperty("stabilityControl") final boolean stabilityControl,
            @JsonProperty("controlDifficulty") final int controlDifficulty,
            @JsonProperty("corneringSpeed") final int corneringSpeed,
            @JsonProperty("dlc") final String dlc) {
        this.id = id;
        this.name = name;
        this.carClass = Application.CAR_CLASSES2.stream().filter(cclass -> cclass.getName().equalsIgnoreCase(carClass)).findAny().orElseThrow(RuntimeException::new);
        this.manufacturer = manufacturer;
        this.country = country;
        this.year = year;
        this.drivetrain = drivetrain;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.power = power;
        this.weight = weight;
        this.gears = gears;
        this.transmission = transmission;
        this.engineType = engineType;
        this.antilockBrakeSystem = antilockBrakeSystem;
        this.tractionControl = tractionControl;
        this.stabilityControl = stabilityControl;
        this.controlDifficulty = controlDifficulty;
        this.corneringSpeed = corneringSpeed;
        this.dlc = dlc;

        final JsonNode carLiveryNode = Application.LIVERY_NODES.parallelStream()
                .filter(jsonNode -> jsonNode.get("id").intValue() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        this.liveries = Application.getData(Livery.class, carLiveryNode.get("liveries"));
    }

    Car2(
            final int id,
            final String name,
            final CarClass2 carClass,
            final String manufacturer,
            final String country,
            final int year,
            final String drivetrain,
            final int topSpeed,
            final float acceleration,
            final int power,
            final int weight,
            final int gears,
            final String transmission,
            final String engineType,
            final boolean antilockBrakeSystem,
            final boolean tractionControl,
            final boolean stabilityControl,
            final int controlDifficulty,
            final int corneringSpeed,
            final String dlc,
            final Set<Livery> liveries) {
        this.id = id;
        this.name = name;
        this.carClass = carClass;
        this.manufacturer = manufacturer;
        this.country = country;
        this.year = year;
        this.drivetrain = drivetrain;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.power = power;
        this.weight = weight;
        this.gears = gears;
        this.transmission = transmission;
        this.engineType = engineType;
        this.antilockBrakeSystem = antilockBrakeSystem;
        this.tractionControl = tractionControl;
        this.stabilityControl = stabilityControl;
        this.controlDifficulty = controlDifficulty;
        this.corneringSpeed = corneringSpeed;
        this.dlc = dlc;

        this.liveries = liveries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CarClass2 getCarClass() {
        return carClass;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    int getPower() {
        return power;
    }

    public int getWeight() {
        return weight;
    }

    public int getGears() {
        return gears;
    }

    String getTransmission() {
        return transmission;
    }

    public String getEngineType() {
        return engineType;
    }

    boolean hasAntilockBrakeSystem() {
        return antilockBrakeSystem;
    }

    boolean hasTractionControl() {
        return tractionControl;
    }

    boolean hasStabilityControl() {
        return stabilityControl;
    }

    int getControlDifficulty() {
        return controlDifficulty;
    }

    int getCorneringSpeed() {
        return corneringSpeed;
    }

    public String getDlc() {
        return dlc;
    }

    public Set<Livery> getLiveries() {
        return liveries;
    }
}
