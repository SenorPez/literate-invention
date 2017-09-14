package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class EmbeddedCar2Resource extends Resource<EmbeddedCar2Model> {
    EmbeddedCar2Resource(final EmbeddedCar2Model content, final Link... links) {
        super(content, links);
    }
}
