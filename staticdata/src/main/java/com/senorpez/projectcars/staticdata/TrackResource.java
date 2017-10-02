package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class TrackResource extends Resource<TrackModel> {
    TrackResource(final TrackModel content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
    }

    TrackResource(final int eventId, final int roundId, final Collection<Event> events, final Link... links) {
        this(new TrackModel(events.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getTrack()), links);
        this.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, roundId)).withSelfRel());
        this.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        this.add(linkTo(methodOn(RoundController.class).rounds(eventId, roundId)).withRel("round"));
        this.add(linkTo(methodOn(TrackController.class).tracks(this.getContent().getId())).withSelfRel());
    }
}
