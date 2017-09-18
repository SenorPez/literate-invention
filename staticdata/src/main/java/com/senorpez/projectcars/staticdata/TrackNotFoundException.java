package com.senorpez.projectcars.staticdata;

class TrackNotFoundException extends RuntimeException {
    TrackNotFoundException(final int trackId) {
        super(String.format("Track with ID of %d not found", trackId));
    }

    TrackNotFoundException(final String location, final String variation) {
        super(String.format("Track with location of %s and variation of %s not found", location, variation));
    }

}
