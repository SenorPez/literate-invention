package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Resources;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@org.springframework.stereotype.Service
final class EmbeddedRaceService implements Service<
        EmbeddedRaceModel,
        EmbeddedRaceResource,
        Race, Integer> {
    @Override
    public List<EmbeddedRaceModel> findAll(final Collection<Race> entities) {
        final AtomicInteger index = new AtomicInteger(0);
        return entities.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .map(EmbeddedRaceModel::new)
                .collect(Collectors.toList());
    }

    @Override
    public EmbeddedRaceModel findOne(final Collection<Race> entities, final Integer entityId) {
        final AtomicInteger index = new AtomicInteger(0);
        return entities.stream()
                .map(race -> new RaceModel(index.incrementAndGet(), race))
                .map(EmbeddedRaceModel::new)
                .filter(model -> model.getId().equals(entityId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<EmbeddedRaceResource> toResource(final List<EmbeddedRaceModel> models, final Object... parameters) {
        final Resources<EmbeddedRaceResource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model, parameters))
                .collect(Collectors.toList()));
        resources.add(linkTo(methodOn(RaceController.class).races()).withSelfRel());
        return resources;
    }

    @Override
    public EmbeddedRaceResource toResource(final EmbeddedRaceModel model, final Object... parameters) {
        return makeResource(model,
                () -> new EmbeddedRaceResource(
                        model,
                        linkTo(methodOn(RaceController.class).races()).withRel("races"),
                        linkTo(methodOn(RootController.class).root()).withRel("index")),
                RaceController.class,
                EmbeddedRaceResource.class,
                parameters);
    }
}
