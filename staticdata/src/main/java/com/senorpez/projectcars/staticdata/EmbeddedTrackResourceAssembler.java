package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class EmbeddedTrackResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedTrackModel, EmbeddedTrackResource> {
    final private Supplier<EmbeddedTrackResource> supplier;

    EmbeddedTrackResourceAssembler(final Supplier<EmbeddedTrackResource> supplier) {
        super(TrackController.class, EmbeddedTrackResource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedTrackResource toResource(EmbeddedTrackModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedTrackResource instantiateResource(EmbeddedTrackModel entity) {
        return supplier.get();
    }
}
