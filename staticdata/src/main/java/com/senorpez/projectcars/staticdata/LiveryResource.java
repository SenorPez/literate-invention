package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class LiveryResource extends Resource<LiveryModel> {
    LiveryResource(final LiveryModel content, final Link... links) {
        super(content, links);
    }

    LiveryResource(final LiveryModel content, final int carId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withRel("liveries"));
    }

    LiveryResource(final LiveryModel content, final int eventId, final int carId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId)).withSelfRel());
    }

    LiveryResource(final LiveryModel content, final int eventId, final int carId, final int liveryId, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId, liveryId)).withSelfRel());
        this.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withRel("liveries"));
        this.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId)).withRel("liveries"));
    }

    static Resources<LiveryResource> makeResources(final List<LiveryResource> resources, final int carId) {
        final Resources<LiveryResource> liveryResources = new Resources<>(resources);
        liveryResources.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withSelfRel());
        liveryResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return liveryResources;
    }

    static Resources<LiveryResource> makeResources(final List<LiveryResource> resources, final int eventId, final int carId) {
        final Resources<LiveryResource> liveryResources = new Resources<>(resources);
        liveryResources.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withSelfRel());
        liveryResources.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId)).withSelfRel());
        liveryResources.add(linkTo(methodOn(CarController.class).cars(carId)).withRel("car"));
        liveryResources.add(linkTo(methodOn(EventController.class).eventCars(eventId, carId)).withRel("car"));
        liveryResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return liveryResources;
    }
}
