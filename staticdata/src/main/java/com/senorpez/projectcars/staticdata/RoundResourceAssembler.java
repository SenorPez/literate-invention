package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class RoundResourceAssembler extends IdentifiableResourceAssemblerSupport<RoundModel, RoundResource> {
    final private Supplier<RoundResource> supplier;

    RoundResourceAssembler(final Supplier<RoundResource> supplier) {
        super(RoundController.class, RoundResource.class);
        this.supplier = supplier;
    }

    @Override
    public RoundResource toResource(RoundModel entity) {
        return createResource(entity);
    }

    public RoundResource toResource(RoundModel entity, final int eventId) {
        return createResource(entity, eventId);
    }

    @Override
    protected RoundResource instantiateResource(RoundModel entity) {
        return supplier.get();
    }
}
