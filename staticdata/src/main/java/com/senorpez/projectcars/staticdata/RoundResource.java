package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class RoundResource extends Resource<RoundModel> {
    RoundResource(final RoundModel content, final int eventId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withRel("rounds"));
        this.add(linkTo(methodOn(RaceController.class).races(eventId, content.getId())).withRel("races"));
        this.add(linkTo(methodOn(TrackController.class).tracks(this.getContent().getTrackId())).withRel("track"));
        this.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, content.getId())).withRel("track"));
    }

    static Resources<RoundResource> makeResources(final Collection<RoundResource> resources, final int eventId) {
        resources.forEach(ResourceSupport::removeLinks);
        resources.forEach(roundResource -> roundResource.add(linkTo(methodOn(RoundController.class).rounds(eventId, roundResource.getContent().getId())).withSelfRel()));
        final Resources<RoundResource> roundResources = new Resources<>(resources);
        roundResources.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withSelfRel());
        roundResources.add(linkTo(methodOn(EventController.class).events(eventId)).withRel("event"));
        roundResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return roundResources;
    }
}
