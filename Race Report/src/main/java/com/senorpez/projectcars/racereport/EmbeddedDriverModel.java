package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "driver", collectionRelation = "driver")
public class EmbeddedDriverModel implements Identifiable<Integer> {
    private final int index;
    private final String name;

    EmbeddedDriverModel(final DriverModel driver) {
        this.index = driver.getId();
        this.name = driver.getName();
    }

    @Override
    public Integer getId() {
        return index;
    }

    public String getName() {
        return name;
    }
}
