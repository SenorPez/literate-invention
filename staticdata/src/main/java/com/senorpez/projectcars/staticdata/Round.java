package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Round {
    private final int id;
    private final Track track;
    private final Set<Race> races;

    private final static AtomicInteger roundId = new AtomicInteger(0);

    public Round(
            @JsonProperty("location") String location,
            @JsonProperty("variation") String variation,
            @JsonProperty("races")JsonNode races) {
        this.id = roundId.incrementAndGet();
        this.track = Application.TRACKS.stream()
                .filter(foundTrack ->
                        foundTrack.getLocation().equalsIgnoreCase(location)
                                && foundTrack.getVariation().equalsIgnoreCase(variation))
                .findFirst()
                .orElse(null);

        Race.resetId();
        this.races = Application.getData(Race.class, races);
    }

    public int getId() {
        return id;
    }

    public Track getTrack() {
        return track;
    }

    public Set<Race> getRaces() {
        return races;
    }

    static void resetId() {
        roundId.set(0);
    }
}
