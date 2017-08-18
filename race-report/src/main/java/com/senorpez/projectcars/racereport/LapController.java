package com.senorpez.projectcars.racereport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: 07/17/17 Eventually we should be able to fix the mapping.
// See: https://github.com/spring-projects/spring-hateoas/pull/591
// See: https://github.com/spring-projects/spring-hateoas/issues/571

@RequestMapping(
        value = "/races/{raceId}/laps",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
)
@RestController
class LapController {
    private final LapService lapService;
    private final RaceService raceService;

    @Autowired
    LapController(final LapService lapService, final RaceService raceService) {
        this.lapService = lapService;
        this.raceService = raceService;
    }

    @RequestMapping
    Resources<LapResource> laps(@PathVariable final int raceId) {
        final RaceModel raceModel = raceService.findOne(Application.RACES, raceId);
        final List<LapModel> lapModels = lapService.findAll(IntStream
                .rangeClosed(1, raceModel.getCurrentLapNumber())
                .boxed()
                .collect(Collectors.toList()));
        return lapService.toResource(lapModels, raceId);
    }

    @RequestMapping(value = "/{lapId}")
    LapResource laps(@PathVariable final Integer raceId, @PathVariable final Integer lapId) {
        final RaceModel raceModel = raceService.findOne(Application.RACES, raceId);
        final LapModel lapModel = lapService.findOne(IntStream
                .rangeClosed(1, raceModel.getCurrentLapNumber())
                .boxed()
                .collect(Collectors.toList()), lapId);
        return lapService.toResource(lapModel,
                raceId,
                lapId);
    }
}