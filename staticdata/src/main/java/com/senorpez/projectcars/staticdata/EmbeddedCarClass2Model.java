package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "class", collectionRelation = "class")
class EmbeddedCarClass2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedCarClass2Model(final CarClass2 carClass) {
        this.id = carClass.getId();
        this.name = carClass.getName();
    }

    Resource<EmbeddedCarClass2Model> toResource() {
        final APIEmbeddedResourceAssembler<EmbeddedCarClass2Model, EmbeddedCarClass2Resource> assembler = new APIEmbeddedResourceAssembler<>(CarClass2Controller.class, EmbeddedCarClass2Resource.class, () -> new EmbeddedCarClass2Resource(this));
        return assembler.toResource(this);
    }

    private class EmbeddedCarClass2Resource extends Resource<EmbeddedCarClass2Model> {
        private EmbeddedCarClass2Resource(final EmbeddedCarClass2Model content, final Link... links) {
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
