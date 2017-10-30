package com.senorpez.projectcars.staticdata;

import java.util.Set;

public class RoundBuilder {
    private int id = 0;
    private Track track = null;
    private Set<Race> races = null;

    RoundBuilder() {
    }

    Round build() {
        return new Round(id, track, races);
    }

    public RoundBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    RoundBuilder setTrack(final Track track) {
        this.track = track;
        return this;
    }

    RoundBuilder setRaces(final Set<Race> races) {
        this.races = races;
        return this;
    }
}
