package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(
        value = "/races",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
class RaceController {
    private final EmbeddedRaceService embeddedRaceService;
    private final RaceService service;

    @Autowired
    RaceController(final EmbeddedRaceService embeddedRaceService, final RaceService service) {
        this.embeddedRaceService = embeddedRaceService;
        this.service = service;
    }

    @RequestMapping
    Resources<EmbeddedRaceResource> races() {
        final List<EmbeddedRaceModel> raceModels = embeddedRaceService.findAll(Application.RACES);
        return embeddedRaceService.toResource(raceModels);
    }

    @RequestMapping(value = "/{raceId}")
    RaceResource race(@PathVariable final int raceId) {
        final RaceModel raceModel = service.findOne(Application.RACES, raceId);
        return service.toResource(raceModel);
    }
}