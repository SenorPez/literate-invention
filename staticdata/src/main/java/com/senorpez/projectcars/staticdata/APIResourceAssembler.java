package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;

import java.util.function.Supplier;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class APIResourceAssembler<M extends Identifiable<Integer>, R extends ResourceSupport> extends APIEmbeddedResourceAssembler<M, R> {
    APIResourceAssembler(final Class controllerClass, final Class<R> resourceType, final Supplier<R> supplier) {
        super(controllerClass, resourceType, supplier);
    }

    @Override
    public R toResource(final M entity) {
        final R resource = super.toResource(entity);
        return addIndexLink(resource);
    }

    @Override
    public R toResource(final M entity, final Object... parameters) {
        final R resource = super.toResource(entity, parameters);
        return addIndexLink(resource);
    }

    private R addIndexLink(final R resource) {
        resource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return resource;
    }
}
