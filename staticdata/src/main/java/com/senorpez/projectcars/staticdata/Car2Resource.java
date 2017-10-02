package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class Car2Resource extends Resource<Car2Model> {
    Car2Resource(final Car2Model content, final Link... links) {
        super(content, links);
        this.add(linkTo(methodOn(Car2Controller.class).cars()).withRel("cars"));
        this.add(linkTo(methodOn(Car2Controller.class).carClass(content.getId())).withRel("class"));
        this.add(linkTo(methodOn(CarClassController.class).carClasses2(content.getCarClassId())).withRel("class"));
        this.add(linkTo(methodOn(LiveryController.class).liveries2(content.getId())).withRel("liveries"));
    }

    static Resources<Car2Resource> makeResources(final Collection<Car2Resource> resources) {
        resources.forEach(ResourceSupport::removeLinks);
        resources.forEach(car2Resource -> car2Resource.add(linkTo(methodOn(Car2Controller.class).cars(car2Resource.getContent().getId())).withSelfRel()));
        final Resources<Car2Resource> car2Resources = new Resources<>(resources);
        car2Resources.add(linkTo(methodOn(Car2Controller.class).cars()).withSelfRel());
        car2Resources.add(linkTo(methodOn(RootController.class).root2()).withRel("index"));
        return car2Resources;
    }
}
