package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class EmbeddedDriverResource extends Resource<EmbeddedDriverModel> {
    EmbeddedDriverResource(final EmbeddedDriverModel content, final Link... links) {
        super(content, links);
    }
}
