package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        value = "/races",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
class RaceController {
    private final RaceService service;

    @Autowired
    RaceController(final RaceService service) {
        this.service = service;
    }

    @RequestMapping
    Resources<EmbeddedRaceResource> races() {
        return service.findAll();
    }

    @RequestMapping(value = "/{raceId}")
    RaceResource race(@PathVariable final int raceId) {
        return service.findOne(raceId);
    }
}