package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/events/{eventId}/rounds/{roundId}/races",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class RaceController {
    @Autowired
    private RaceService raceService;

    @RequestMapping
    ResponseEntity<Resources<RaceResource>> races(@PathVariable final int eventId, @PathVariable final int roundId) {
        final List<RaceModel> raceModels = raceService.findAll(eventId, roundId);
        final List<RaceResource> raceResources = raceModels.stream()
                .map(raceModel -> raceModel.toResource(eventId, roundId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(RaceResource.makeResources(raceResources, eventId, roundId));
    }

    @RequestMapping("/{raceId}")
    ResponseEntity<RaceResource> races(@PathVariable final int eventId, @PathVariable final int roundId, @PathVariable final int raceId) {
        final RaceModel raceModel = raceService.findOne(eventId, roundId, raceId);
        final RaceResource raceResource = raceModel.toResource(eventId, roundId);
        return ResponseEntity.ok(raceResource);
    }
}
