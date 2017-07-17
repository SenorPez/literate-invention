package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class LapService implements NewServiceInterface<
        LapModel,
        LapResource,
        Integer, Integer> {
    @Override
    public List<LapModel> findAll(final Collection<Integer> entities) {
        return entities.stream()
                .map(LapModel::new)
                .collect(Collectors.toList());
    }

    @Override
    public LapModel findOne(final Collection<Integer> entities, final Integer lapId) {
        return entities.stream()
                .map(LapModel::new)
                .filter(model -> model.getId().equals(lapId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Error."));
    }

    @Override
    public Resources<LapResource> toResource(final List<LapModel> models, final Object... parameters) {
        final Resources<LapResource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model, parameters))
                .collect(Collectors.toList()));
        resources.add(linkTo(methodOn(LapController.class).laps((int) parameters[0])).withSelfRel());
        return resources;
    }

    @Override
    public LapResource toResource(final LapModel model, final Object... parameters) {
        return makeResource(model,
                () -> new LapResource(
                        model,
                        linkTo(methodOn(LapController.class).laps((int) parameters[0])).withRel("laps"),
                        linkTo(methodOn(RootController.class).root()).withRel("index")),
                LapController.class,
                LapResource.class,
                parameters);
    }
}
