package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EmbeddedRaceResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedRaceModel, EmbeddedRaceResource> {
    private final Class<EmbeddedRaceResource> resourceClass;

    EmbeddedRaceResourceAssembler(final Class<?> controllerClass, final Class<EmbeddedRaceResource> resourceClass) {
        super(controllerClass, resourceClass);
        this.resourceClass = resourceClass;
    }

    @Override
    public EmbeddedRaceResource toResource(final EmbeddedRaceModel entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedRaceResource instantiateResource(final EmbeddedRaceModel entity) {
        try {
            final Constructor<EmbeddedRaceResource> constructor = resourceClass.getConstructor(EmbeddedRaceModel.class, Link[].class);
            return constructor.newInstance(entity, new Link[]{});
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
