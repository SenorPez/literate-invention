package com.senorpez.projectcars.staticdata;

class LiveryBuilder {
    private int id = 0;
    private String name = null;

    LiveryBuilder() {
    }

    Livery build() {
        return new Livery(id, name);
    }

    LiveryBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    LiveryBuilder setName(final String name) {
        this.name = name;
        return this;
    }
}
