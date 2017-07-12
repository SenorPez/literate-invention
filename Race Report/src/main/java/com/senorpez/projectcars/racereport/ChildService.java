package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Set;
import java.util.function.Supplier;

interface ChildService<P extends Identifiable<?>, C, E extends ResourceSupport, S extends ResourceSupport> extends GenericService<E, S> {
    P findParent(final int Id);
    Resources<E> findAll(final P parent, final Set<C> children);
    S findOne(final P parent, final Set<C> children, final int Id);

    default <M extends Identifiable<?>, T extends ResourceSupport, U> T makeResource(final M model, final Supplier<T> supplier, final Class<U> controllerClass, final Class<T> resourceClass, final Object... parameters) {
        final ResourceAssembler<M, T> assembler = makeAssembler(supplier, controllerClass, resourceClass);
        return assembler.toResource(model, parameters);
    }
}
