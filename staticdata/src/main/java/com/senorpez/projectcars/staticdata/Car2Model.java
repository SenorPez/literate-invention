package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class Car2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;
    @JsonProperty("class")
    private final String carClass;
    @JsonIgnore
    private final int carClassId;
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
    @JsonProperty("antilockBrakeSystem")
    private final boolean antilockBrakeSystem;
    @JsonProperty("tractionControl")
    private final boolean tractionControl;
    @JsonProperty("stabilityControl")
    private final boolean stabilityControl;
    private final int controlDifficulty;
    private final int corneringSpeed;
    private final String dlc;

    Car2Model(final Car2 car) {
        this.id = car.getId();
        this.name = car.getName();
        this.carClass = car.getCarClass().getName();
        this.carClassId = car.getCarClass().getId();
        this.manufacturer = car.getManufacturer();
        this.country = car.getCountry();
        this.year = car.getYear();
        this.drivetrain = car.getDrivetrain();
        this.topSpeed = car.getTopSpeed();
        this.acceleration = car.getAcceleration();
        this.power = car.getPower();
        this.weight = car.getWeight();
        this.gears = car.getGears();
        this.transmission = car.getTransmission();
        this.engineType = car.getEngineType();
        this.antilockBrakeSystem = car.hasAntilockBrakeSystem();
        this.tractionControl = car.hasTractionControl();
        this.stabilityControl = car.hasStabilityControl();
        this.controlDifficulty = car.getControlDifficulty();
        this.corneringSpeed = car.getCorneringSpeed();
        this.dlc = car.getDlc();
    }

    Car2Resource toResource() {
        final APIResourceAssembler<Car2Model, Car2Resource> assembler = new APIResourceAssembler<>(Car2Controller.class, Car2Resource.class, () -> new Car2Resource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCarClass() {
        return carClass;
    }

    int getCarClassId() {
        return carClassId;
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

    public int getPower() {
        return power;
    }

    public int getWeight() {
        return weight;
    }

    public int getGears() {
        return gears;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getEngineType() {
        return engineType;
    }

    public boolean hasAntilockBrakeSystem() {
        return antilockBrakeSystem;
    }

    public boolean hasTractionControl() {
        return tractionControl;
    }

    public boolean hasStabilityControl() {
        return stabilityControl;
    }

    public int getControlDifficulty() {
        return controlDifficulty;
    }

    public int getCorneringSpeed() {
        return corneringSpeed;
    }

    public String getDlc() {
        return dlc;
    }
}
