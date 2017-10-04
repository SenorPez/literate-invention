package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class EmbeddedCarClass2Resources extends APIResources<EmbeddedCarClass2Model> {
    EmbeddedCarClass2Resources(final Iterable<Resource<EmbeddedCarClass2Model>> content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarClass2Controller.class).carClasses()).withSelfRel());
    }
}
