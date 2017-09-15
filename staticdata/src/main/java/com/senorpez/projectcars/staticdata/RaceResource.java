package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class RaceResource extends Resource<RaceModel> {
    RaceResource(final RaceModel content, final Link... links) {
        super(content, links);
    }
}
