package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class CarClassResource extends Resource<CarClassModel> {
    CarClassResource(final CarClassModel content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
    }

    CarClassResource(final CarClassModel content, final int carId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarController.class).carClass(carId)).withSelfRel());
        this.add(linkTo(methodOn(CarController.class).cars(carId)).withRel("car"));
        this.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
    }

    CarClassResource(final CarClassModel content, final int eventId, final int carId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).eventCarClass(eventId, carId)).withSelfRel());
        this.add(linkTo(methodOn(CarController.class).carClass(carId)).withSelfRel());
        this.add(linkTo(methodOn(CarController.class).cars(carId)).withRel("car"));
        this.add(linkTo(methodOn(EventController.class).eventCars(eventId, carId)).withRel("car"));
        this.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
    }
}
