package com.senorpez.projectcars.staticdata;

class CarClassNotFoundException extends RuntimeException {
    CarClassNotFoundException(final int carClassId) {
        super(String.format("Car class with ID of %d not found", carClassId));
    }
}
