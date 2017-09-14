package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class CarModel implements Identifiable<Integer> {
    private final int id;
    private final String manufacturer;
    private final String model;
    private final String country;
    private final String carClass;
    private final int year;
    private final String drivetrain;
    private final String enginePosition;
    private final String engineType;
    private final int topSpeed;
    private final int horsepower;
    private final float acceleration;
    private final float braking;
    private final int weight;
    private final int torque;
    private final int weightBalance;
    private final float wheelbase;
    private final String shiftPattern;
    private final String shifter;
    private final int gears;
    private final String dlc;

    CarModel(final Car car) {
        this.id = car.getId();
        this.manufacturer = car.getManufacturer();
        this.model = car.getModel();
        this.country = car.getCountry();
        this.carClass = car.getCarClass().getName();
        this.year = car.getYear();
        this.drivetrain = car.getDrivetrain().name();
        this.enginePosition = car.getEnginePosition().getDisplayString();
        this.engineType = car.getEngineType();
        this.topSpeed = car.getTopSpeed();
        this.horsepower = car.getHorsepower();
        this.acceleration = car.getAcceleration();
        this.braking = car.getBraking();
        this.weight = car.getWeight();
        this.torque = car.getTorque();
        this.weightBalance = car.getWeightBalance();
        this.wheelbase = car.getWheelbase();
        this.shiftPattern = car.getShiftPattern().getDisplayString();
        this.shifter = car.getShifter().getDisplayString();
        this.gears = car.getGears();
        this.dlc = car.getDlc();
    }

    CarResource toResource() {
        final CarResourceAssembler assembler = new CarResourceAssembler(() -> new CarResource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getCountry() {
        return country;
    }

    public String getCarClass() {
        return carClass;
    }

    public int getYear() {
        return year;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public String getEnginePosition() {
        return enginePosition;
    }

    public String getEngineType() {
        return engineType;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getBraking() {
        return braking;
    }

    public int getWeight() {
        return weight;
    }

    public int getTorque() {
        return torque;
    }

    public int getWeightBalance() {
        return weightBalance;
    }

    public float getWheelbase() {
        return wheelbase;
    }

    public String getShiftPattern() {
        return shiftPattern;
    }

    public String getShifter() {
        return shifter;
    }

    public int getGears() {
        return gears;
    }

    public String getDlc() {
        return dlc;
    }
}
