package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface NewServiceInterface<
        M extends Identifiable<?>,
        R extends ResourceSupport,
        T> {
    List<M> findAll(Collection<T> entities);
    M findOne(Collection<T> entities, T lapId);
    Resources<R> toResource(List<M> models, Object... parameters);
    R toResource(M model, Object... parameters);

    default <U> R makeResource(final M model, final Supplier<R> supplier, final Class<U> controllerClass, final Class<R> resourceClass, final Object... parameters) {
        final ResourceAssembler<M, R> assembler = makeAssembler(supplier, controllerClass, resourceClass);
        return assembler.toResource(model, parameters);
    }

    default <U> ResourceAssembler<M, R> makeAssembler(final Supplier<R> supplier, final Class<U> controllerClass, final Class<R> resourceClass) {
        return new ResourceAssembler<>(
                controllerClass,
                resourceClass,
                supplier);
    }
}
