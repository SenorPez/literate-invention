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
        value = "/events/{eventId}/rounds",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class RoundController {
    @Autowired
    private final APIService apiService;

    RoundController(final APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping
    ResponseEntity<Resources<RoundResource>> rounds(@PathVariable final int eventId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Collection<Round> rounds = event.getRounds();
        final Collection<RoundModel> roundModels = rounds.stream()
                .map(RoundModel::new)
                .collect(Collectors.toList());
        final Collection<RoundResource> roundResources = roundModels.stream()
                .map(roundModel -> roundModel.toResource(eventId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(RoundResource.makeResources(roundResources, eventId));
    }

    @RequestMapping("/{roundId}")
    ResponseEntity<RoundResource> rounds(@PathVariable final int eventId, @PathVariable final int roundId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Round round = apiService.findOne(
                event.getRounds(),
                findRound -> findRound.getId() == roundId,
                () -> new RoundNotFoundException(roundId));
        final RoundModel roundModel = new RoundModel(round);
        final RoundResource roundResource = roundModel.toResource(eventId);
        return ResponseEntity.ok(roundResource);
    }

    @RequestMapping("/{roundId}/track")
    ResponseEntity<TrackResource> roundTrack(@PathVariable final int eventId, @PathVariable final int roundId) {
        final TrackResource trackResource = new TrackResource(eventId, roundId);
        return ResponseEntity.ok(trackResource);
    }
}
