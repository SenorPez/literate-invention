package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Relation(value = "car", collectionRelation = "car")
class EmbeddedCarModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedCarModel(final Car car) {
        this.id = car.getId();
        this.name = car.getManufacturer() + " " + car.getModel();
    }

    Resource<EmbeddedCarModel> toResource() {
        final APIEmbeddedResourceAssembler<EmbeddedCarModel, EmbeddedCarResource> assembler = new APIEmbeddedResourceAssembler<>(CarController.class, EmbeddedCarResource.class, () -> new EmbeddedCarResource(this));
        return assembler.toResource(this);
    }

    Resource<EmbeddedCarModel> toResource(final int eventId) {
        final Resource<EmbeddedCarModel> resource = toResource();
        resource.add(linkTo(methodOn(EventController.class).eventCars(eventId, resource.getContent().getId())).withSelfRel());
        return resource;
    }

    private class EmbeddedCarResource extends Resource<EmbeddedCarModel> {
        private EmbeddedCarResource(final EmbeddedCarModel content, final Link... links) {
            super(content, links);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
