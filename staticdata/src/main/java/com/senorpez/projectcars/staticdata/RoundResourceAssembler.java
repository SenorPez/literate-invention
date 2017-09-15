package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class RoundResourceAssembler extends IdentifiableResourceAssemblerSupport<RoundModel, RoundResource> {
    final private Supplier<RoundResource> supplier;

    RoundResourceAssembler(final Supplier<RoundResource> supplier) {
        super(RoundController.class, RoundResource.class);
        this.supplier = supplier;
    }

    @Override
    public RoundResource toResource(final RoundModel entity) {
        return createResource(entity);
    }

    RoundResource toResource(final RoundModel entity, final int eventId) {
        return createResource(entity, eventId);
    }

    @Override
    protected RoundResource instantiateResource(final RoundModel entity) {
        return supplier.get();
    }
}
