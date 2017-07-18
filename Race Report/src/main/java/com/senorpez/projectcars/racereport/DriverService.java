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
final class DriverService implements GenericService<
        DriverModel,
        DriverResource,
        Driver, Integer> {
    @Override
    public List<DriverModel> findAll(final Collection<Driver> entities) {
        return entities.stream()
                .map(DriverModel::new)
                .collect(Collectors.toList());
    }

    @Override
    public DriverModel findOne(final Collection<Driver> entities, final Integer entityId) {
        return entities.stream()
                .map(DriverModel::new)
                .filter(model -> model.getId().equals(entityId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<DriverResource> toResource(final List<DriverModel> models, final Object... parameters) {
        final Resources<DriverResource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model, parameters))
                .collect(Collectors.toList()));
        resources.add(linkTo(methodOn(DriverController.class).drivers((int) parameters[0])).withSelfRel());
        return resources;
    }

    @Override
    public DriverResource toResource(final DriverModel model, final Object... parameters) {
        return makeResource(model,
                () -> new DriverResource(
                        model,
                        linkTo(methodOn(DriverController.class).drivers((int) parameters[0])).withRel("drivers"),
                        linkTo(methodOn(RootController.class).root()).withRel("index")),
                DriverController.class,
                DriverResource.class,
                parameters);
    }
}