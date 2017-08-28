package com.senorpez.projectcars.staticdata;

class Car2Builder {
    private String name = "default";

    Car2Builder() {
    }

    Car2Builder setName(final String name) {
        this.name = name;
        return this;
    }

    Car2 build() {
        return new Car2(name);
    }
}
