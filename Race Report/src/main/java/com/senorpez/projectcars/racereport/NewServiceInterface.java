package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

interface NewServiceInterface<
        M extends Identifiable<?>,
        R extends ResourceSupport,
        T, U> {
    List<M> findAll(Collection<T> entities);
    M findOne(Collection<T> entities, U entityId);
    Resources<R> toResource(List<M> models, Object... parameters);
    R toResource(M model, Object... parameters);

    default <C> R makeResource(final M model, final Supplier<R> supplier, final Class<C> controllerClass, final Class<R> resourceClass, final Object... parameters) {
        final ResourceAssembler<M, R> assembler = makeAssembler(supplier, controllerClass, resourceClass);
        return assembler.toResource(model, parameters);
    }

    default <C> ResourceAssembler<M, R> makeAssembler(final Supplier<R> supplier, final Class<C> controllerClass, final Class<R> resourceClass) {
        return new ResourceAssembler<>(
                controllerClass,
                resourceClass,
                supplier);
    }
}
