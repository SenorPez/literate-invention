package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EventResource extends Resource<EventModel> {
    EventResource(final EventModel content, final Link... links) {
        super(content, links);
    }
}
