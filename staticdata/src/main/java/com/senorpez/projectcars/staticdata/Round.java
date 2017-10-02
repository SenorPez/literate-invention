package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class Round {
    private final int id;
    private final Track track;
    private final Set<Race> races;

    private final static AtomicInteger roundId = new AtomicInteger(0);

    @JsonCreator
    Round(
            @JsonProperty("location") final String location,
            @JsonProperty("variation") final String variation,
            @JsonProperty("races") final JsonNode races) {
        this.id = roundId.incrementAndGet();
        this.track = Application.TRACKS.stream()
                .filter(foundTrack ->
                        foundTrack.getLocation().equalsIgnoreCase(location)
                                && foundTrack.getVariation().equalsIgnoreCase(variation))
                .findFirst()
                .orElseThrow(() -> new TrackNotFoundException(location, variation));

        Race.resetId();
        this.races = Application.getData(Race.class, races);
    }

    Round(final int id, final Track track, final Set<Race> races) {
        this.id = id;
        this.track = track;
        this.races = races;
    }

    int getId() {
        return id;
    }

    Track getTrack() {
        return track;
    }

    Set<Race> getRaces() {
        return races;
    }

    static void resetId() {
        roundId.set(0);
    }
}
