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
        final APIResourceAssembler<CarClassModel, CarClassResource> assembler = new APIResourceAssembler<>(CarClassController.class, CarClassResource.class, () -> new CarClassResource(this));
        return assembler.toResource(this);
    }

    CarClassResource toResource(final int carId) {
        final APIResourceAssembler<CarClassModel, CarClassResource> assembler = new APIResourceAssembler<>(CarClassController.class, CarClassResource.class, () -> new CarClassResource(this, carId));
        return assembler.toResource(this);
    }

    CarClassResource toResource(final int eventId, final int carId) {
        final APIResourceAssembler<CarClassModel, CarClassResource> assembler = new APIResourceAssembler<>(CarClassController.class, CarClassResource.class, () -> new CarClassResource(this, eventId, carId));
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
