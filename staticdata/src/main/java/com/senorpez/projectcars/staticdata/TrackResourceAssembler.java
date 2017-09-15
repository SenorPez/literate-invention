package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class TrackResourceAssembler extends IdentifiableResourceAssemblerSupport<TrackModel, TrackResource> {
    final private Supplier<TrackResource> supplier;

    TrackResourceAssembler(final Supplier<TrackResource> supplier) {
        super(TrackController.class, TrackResource.class);
        this.supplier = supplier;
    }

    @Override
    public TrackResource toResource(final TrackModel entity) {
        return createResource(entity);
    }

    @Override
    protected TrackResource instantiateResource(final TrackModel entity) {
        return supplier.get();
    }
}
