package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "event", collectionRelation = "event")
class EmbeddedEventModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedEventModel(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    Resource<EmbeddedEventModel> toResource() {
        final APIEmbeddedResourceAssembler<EmbeddedEventModel, EmbeddedEventResource> assembler = new APIEmbeddedResourceAssembler<>(EventController.class, EmbeddedEventResource.class, () -> new EmbeddedEventResource(this));
        return assembler.toResource(this);
    }

    private class EmbeddedEventResource extends Resource<EmbeddedEventModel> {
        private EmbeddedEventResource(final EmbeddedEventModel content, final Link... links) {
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
