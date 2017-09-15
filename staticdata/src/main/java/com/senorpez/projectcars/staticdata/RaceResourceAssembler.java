package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class RaceResourceAssembler extends IdentifiableResourceAssemblerSupport<RaceModel, RaceResource> {
    final private Supplier<RaceResource> supplier;

    RaceResourceAssembler(final Supplier<RaceResource> supplier) {
        super(RaceController.class, RaceResource.class);
        this.supplier = supplier;
    }

    @Override
    public RaceResource toResource(final RaceModel entity) {
        return createResource(entity);
    }

    RaceResource toResource(final RaceModel entity, final int eventId, final int roundId) {
        return createResource(entity, eventId, roundId);
    }

    @Override
    protected RaceResource instantiateResource(final RaceModel entity) {
        return supplier.get();
    }
}
