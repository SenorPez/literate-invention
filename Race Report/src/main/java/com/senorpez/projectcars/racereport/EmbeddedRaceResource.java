package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EmbeddedRaceResource extends Resource<EmbeddedRaceModel> {
    public EmbeddedRaceResource(EmbeddedRaceModel content, Link... links) {
        super(content, links);
    }

    public EmbeddedRaceResource(EmbeddedRaceModel content, Iterable<Link> links) {
        super(content, links);
    }
}