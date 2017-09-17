package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class Event {
    private final int id;
    private final String name;
    private final Integer tier;
    private final Set<Car> cars;
    private final Set<Round> rounds;
    private final Boolean verified;

    private final static AtomicInteger eventId = new AtomicInteger(0);

    Event(
            @JsonProperty("name") final String name,
            @JsonProperty("tier") final Integer tier,
            @JsonProperty("rounds") final JsonNode rounds,
            @JsonProperty("verified") final boolean verified,
            @JsonProperty("carFilter") final JsonNode carFilter) {
        this.id = eventId.incrementAndGet();
        this.name = name;
        this.tier = tier;

        if (carFilter.isNull()) {
            this.cars = null;
        } else {
            final Set<Car> cars = new HashSet<>(Application.CARS);
            final Set<CarFilter> carFilters = Application.getData(CarFilter.class, carFilter);
            carFilters.forEach(filter -> cars.removeIf(filter.getOperation().negate()));
            this.cars = cars;
        }

        this.verified = verified;

        Round.resetId();
        this.rounds = Application.getData(Round.class, rounds);
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Integer getTier() {
        return tier;
    }

    Set<Car> getCars() {
        return cars;
    }

    Set<Round> getRounds() {
        return rounds;
    }

    Boolean getVerified() {
        return verified;
    }
}
