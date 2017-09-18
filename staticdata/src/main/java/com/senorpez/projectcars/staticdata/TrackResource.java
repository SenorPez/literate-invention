package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class TrackResource extends Resource<TrackModel> {
    TrackResource(final TrackModel content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
    }

    TrackResource(final int eventId, final int roundId, final Link... links) {
        this(new TrackModel(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getTrack()), links);
        this.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, roundId)).withSelfRel());
        this.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
    }
}
