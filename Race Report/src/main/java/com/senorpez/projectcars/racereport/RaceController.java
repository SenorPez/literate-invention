package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequestMapping(
        value = "/races",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
public class RaceController {
    private final RaceService service;

    @Autowired
    RaceController(RaceService service) {
        this.service = service;
    }

    @RequestMapping
    Resources<Resource<EmbeddedRaceModel>> races() {
        return service.findAll();
    }
}
