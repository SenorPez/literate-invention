package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Driver;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
final public class DriverService implements ChildService<RaceModel, Driver, EmbeddedDriverResource, DriverResource> {
    @Override
    public RaceModel findParent(final int raceId) {
        final AtomicInteger index = new AtomicInteger(0);
        return Application.RACES.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .filter(race -> race.getId().equals(raceId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<EmbeddedDriverResource> findAll(final RaceModel race, final Set<Driver> drivers) {
        return new Resources<>(drivers.stream()
                .map(DriverModel::new)
                .map(model -> makeResource(model.getEmbeddable(),
                        () -> new EmbeddedDriverResource(model.getEmbeddable()),
                        DriverController.class,
                        EmbeddedDriverResource.class,
                        race.getId()))
                .collect(Collectors.toList()),
                linkTo(methodOn(DriverController.class).drivers(race.getId())).withSelfRel(),
                linkTo(methodOn(RaceController.class).race(race.getId())).withRel("race"),
                linkTo(methodOn(RootController.class).root()).withRel("index"));
    }

    @Override
    public DriverResource findOne(final RaceModel race, final Set<Driver> drivers, final int driverId) {
        return drivers.stream()
                .map(DriverModel::new)
                .filter(model -> model.getId().equals(driverId))
                .findAny()
                .map(model -> makeResource(model,
                        () -> new DriverResource(
                                model,
                                linkTo(methodOn(DriverController.class).drivers(race.getId())).withRel("drivers"),
                                linkTo(methodOn(RootController.class).root()).withRel("index")),
                        DriverController.class,
                        DriverResource.class,
                        race.getId(),
                        driverId))
                .orElseThrow(() -> new RuntimeException("Driver."));
    }

    @Override
    public Resources<EmbeddedDriverResource> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DriverResource findOne(final int driverId) {
        throw new UnsupportedOperationException();
    }

    public DriverModel findOneModel(final RaceModel parent, final int id) {
        return parent.getDrivers().stream()
                .map(DriverModel::new)
                .filter(driver -> driver.getId().equals(id))
                .findAny()
                .orElseThrow(RuntimeException::new);

    }
}
