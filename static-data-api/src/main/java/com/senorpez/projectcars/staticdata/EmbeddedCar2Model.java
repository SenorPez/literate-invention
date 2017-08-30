package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
public class EmbeddedCar2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;

    public EmbeddedCar2Model(final int id, final Car2 car) {
        this.id = id;
        this.name = car.getManufacturer() + " " + car.getModel();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
