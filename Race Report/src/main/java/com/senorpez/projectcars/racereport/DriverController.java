package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(
        value = "/races/{raceId}/drivers",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
class DriverController {
    private final RaceService raceService;
    private final EmbeddedDriverService embeddedDriverService;
    private final DriverService driverService;

    @Autowired
    DriverController(final RaceService raceService, final EmbeddedDriverService embeddedDriverService, final DriverService driverService) {
        this.raceService = raceService;
        this.embeddedDriverService = embeddedDriverService;
        this.driverService = driverService;
    }

    @RequestMapping
    Resources<EmbeddedDriverResource> drivers(@PathVariable final int raceId) {
        final RaceModel raceModel = raceService.findOneModel(raceId);
        final List<EmbeddedDriverModel> driverModels = embeddedDriverService.findAll(raceModel.getDrivers());
        return embeddedDriverService.toResource(driverModels, raceId);
    }

    @RequestMapping(value = "/{driverId}")
    DriverResource driver(@PathVariable final int raceId, @PathVariable final int driverId) {
        final RaceModel raceModel = raceService.findOneModel(raceId);
        final DriverModel driverModel = driverService.findOne(
                raceModel.getDrivers(),
                driverId);
        return driverService.toResource(driverModel,
                raceId,
                driverId);
    }
}
