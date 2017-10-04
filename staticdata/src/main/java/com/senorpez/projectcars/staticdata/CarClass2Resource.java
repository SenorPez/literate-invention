package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class CarClass2Resource extends Resource<CarClass2Model> {
    CarClass2Resource(final CarClass2Model content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(CarClass2Controller.class).carClasses()).withRel("classes"));
    }

    CarClass2Resource(final CarClass2Model content, final int carId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(Car2Controller.class).carClass(carId)).withSelfRel());
        this.add(linkTo(methodOn(Car2Controller.class).cars(carId)).withRel("car"));
        this.add(linkTo(methodOn(CarClass2Controller.class).carClasses()).withRel("classes"));
    }
}
