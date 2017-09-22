package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class EmbeddedCar2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedCar2Model(final Car2 car) {
        this.id = car.getId();
        this.name = car.getManufacturer() + " " + car.getModel();
    }

    Resource<EmbeddedCar2Model> toResource() {
        final APIEmbeddedResourceAssembler<EmbeddedCar2Model, EmbeddedCar2Resource> assembler = new APIEmbeddedResourceAssembler<>(Car2Controller.class, EmbeddedCar2Resource.class, () -> new EmbeddedCar2Resource(this));
        return assembler.toResource(this);
    }

    private class EmbeddedCar2Resource extends Resource<EmbeddedCar2Model> {
        private EmbeddedCar2Resource(final EmbeddedCar2Model content, final Link... links) {
            super(content, links);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
