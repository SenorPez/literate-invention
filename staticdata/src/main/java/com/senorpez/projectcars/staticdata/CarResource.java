package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class CarResource extends Resource<CarModel> {
    CarResource(final CarModel content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarController.class).cars()).withRel("cars"));
        this.add(linkTo(methodOn(CarClassController.class).carClasses(content.getCarClassId())).withRel("class"));
        this.add(linkTo(methodOn(CarController.class).carClass(content.getId())).withRel("class"));
        this.add(linkTo(methodOn(LiveryController.class).liveries(content.getId())).withRel("liveries"));
    }

    CarResource(final CarModel content, final int eventId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).eventCars(eventId, content.getId())).withSelfRel());
        this.add(linkTo(methodOn(EventController.class).eventCars(eventId)).withRel("cars"));
        this.add(linkTo(methodOn(CarClassController.class).carClasses(content.getCarClassId())).withRel("class"));
        this.add(linkTo(methodOn(CarController.class).carClass(content.getId())).withRel("class"));
        this.add(linkTo(methodOn(EventController.class).eventCarClass(eventId, content.getId())).withRel("class"));
        this.add(linkTo(methodOn(LiveryController.class).liveries(content.getId())).withRel("liveries"));
        this.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, content.getId())).withRel("liveries"));
    }
}
