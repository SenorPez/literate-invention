package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.function.Supplier;

interface GenericService<E extends ResourceSupport, S extends ResourceSupport> {
    Resources<E> findAll();
    S findOne(final int Id);

    default <M extends Identifiable<?>, T extends ResourceSupport, U> ResourceAssembler<M, T> makeAssembler(final Supplier<T> supplier, final Class<U> controllerClass, final Class<T> resourceClass) {
        return new ResourceAssembler<>(
                controllerClass,
                resourceClass,
                supplier);
    }

    default <M extends Identifiable<?>, T extends ResourceSupport, U> T makeResource(final M model, final Supplier<T> supplier, final Class<U> controllerClass, final Class<T> resourceClass) {
        final ResourceAssembler<M, T> assembler = makeAssembler(supplier, controllerClass, resourceClass);
        return assembler.toResource(model);
    }
}
