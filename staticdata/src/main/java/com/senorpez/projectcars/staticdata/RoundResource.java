package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class RoundResource extends Resource<RoundModel> {
    RoundResource(final RoundModel content, final int eventId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withRel("event"));
    }

    RoundResource(final RoundModel content, final int eventId, final int roundId, final Link... links) {
        super(content, links);

        final Track track = Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getTrack();
        this.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withRel("rounds"));
        this.add(linkTo(methodOn(RaceController.class).races(eventId, roundId)).withRel("races"));
        this.add(linkTo(methodOn(TrackController.class).tracks(track.getId())).withRel("track"));
        this.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, roundId)).withRel("track"));
    }

    static Resources<RoundResource> makeResources(final List<RoundResource> resources, final int eventId) {
        final Resources<RoundResource> roundResources = new Resources<>(resources);
        roundResources.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withSelfRel());
        roundResources.add(linkTo(methodOn(EventController.class).events(eventId)).withRel("event"));
        roundResources.add(linkTo(methodOn(RootController.class)).withRel("index"));
        return roundResources;
    }
}
