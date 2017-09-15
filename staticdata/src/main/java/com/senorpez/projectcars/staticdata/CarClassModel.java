package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "class", collectionRelation = "class")
class CarClassModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    CarClassModel(final CarClass carClass) {
        this.id = carClass.getId();
        this.name = carClass.getName();
    }

    CarClassResource toResource() {
        final CarClassResourceAssembler assembler = new CarClassResourceAssembler(() -> new CarClassResource(this));
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
