package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
final public class RaceService implements GenericService<EmbeddedRaceResource, RaceResource> {
    @Override
    public Resources<EmbeddedRaceResource> findAll() {
        final AtomicInteger index = new AtomicInteger(0);
        return new Resources<>(Application.RACES.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .map(model -> makeResource(model.getEmbeddable(),
                        () -> new EmbeddedRaceResource(model.getEmbeddable()),
                        RaceController.class,
                        EmbeddedRaceResource.class))
                .collect(Collectors.toList()),
                linkTo(methodOn(RaceController.class).races()).withSelfRel(),
                linkTo(methodOn(RootController.class).root()).withRel("index"));
    }

    @Override
    public RaceResource findOne(final int raceId) {
        final AtomicInteger index = new AtomicInteger(0);
        return Application.RACES.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .filter(race -> race.getId().equals(raceId))
                .findAny()
                .map(model -> makeResource(model,
                        () -> new RaceResource(
                                model,
                                linkTo(methodOn(RaceController.class).races()).withRel("races"),
                                linkTo(methodOn(DriverController.class).drivers(raceId)).withRel("drivers"),
                                linkTo(methodOn(RootController.class).root()).withRel("index")),
                        RaceController.class,
                        RaceResource.class))
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    RaceModel findOneModel(final int raceId) {
        final AtomicInteger index = new AtomicInteger(0);
        return Application.RACES.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .filter(race -> race.getId().equals(raceId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error"));
    }
}
