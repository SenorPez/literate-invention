package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping(
        value = "/",
        method = RequestMethod.GET
)
@RestController
public class RootController {
    @RequestMapping(produces = {PROJECT_CARS_VALUE, FALLBACK_VALUE})
    ResponseEntity<ResourceSupport> root() {
        final ResourceSupport root = new ResourceSupport();
        root.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        root.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        root.add(linkTo(methodOn(CarController.class).cars()).withRel("cars"));
        root.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
        root.add(linkTo(methodOn(EventController.class).events()).withRel("events"));
        root.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
        return ResponseEntity.ok(root);
    }

    @RequestMapping(produces = PROJECT_CARS_2_VALUE)
    ResponseEntity<ResourceSupport> root2() {
        final ResourceSupport root = new ResourceSupport();
        root.add(linkTo(methodOn(RootController.class).root2()).withSelfRel());
        root.add(linkTo(methodOn(Car2Controller.class).cars()).withRel("cars"));
        return ResponseEntity.ok(root);
    }
}
