package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class Track2Resource extends Resource<Track2Model> {
    Track2Resource(final Track2Model content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(Track2Controller.class).tracks()).withRel("tracks"));
    }

    static Resources<Track2Resource> makeResources(final Collection<Track2Resource> resources) {
        resources.forEach(ResourceSupport::removeLinks);
        resources.forEach(track2Resource -> track2Resource.add(linkTo(methodOn(Track2Controller.class).tracks(track2Resource.getContent().getId())).withSelfRel()));
        final Resources<Track2Resource> track2Resources = new Resources<>(resources);
        track2Resources.add(linkTo(methodOn(Track2Controller.class).tracks()).withSelfRel());
        track2Resources.add(linkTo(methodOn(RootController.class).root2()).withRel("index"));
        return track2Resources;
    }
}
