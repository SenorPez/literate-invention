package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

class Car {
    private final int id;
    private final String manufacturer;
    private final String model;
    private final String country;
    private final CarClass carClass;
    private final int year;
    private final Drivetrain drivetrain;
    private final EnginePosition enginePosition;
    private final String engineType;
    private final int topSpeed;
    private final int horsepower;
    private final float acceleration;
    private final float braking;
    private final int weight;
    private final int torque;
    private final int weightBalance;
    private final float wheelbase;
    private final ShiftPattern shiftPattern;
    private final Shifter shifter;
    private final int gears;
    private final String dlc;
    private final Set<Livery> liveries;

    enum Drivetrain {
        FWD,
        RWD,
        AWD
    }

    enum EnginePosition {
        FRONT ("Front"),
        MID ("Mid"),
        REAR ("Rear");

        private final String displayString;

        EnginePosition(final String displayString) {
            this.displayString = displayString;
        }

        @JsonValue
        public String getDisplayString() {
            return displayString;
        }
    }

    enum ShiftPattern {
        SEQUENTIAL ("Sequential"),
        H ("H");

        private final String displayString;

        ShiftPattern(final String displayString) {
            this.displayString = displayString;
        }

        @JsonValue
        public String getDisplayString() {
            return displayString;
        }
    }

    enum Shifter {
        SHIFTER ("Shifter"),
        PADDLES ("Paddles");

        private final String displayString;

        Shifter(final String displayString) {
            this.displayString = displayString;
        }

        @JsonValue
        public String getDisplayString() {
            return displayString;
        }
    }

    @JsonCreator
    public Car(
            @JsonProperty("id") final int id,
            @JsonProperty("manufacturer") final String manufacturer,
            @JsonProperty("model") final String model,
            @JsonProperty("country") final String country,
            @JsonProperty("class") final String carClass,
            @JsonProperty("year") final int year,
            @JsonProperty("drivetrain") final Drivetrain drivetrain,
            @JsonProperty("enginePosition") final EnginePosition enginePosition,
            @JsonProperty("engineType") final String engineType,
            @JsonProperty("topSpeed") final int topSpeed,
            @JsonProperty("horsepower") final int horsepower,
            @JsonProperty("acceleration") final float acceleration,
            @JsonProperty("braking") final float braking,
            @JsonProperty("weight") final int weight,
            @JsonProperty("torque") final int torque,
            @JsonProperty("weightBalance") final int weightBalance,
            @JsonProperty("wheelbase") final float wheelbase,
            @JsonProperty("shiftPattern") final ShiftPattern shiftPattern,
            @JsonProperty("shifter") final Shifter shifter,
            @JsonProperty("gears") final int gears,
            @JsonProperty("dlc") final String dlc,
            @JsonProperty("liveries") final JsonNode liveries) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.country = country;
        this.carClass = Application.CAR_CLASSES.stream().filter(cclass -> cclass.getName().equalsIgnoreCase(carClass)).findAny().orElseThrow(RuntimeException::new);
        this.year = year;
        this.drivetrain = drivetrain;
        this.enginePosition = enginePosition;
        this.engineType = engineType;
        this.topSpeed = topSpeed;
        this.horsepower = horsepower;
        this.acceleration = acceleration;
        this.braking = braking;
        this.weight = weight;
        this.torque = torque;
        this.weightBalance = weightBalance;
        this.wheelbase = wheelbase;
        this.shiftPattern = shiftPattern;
        this.shifter = shifter;
        this.gears = gears;
        this.dlc = dlc;
        this.liveries = Application.getData(Livery.class, liveries);
    }

    public Car(
            @JsonProperty("id") final int id,
            @JsonProperty("manufacturer") final String manufacturer,
            @JsonProperty("model") final String model,
            @JsonProperty("country") final String country,
            @JsonProperty("class") final CarClass carClass,
            @JsonProperty("year") final int year,
            @JsonProperty("drivetrain") final Drivetrain drivetrain,
            @JsonProperty("enginePosition") final EnginePosition enginePosition,
            @JsonProperty("engineType") final String engineType,
            @JsonProperty("topSpeed") final int topSpeed,
            @JsonProperty("horsepower") final int horsepower,
            @JsonProperty("acceleration") final float acceleration,
            @JsonProperty("braking") final float braking,
            @JsonProperty("weight") final int weight,
            @JsonProperty("torque") final int torque,
            @JsonProperty("weightBalance") final int weightBalance,
            @JsonProperty("wheelbase") final float wheelbase,
            @JsonProperty("shiftPattern") final ShiftPattern shiftPattern,
            @JsonProperty("shifter") final Shifter shifter,
            @JsonProperty("gears") final int gears,
            @JsonProperty("dlc") final String dlc,
            @JsonProperty("liveries") final Set<Livery> liveries) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.country = country;
        this.carClass = carClass;
        this.year = year;
        this.drivetrain = drivetrain;
        this.enginePosition = enginePosition;
        this.engineType = engineType;
        this.topSpeed = topSpeed;
        this.horsepower = horsepower;
        this.acceleration = acceleration;
        this.braking = braking;
        this.weight = weight;
        this.torque = torque;
        this.weightBalance = weightBalance;
        this.wheelbase = wheelbase;
        this.shiftPattern = shiftPattern;
        this.shifter = shifter;
        this.gears = gears;
        this.dlc = dlc;
        this.liveries = liveries;
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

    String getCountry() {
        return country;
    }

    CarClass getCarClass() {
        return carClass;
    }

    int getYear() {
        return year;
    }

    Drivetrain getDrivetrain() {
        return drivetrain;
    }

    EnginePosition getEnginePosition() {
        return enginePosition;
    }

    String getEngineType() {
        return engineType;
    }

    int getTopSpeed() {
        return topSpeed;
    }

    int getHorsepower() {
        return horsepower;
    }

    float getAcceleration() {
        return acceleration;
    }

    float getBraking() {
        return braking;
    }

    int getWeight() {
        return weight;
    }

    int getTorque() {
        return torque;
    }

    int getWeightBalance() {
        return weightBalance;
    }

    float getWheelbase() {
        return wheelbase;
    }

    ShiftPattern getShiftPattern() {
        return shiftPattern;
    }

    Shifter getShifter() {
        return shifter;
    }

    int getGears() {
        return gears;
    }

    String getDlc() {
        return dlc;
    }

    Set<Livery> getLiveries() {
        return liveries;
    }
}
