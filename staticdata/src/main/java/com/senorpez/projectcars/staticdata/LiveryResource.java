package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class LiveryResource extends Resource<LiveryModel> {
    LiveryResource(final LiveryModel content, final Link... links) {
        super(content, links);
    }
}
