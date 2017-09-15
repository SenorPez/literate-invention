package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class RoundResource extends Resource<RoundModel> {
    RoundResource(final RoundModel content, final Link... links) {
        super(content, links);
    }
}
