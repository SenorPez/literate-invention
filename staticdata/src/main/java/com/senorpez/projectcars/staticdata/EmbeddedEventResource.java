package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EmbeddedEventResource extends Resource<EmbeddedEventModel> {
    EmbeddedEventResource(final EmbeddedEventModel content, final Link... links) {
        super(content, links);
    }
}
