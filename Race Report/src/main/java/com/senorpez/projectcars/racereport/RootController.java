package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping(
        value = "/",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
public class RootController {
    ResourceSupport root() {
        ResourceSupport root = new ResourceSupport();
        root.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        root.add(linkTo(methodOn(RaceController.class).races()).withRel("races"));
        return root;
    }
}
