package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class RaceResource extends Resource<RaceModel> {
    RaceResource(final RaceModel content, final Link... links) {
        super(content, links);
    }
}
