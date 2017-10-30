package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EmbeddedEventResources extends APIResources<EmbeddedEventModel> {
    EmbeddedEventResources(final Iterable<Resource<EmbeddedEventModel>> content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).events()).withSelfRel());
    }
}
