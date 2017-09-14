package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Event {
    private final int id;
    private final String name;
    private final int tier;
    private final Set<Car> cars;
    private final Set<Round> rounds;
    private final Boolean verified;

    private final static AtomicInteger eventId = new AtomicInteger(0);

    public Event(
            @JsonProperty("name") String name,
            @JsonProperty("tier") int tier,
            @JsonProperty("rounds")JsonNode rounds,
            @JsonProperty("verified") boolean verified,
            @JsonProperty("carFilter") JsonNode carFilter) {
        this.id = eventId.incrementAndGet();
        this.name = name;
        this.tier = tier;

        if (carFilter.isNull()) {
            this.cars = null;
        } else {
            Set<Car> cars = new HashSet<>(Application.CARS);
            Set<CarFilter> carFilters = Application.getData(CarFilter.class, carFilter);
            carFilters.forEach(filter -> cars.removeIf(filter.getOperation().negate()));
            this.cars = cars;
        }

        this.verified = verified;

        Round.resetId();
        this.rounds = Application.getData(Round.class, rounds);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public Set<Round> getRounds() {
        return rounds;
    }

    public Boolean getVerified() {
        return verified;
    }
}
