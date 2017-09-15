package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class EventResourceAssembler extends IdentifiableResourceAssemblerSupport<EventModel, EventResource> {
    final private Supplier<EventResource> supplier;

    EventResourceAssembler(final Supplier<EventResource> supplier) {
        super(EventController.class, EventResource.class);
        this.supplier = supplier;
    }

    @Override
    public EventResource toResource(final EventModel entity) {
        return createResource(entity);
    }

    @Override
    protected EventResource instantiateResource(final EventModel entity) {
        return supplier.get();
    }
}
