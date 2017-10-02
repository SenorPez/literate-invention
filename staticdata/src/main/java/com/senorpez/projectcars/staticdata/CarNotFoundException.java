package com.senorpez.projectcars.staticdata;

class CarNotFoundException extends RuntimeException {
    CarNotFoundException(final int carId) {
        super(String.format("Car with ID of %d not found", carId));
    }
}
