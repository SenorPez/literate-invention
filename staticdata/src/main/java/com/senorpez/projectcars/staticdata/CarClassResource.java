package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;

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

    static Resources<CarClassResource> makeResources(final Collection<CarClassResource> resources) {
        resources.forEach(ResourceSupport::removeLinks);
        resources.forEach(carClassResource -> carClassResource.add(linkTo(methodOn(CarClassController.class).carClasses(carClassResource.getContent().getId())).withSelfRel()));
        final Resources<CarClassResource> carClassResources = new Resources<>(resources);
        carClassResources.add(linkTo(methodOn(CarClassController.class).carClasses()).withSelfRel());
        carClassResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return carClassResources;
    }
}
