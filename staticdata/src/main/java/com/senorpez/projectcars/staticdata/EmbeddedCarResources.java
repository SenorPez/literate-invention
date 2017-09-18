package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EmbeddedCarResources extends APIResources<EmbeddedCarModel> {
    EmbeddedCarResources(final Iterable<Resource<EmbeddedCarModel>> content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarController.class).cars()).withSelfRel());
    }

    EmbeddedCarResources(final Iterable<Resource<EmbeddedCarModel>> content, final int eventId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).eventCars(eventId)).withSelfRel());
        this.add(linkTo(methodOn(EventController.class).events(eventId)).withRel("events"));
    }

}