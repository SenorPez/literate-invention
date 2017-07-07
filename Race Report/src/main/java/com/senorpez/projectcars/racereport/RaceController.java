package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping(
        value = "/races",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
public class RaceController {
    @RequestMapping
    Resources<Resource> races() {
        IdentifiableResourceAssembler<RaceModel, Resource> assembler = new IdentifiableResourceAssembler<>(RaceController.class, Resource.class);
        final AtomicInteger index = new AtomicInteger(1);
                return new Resources<>(
                Application.RACES.stream()
                        .map(race -> new RaceModel(index.getAndIncrement(), race))
                        .map(assembler::toResource)
                        .collect(Collectors.toList()),
                linkTo(methodOn(RaceController.class).races()).withSelfRel());
    }
}
