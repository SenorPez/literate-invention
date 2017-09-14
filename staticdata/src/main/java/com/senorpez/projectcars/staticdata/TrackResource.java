package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class TrackResource extends Resource<TrackModel> {
    TrackResource(final TrackModel content, final Link... links) {
        super(content, links);
    }
}
