package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EmbeddedCar2Resources extends APIResources<EmbeddedCar2Model> {
    EmbeddedCar2Resources(final Iterable<Resource<EmbeddedCar2Model>> content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(Car2Controller.class).cars()).withSelfRel());
    }
}
