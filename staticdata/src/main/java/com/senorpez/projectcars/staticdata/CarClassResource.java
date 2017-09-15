package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class CarClassResource extends Resource<CarClassModel> {
    CarClassResource(final CarClassModel content, final Link... links) {
        super(content, links);
    }
}
