package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EmbeddedTrackResource extends Resource<EmbeddedTrackModel> {
    EmbeddedTrackResource(final EmbeddedTrackModel content, final Link... links) {
        super(content, links);
    }
}
