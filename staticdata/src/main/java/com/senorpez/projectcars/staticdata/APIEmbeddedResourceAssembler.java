package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class APIEmbeddedResourceAssembler<M extends Identifiable<Integer>, R extends ResourceSupport> extends IdentifiableResourceAssemblerSupport<M, R> {
    private final Supplier<R> supplier;

    APIEmbeddedResourceAssembler(final Class controllerClass, final Class<R> resourceType, final Supplier<R> supplier) {
        super(controllerClass, resourceType);
        this.supplier = supplier;
    }

    @Override
    public R toResource(final M entity) {
        return createResource(entity);
    }

    R toResource(final M entity, final Object... parameters) {
        return createResource(entity, parameters);
    }

    @Override
    protected R instantiateResource(final M entity) {
        return supplier.get();
    }
}