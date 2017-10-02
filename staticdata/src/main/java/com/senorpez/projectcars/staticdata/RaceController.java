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

import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequestMapping(
        value = "/events/{eventId}/rounds/{roundId}/races",
        method = RequestMethod.GET,
        produces = {PROJECT_CARS_VALUE, APPLICATION_JSON_UTF8_VALUE}
)
@RestController
public class RaceController {
    private final APIService apiService;
    private final Collection<Event> events;

    @Autowired
    RaceController(final APIService apiService) {
        this.apiService = apiService;
        this.events = Application.EVENTS;
    }

    RaceController(final APIService apiService, final Collection<Event> events) {
        this.apiService = apiService;
        this.events = events;
    }

    @RequestMapping
    ResponseEntity<Resources<RaceResource>> races(@PathVariable final int eventId, @PathVariable final int roundId) {
        final Event event = apiService.findOne(
                this.events,
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
                this.events,
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
