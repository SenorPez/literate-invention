package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "class", collectionRelation = "class")
class CarClass2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;
    private final String abbreviation;

    CarClass2Model(final CarClass2 carClass) {
        this.id = carClass.getId();
        this.name = carClass.getName();
        this.abbreviation = carClass.getAbbreviation();
    }

    CarClass2Resource toResource() {
        final APIResourceAssembler<CarClass2Model, CarClass2Resource> assembler = new APIResourceAssembler<>(CarClass2Controller.class, CarClass2Resource.class, () -> new CarClass2Resource(this));
        return assembler.toResource(this);
    }

    CarClass2Resource toResource(final int carId) {
        final APIResourceAssembler<CarClass2Model, CarClass2Resource> assembler = new APIResourceAssembler<>(CarClass2Controller.class, CarClass2Resource.class, () -> new CarClass2Resource(this, carId));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
