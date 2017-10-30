package com.senorpez.projectcars.staticdata;

class RoundNotFoundException extends RuntimeException {
    RoundNotFoundException(final int roundId) {
        super(String.format("Round with ID of %d not found", roundId));
    }
}
