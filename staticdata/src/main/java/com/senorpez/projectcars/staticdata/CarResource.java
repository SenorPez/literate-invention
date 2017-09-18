package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class CarResource extends Resource<CarModel> {
    CarResource(final CarModel content, final Link... links) {
        super(content, links);
    }
}
