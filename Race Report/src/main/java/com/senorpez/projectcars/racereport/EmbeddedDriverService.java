package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Driver;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class EmbeddedDriverService implements NewServiceInterface<
        EmbeddedDriverModel,
        EmbeddedDriverResource,
        Driver, Integer> {
    @Override
    public List<EmbeddedDriverModel> findAll(final Collection<Driver> entities) {
        return entities.stream()
                .map(EmbeddedDriverModel::new)
                .collect(Collectors.toList());
    }

    @Override
    public EmbeddedDriverModel findOne(final Collection<Driver> entities, final Integer entityId) {
        return entities.stream()
                .map(EmbeddedDriverModel::new)
                .filter(model -> model.getId().equals(entityId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<EmbeddedDriverResource> toResource(final List<EmbeddedDriverModel> models, final Object... parameters) {
        final Resources<EmbeddedDriverResource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model, parameters))
                .collect(Collectors.toList()));
        resources.add(linkTo(methodOn(DriverController.class).drivers((int) parameters[0])).withSelfRel());
        return resources;
    }

    @Override
    public EmbeddedDriverResource toResource(final EmbeddedDriverModel model, final Object... parameters) {
        return makeResource(model,
                () -> new EmbeddedDriverResource(
                        model,
                        linkTo(methodOn(DriverController.class).drivers((int) parameters[0])).withRel("drivers"),
                        linkTo(methodOn(RootController.class).root()).withRel("index")),
                DriverController.class,
                EmbeddedDriverResource.class,
                parameters);
    }
}
