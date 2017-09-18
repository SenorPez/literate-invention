package com.senorpez.projectcars.staticdata;

class LiveryNotFoundException extends RuntimeException {
    LiveryNotFoundException(final int liveryId) {
        super(String.format("Livery with ID of %d not found", liveryId));
    }
}
