package com.senorpez.projectcars.staticdata;

class RaceNotFoundException extends RuntimeException {
    RaceNotFoundException(final int raceId) {
        super(String.format("Race with ID of %d not found", raceId));
    }
}
