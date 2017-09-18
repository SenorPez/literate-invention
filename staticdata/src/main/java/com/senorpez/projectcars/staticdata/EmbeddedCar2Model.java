package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class EmbeddedCar2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedCar2Model(final Car2 car) {
        this.id = car.getId();
        this.name = car.getManufacturer() + " " + car.getModel();
    }

    EmbeddedCar2Resource toResource() {
        final EmbeddedCar2ResourceAssembler assembler = new EmbeddedCar2ResourceAssembler(() -> new EmbeddedCar2Resource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
