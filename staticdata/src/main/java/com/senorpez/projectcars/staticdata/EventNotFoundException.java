package com.senorpez.projectcars.staticdata;

class EventNotFoundException extends RuntimeException {
    EventNotFoundException(final int eventId) {
        super(String.format("Event with ID of %d not found", eventId));
    }
}
