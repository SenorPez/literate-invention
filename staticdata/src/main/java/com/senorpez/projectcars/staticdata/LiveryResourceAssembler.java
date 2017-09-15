package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class LiveryResourceAssembler extends IdentifiableResourceAssemblerSupport<LiveryModel, LiveryResource> {
    final private Supplier<LiveryResource> supplier;

    LiveryResourceAssembler(final Supplier<LiveryResource> supplier) {
        super(LiveryController.class, LiveryResource.class);
        this.supplier = supplier;
    }

    @Override
    public LiveryResource toResource(final LiveryModel entity) {
        return createResource(entity);
    }

    LiveryResource toResource(final LiveryModel entity, final int carId) {
        return createResource(entity, carId);
    }

    @Override
    protected LiveryResource instantiateResource(final LiveryModel entity) {
        return supplier.get();
    }
}
