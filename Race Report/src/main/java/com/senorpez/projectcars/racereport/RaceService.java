package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
final class RaceService implements GenericService<
        RaceModel,
        RaceResource,
        Race, Integer> {
    @Override
    public List<RaceModel> findAll(final Collection<Race> entities) {
        final AtomicInteger index = new AtomicInteger(0);
        return entities.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .collect(Collectors.toList());
    }

    @Override
    public RaceModel findOne(final Collection<Race> entities, final Integer entityId) {
        final AtomicInteger index = new AtomicInteger(0);
        return entities.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .filter(model -> model.getId().equals(entityId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<RaceResource> toResource(final List<RaceModel> models, final Object... parameters) {
        final Resources<RaceResource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model, parameters))
                .collect(Collectors.toList()));
        resources.add(linkTo(methodOn(RaceController.class).races()).withSelfRel());
        return resources;
    }

    @Override
    public RaceResource toResource(final RaceModel model, final Object... parameters) {
        return makeResource(model,
                () -> new RaceResource(
                        model,
                        linkTo(methodOn(RaceController.class).races()).withRel("races"),
                        linkTo(methodOn(RootController.class).root()).withRel("index")),
                RaceController.class,
                RaceResource.class,
                parameters);
    }
}