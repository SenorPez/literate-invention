package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class LiveryResourceAssembler extends IdentifiableResourceAssemblerSupport<LiveryModel, LiveryResource> {
    final private Supplier<LiveryResource> supplier;

    LiveryResourceAssembler(final Supplier<LiveryResource> supplier) {
        super(LiveryController.class, LiveryResource.class);
        this.supplier = supplier;
    }

    @Override
    public LiveryResource toResource(LiveryModel entity) {
        return createResource(entity);
    }

    public LiveryResource toResource(LiveryModel entity, final int carId) {
        return createResource(entity, carId);
    }

    @Override
    protected LiveryResource instantiateResource(LiveryModel entity) {
        return supplier.get();
    }
}
