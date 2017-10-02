package com.senorpez.projectcars.staticdata;

import com.senorpez.projectcars.staticdata.Car.Drivetrain;
import com.senorpez.projectcars.staticdata.Car.EnginePosition;
import com.senorpez.projectcars.staticdata.Car.ShiftPattern;
import com.senorpez.projectcars.staticdata.Car.Shifter;

import java.util.Set;

class CarBuilder {
    private int id = 0;
    private String manufacturer = null;
    private String model = null;
    private String country = null;
    private CarClass carClass = null;
    private int year = 0;
    private Drivetrain drivetrain = null;
    private EnginePosition enginePosition = null;
    private String engineType = null;
    private int topSpeed = 0;
    private int horsepower = 0;
    private float acceleration = 0f;
    private float braking = 0f;
    private int weight = 0;
    private int torque = 0;
    private int weightBalance = 0;
    private float wheelbase = 0f;
    private ShiftPattern shiftPattern = null;
    private Shifter shifter = null;
    private int gears = 0;
    private String dlc = null;
    private Set<Livery> liveries = null;

    Car build() {
        return new Car(
                id,
                manufacturer,
                model,
                country,
                carClass,
                year,
                drivetrain,
                enginePosition,
                engineType,
                topSpeed,
                horsepower,
                acceleration,
                braking,
                weight,
                torque,
                weightBalance,
                wheelbase,
                shiftPattern,
                shifter,
                gears,
                dlc,
                liveries
        );
    }

    Car2 build2() {
        return new Car2(id, String.join(" ", manufacturer, model), carClass, liveries);
    }

    CarBuilder() {
    }

    CarBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    CarBuilder setManufacturer(final String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    CarBuilder setModel(final String model) {
        this.model = model;
        return this;
    }

    CarBuilder setCountry(final String country) {
        this.country = country;
        return this;
    }

    CarBuilder setCarClass(final CarClass carClass) {
        this.carClass = carClass;
        return this;
    }

    CarBuilder setYear(final int year) {
        this.year = year;
        return this;
    }

    CarBuilder setDrivetrain(final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }

    CarBuilder setEnginePosition(final EnginePosition enginePosition) {
        this.enginePosition = enginePosition;
        return this;
    }

    CarBuilder setEngineType(final String engineType) {
        this.engineType = engineType;
        return this;
    }

    CarBuilder setTopSpeed(final int topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

    CarBuilder setHorsepower(final int horsepower) {
        this.horsepower = horsepower;
        return this;
    }

    CarBuilder setAcceleration(final float acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    CarBuilder setBraking(final float braking) {
        this.braking = braking;
        return this;
    }

    CarBuilder setWeight(final int weight) {
        this.weight = weight;
        return this;
    }

    CarBuilder setTorque(final int torque) {
        this.torque = torque;
        return this;
    }

    CarBuilder setWeightBalance(final int weightBalance) {
        this.weightBalance = weightBalance;
        return this;
    }

    CarBuilder setWheelbase(final float wheelbase) {
        this.wheelbase = wheelbase;
        return this;
    }

    CarBuilder setShiftPattern(final ShiftPattern shiftPattern) {
        this.shiftPattern = shiftPattern;
        return this;
    }

    CarBuilder setShifter(final Shifter shifter) {
        this.shifter = shifter;
        return this;
    }

    CarBuilder setGears(final int gears) {
        this.gears = gears;
        return this;
    }

    CarBuilder setDlc(final String dlc) {
        this.dlc = dlc;
        return this;
    }

    CarBuilder setLiveries(final Set<Livery> liveries) {
        this.liveries = liveries;
        return this;
    }
}
