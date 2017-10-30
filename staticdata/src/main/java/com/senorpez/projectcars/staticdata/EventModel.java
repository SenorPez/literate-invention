package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "event", collectionRelation = "event")
class EventModel implements Identifiable<Integer> {
    private final int id;
    private final String name;
    private final Integer tier;
    private final Boolean verified;

    EventModel(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.tier = event.getTier();
        this.verified = event.getVerified();
    }

    EventResource toResource() {
        final APIResourceAssembler<EventModel, EventResource> assembler = new APIResourceAssembler<>(EventController.class, EventResource.class, () -> new EventResource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTier() {
        return tier;
    }

    public Boolean getVerified() {
        return verified;
    }
}
