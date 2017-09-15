package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class EmbeddedEventResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedEventModel, EmbeddedEventResource> {
    final private Supplier<EmbeddedEventResource> supplier;

    EmbeddedEventResourceAssembler(final Supplier<EmbeddedEventResource> supplier) {
        super(EventController.class, EmbeddedEventResource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedEventResource toResource(final EmbeddedEventModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedEventResource instantiateResource(final EmbeddedEventModel entity) {
        return supplier.get();
    }
}
