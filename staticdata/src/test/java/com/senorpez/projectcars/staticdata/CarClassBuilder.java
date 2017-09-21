package com.senorpez.projectcars.staticdata;

class CarClassBuilder {
    private int id = 0;
    private String name = null;

    CarClassBuilder() {
    }

    CarClass build() {
        return new CarClass(id, name);
    }

    CarClassBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    CarClassBuilder setName(final String name) {
        this.name = name;
        return this;
    }
}
