package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class RaceResourceAssembler extends IdentifiableResourceAssemblerSupport<RaceModel, RaceResource> {
    final private Supplier<RaceResource> supplier;

    RaceResourceAssembler(final Supplier<RaceResource> supplier) {
        super(RaceController.class, RaceResource.class);
        this.supplier = supplier;
    }

    @Override
    public RaceResource toResource(RaceModel entity) {
        return createResource(entity);
    }

    public RaceResource toResource(RaceModel entity, final int eventId, final int roundId) {
        return createResource(entity, eventId, roundId);
    }

    @Override
    protected RaceResource instantiateResource(RaceModel entity) {
        return supplier.get();
    }
}
