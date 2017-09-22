package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EmbeddedTrackResources extends APIResources<EmbeddedTrackModel> {
    EmbeddedTrackResources(final Iterable<Resource<EmbeddedTrackModel>> content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(TrackController.class).tracks()).withSelfRel());
    }
}
