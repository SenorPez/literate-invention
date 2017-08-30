package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EmbeddedCar2Resource extends Resource<EmbeddedCar2Model> {
    public EmbeddedCar2Resource(EmbeddedCar2Model content, Link... links) {
        super(content, links);
    }
}
