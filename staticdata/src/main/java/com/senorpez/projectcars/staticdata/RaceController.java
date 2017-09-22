package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/events/{eventId}/rounds/{roundId}/races",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class RaceController {
    @Autowired
    private final APIService apiService;

    RaceController(final APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping
    ResponseEntity<Resources<RaceResource>> races(@PathVariable final int eventId, @PathVariable final int roundId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Round round = apiService.findOne(
                event.getRounds(),
                findRound -> findRound.getId() == roundId,
                () -> new RoundNotFoundException(roundId));
        final Collection<Race> races = round.getRaces();
        final Collection<RaceModel> raceModels = races.stream()
                .map(RaceModel::new)
                .collect(Collectors.toList());
        final Collection<RaceResource> raceResources = raceModels.stream()
                .map(raceModel -> raceModel.toResource(eventId, roundId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(RaceResource.makeResources(raceResources, eventId, roundId));
    }

    @RequestMapping("/{raceId}")
    ResponseEntity<RaceResource> races(@PathVariable final int eventId, @PathVariable final int roundId, @PathVariable final int raceId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Round round = apiService.findOne(
                event.getRounds(),
                findRound -> findRound.getId() == roundId,
                () -> new RoundNotFoundException(roundId));
        final Race race = apiService.findOne(
                round.getRaces(),
                findRace -> findRace.getId() == raceId,
                () -> new RaceNotFoundException(raceId));
        final RaceModel raceModel = new RaceModel(race);
        final RaceResource raceResource = raceModel.toResource(eventId, roundId);
        return ResponseEntity.ok(raceResource);
    }
}
