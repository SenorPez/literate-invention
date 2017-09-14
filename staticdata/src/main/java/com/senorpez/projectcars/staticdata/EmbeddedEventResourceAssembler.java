package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class EmbeddedEventResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedEventModel, EmbeddedEventResource> {
    final private Supplier<EmbeddedEventResource> supplier;

    EmbeddedEventResourceAssembler(final Supplier<EmbeddedEventResource> supplier) {
        super(EventController.class, EmbeddedEventResource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedEventResource toResource(EmbeddedEventModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedEventResource instantiateResource(EmbeddedEventModel entity) {
        return supplier.get();
    }
}
