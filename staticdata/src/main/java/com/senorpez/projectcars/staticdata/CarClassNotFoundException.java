package com.senorpez.projectcars.staticdata;

class CarClassNotFoundException extends RuntimeException {
    CarClassNotFoundException(final int carClassId) {
        super(String.format("Class with ID of %d not found", carClassId));
    }
}
