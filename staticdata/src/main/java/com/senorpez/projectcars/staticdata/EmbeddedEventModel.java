package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "event", collectionRelation = "event")
class EmbeddedEventModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedEventModel(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    EmbeddedEventResource toResource() {
        final EmbeddedEventResourceAssembler assembler = new EmbeddedEventResourceAssembler(() -> new EmbeddedEventResource(this));
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
