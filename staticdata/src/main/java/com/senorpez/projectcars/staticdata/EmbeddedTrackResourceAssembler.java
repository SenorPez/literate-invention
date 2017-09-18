package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class EmbeddedTrackResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedTrackModel, EmbeddedTrackResource> {
    final private Supplier<EmbeddedTrackResource> supplier;

    EmbeddedTrackResourceAssembler(final Supplier<EmbeddedTrackResource> supplier) {
        super(TrackController.class, EmbeddedTrackResource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedTrackResource toResource(final EmbeddedTrackModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedTrackResource instantiateResource(final EmbeddedTrackModel entity) {
        return supplier.get();
    }
}
