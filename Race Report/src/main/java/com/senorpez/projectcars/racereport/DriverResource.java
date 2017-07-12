package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class DriverResource extends Resource<DriverModel> {
    DriverResource(final DriverModel content, final Link... links) {
        super(content, links);
    }
}
