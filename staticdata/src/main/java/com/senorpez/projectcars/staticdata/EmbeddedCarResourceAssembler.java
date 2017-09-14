package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class EmbeddedCarResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedCarModel, EmbeddedCarResource> {
    final private Supplier<EmbeddedCarResource> supplier;

    EmbeddedCarResourceAssembler(final Supplier<EmbeddedCarResource> supplier) {
        super(CarController.class, EmbeddedCarResource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedCarResource toResource(EmbeddedCarModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedCarResource instantiateResource(EmbeddedCarModel entity) {
        return supplier.get();
    }
}
