package com.senorpez.projectcars.staticdata;

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

        EnginePosition(String displayString) {
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

        ShiftPattern(String displayString) {
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

        Shifter(String displayString) {
            this.displayString = displayString;
        }

        @JsonValue
        public String getDisplayString() {
            return displayString;
        }
    }

    public Car(
            @JsonProperty("id") int id,
            @JsonProperty("manufacturer") String manufacturer,
            @JsonProperty("model") String model,
            @JsonProperty("country") String country,
            @JsonProperty("class") String carClass,
            @JsonProperty("year") int year,
            @JsonProperty("drivetrain") Drivetrain drivetrain,
            @JsonProperty("enginePosition") EnginePosition enginePosition,
            @JsonProperty("engineType") String engineType,
            @JsonProperty("topSpeed") int topSpeed,
            @JsonProperty("horsepower") int horsepower,
            @JsonProperty("acceleration") float acceleration,
            @JsonProperty("braking") float braking,
            @JsonProperty("weight") int weight,
            @JsonProperty("torque") int torque,
            @JsonProperty("weightBalance") int weightBalance,
            @JsonProperty("wheelbase") float wheelbase,
            @JsonProperty("shiftPattern") ShiftPattern shiftPattern,
            @JsonProperty("shifter") Shifter shifter,
            @JsonProperty("gears") int gears,
            @JsonProperty("dlc") String dlc,
            @JsonProperty("liveries") JsonNode liveries) {
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

    public int getId() {
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

    public CarClass getCarClass() {
        return carClass;
    }

    public int getYear() {
        return year;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public EnginePosition getEnginePosition() {
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

    public ShiftPattern getShiftPattern() {
        return shiftPattern;
    }

    public Shifter getShifter() {
        return shifter;
    }

    public int getGears() {
        return gears;
    }

    public String getDlc() {
        return dlc;
    }

    public Set<Livery> getLiveries() {
        return liveries;
    }
}
