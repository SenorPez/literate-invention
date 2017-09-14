package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EmbeddedCarResource extends Resource<EmbeddedCarModel> {
    EmbeddedCarResource(final EmbeddedCarModel content, final Link... links) {
        super(content, links);
    }
}
