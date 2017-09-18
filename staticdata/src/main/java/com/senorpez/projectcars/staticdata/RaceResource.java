package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class RaceResource extends Resource<RaceModel> {
    RaceResource(final RaceModel content, final int eventId, final int roundId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(RaceController.class).races(eventId, roundId)).withRel("races"));
    }

    static Resources<RaceResource> makeResources(final Collection<RaceResource> resources, final int eventId, final int roundId) {
        final Resources<RaceResource> raceResources = new Resources<>(resources);
        raceResources.add(linkTo(methodOn(RaceController.class).races(eventId, roundId)).withSelfRel());
        raceResources.add(linkTo(methodOn(RoundController.class).rounds(eventId, roundId)).withRel("round"));
        raceResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return raceResources;
    }
}
