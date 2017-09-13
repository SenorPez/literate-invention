package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class LapResource extends Resource<LapModel> {
    LapResource(final LapModel content, final Link... links) {
        super(content, links);
    }
}
