package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Driver;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "driver", collectionRelation = "driver")
class EmbeddedDriverModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedDriverModel(final DriverModel driver) {
        this.id = driver.getId();
        this.name = driver.getName();
    }

    EmbeddedDriverModel(final Driver driver) {
        this.id = driver.getIndex();
        this.name = driver.getName();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
