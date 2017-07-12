package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

class EmbeddedRaceResource extends Resource<EmbeddedRaceModel> {
    EmbeddedRaceResource(final EmbeddedRaceModel content, final Link... links) {
        super(content, links);
    }

    public EmbeddedRaceResource(final EmbeddedRaceModel content, final Iterable<Link> links) {
        super(content, links);
    }
}