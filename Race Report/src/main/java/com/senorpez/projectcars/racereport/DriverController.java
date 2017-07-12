package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        value = "/races/{raceId}/drivers",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
class DriverController {
    private final DriverService service;

    @Autowired
    DriverController(final DriverService service) {
        this.service = service;
    }

    @RequestMapping
    Resources<EmbeddedDriverResource> drivers(@PathVariable final int raceId) {
        final RaceModel race = service.findParent(raceId);
        return service.findAll(race, race.getDrivers());
    }

    @RequestMapping(value = "/{driverId}")
    DriverResource driver(@PathVariable final int raceId, @PathVariable final int driverId) {
        final RaceModel race = service.findParent(raceId);
        return service.findOne(race, race.getDrivers(), driverId);
    }
}
