package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class Car2Resource extends Resource<Car2Model> {
    Car2Resource(final Car2Model content, final Link... links) {
        super(content, links);
    }
}
