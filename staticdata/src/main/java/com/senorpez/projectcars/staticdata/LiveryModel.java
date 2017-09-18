package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "livery", collectionRelation = "livery")
class LiveryModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    LiveryModel(final Livery livery) {
        this.id = livery.getId();
        this.name = livery.getName();
    }

    LiveryResource toResource() {
        final APIResourceAssembler<LiveryModel, LiveryResource> assembler = new APIResourceAssembler<>(LiveryController.class, LiveryResource.class, () -> new LiveryResource(this));
        return assembler.toResource(this);
    }

    LiveryResource toResource(final int carId) {
        final APIResourceAssembler<LiveryModel, LiveryResource> assembler = new APIResourceAssembler<>(LiveryController.class, LiveryResource.class, () -> new LiveryResource(this, carId));
        return assembler.toResource(this, carId);
    }

    LiveryResource toResource(final int eventId, final int carId) {
        final APIResourceAssembler<LiveryModel, LiveryResource> assembler = new APIResourceAssembler<>(LiveryController.class, LiveryResource.class, () -> new LiveryResource(this, eventId, carId));
        return assembler.toResource(this, carId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
