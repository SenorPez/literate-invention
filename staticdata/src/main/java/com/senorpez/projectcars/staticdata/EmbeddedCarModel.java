package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class EmbeddedCarModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedCarModel(final Car car) {
        this.id = car.getId();
        this.name = car.getManufacturer() + " " + car.getModel();
    }

    EmbeddedCarResource toResource() {
        final EmbeddedCarResourceAssembler assembler = new EmbeddedCarResourceAssembler(() -> new EmbeddedCarResource(this));
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
