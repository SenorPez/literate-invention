package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
final public class RaceService implements GenericService<Race, EmbeddedRaceModel> {
    private final EmbeddedRaceResourceAssembler assembler = new EmbeddedRaceResourceAssembler(RaceController.class, EmbeddedRaceResource.class);

    @Override
    public Resources<Resource<EmbeddedRaceModel>> findAll() {
        final List<Race> races = Application.RACES;
        return new Resources<>(convertToModels(races),
                linkTo(methodOn(RaceController.class).races()).withSelfRel(),
                linkTo(methodOn(RootController.class).root()).withRel("index"));
    }

    @Override
    public List<Resource<EmbeddedRaceModel>> convertToModels(final List<Race> entities) {
        final AtomicInteger index = new AtomicInteger(0);
        return entities.stream()
                .map(race -> convertToModel(index.incrementAndGet(), race))
                .collect(Collectors.toList());
    }

    @Override
    public Resource<EmbeddedRaceModel> convertToModel(final int index, final Race entity) {
        return assembler.toResource(new EmbeddedRaceModel(index, entity));
    }
}
