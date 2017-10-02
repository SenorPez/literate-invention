package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EventResource extends Resource<EventModel> {
    EventResource(final EventModel content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).events()).withRel("events"));
        this.add(linkTo(methodOn(EventController.class).eventCars(content.getId())).withRel("cars"));
        this.add(linkTo(methodOn(RoundController.class).rounds(content.getId())).withRel("rounds"));
    }
}
