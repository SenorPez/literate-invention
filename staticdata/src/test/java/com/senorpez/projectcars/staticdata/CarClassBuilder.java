package com.senorpez.projectcars.staticdata;

class CarClassBuilder {
    private int id = 0;
    private String name = null;
    private String abbreviation = null;

    CarClassBuilder() {
    }

    CarClass build() {
        return new CarClass(id, name);
    }

    CarClass2 build2() {
        return new CarClass2(id, name, abbreviation);
    }

    CarClassBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    CarClassBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    CarClassBuilder setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }
}
