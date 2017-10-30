package com.senorpez.projectcars.staticdata;

import java.util.Set;

public class EventBuilder {
    private int id = 0;
    private String name = null;
    private Integer tier = null;
    private Set<Car> cars = null;
    private Set<Round> rounds = null;
    private Boolean verified = false;

    EventBuilder() {
    }

    Event build() {
        return new Event(id, name, tier, cars, rounds, verified);
    }

    public EventBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public EventBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    EventBuilder setTier(final Integer tier) {
        this.tier = tier;
        return this;
    }

    public EventBuilder setCars(final Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public EventBuilder setRounds(final Set<Round> rounds) {
        this.rounds = rounds;
        return this;
    }

    EventBuilder setVerified(final Boolean verified) {
        this.verified = verified;
        return this;
    }
}
