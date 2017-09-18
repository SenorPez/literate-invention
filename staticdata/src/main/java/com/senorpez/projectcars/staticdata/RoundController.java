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
        value = "/events/{eventId}/rounds",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class RoundController {
    @Autowired
    private RoundService roundService;

    @Autowired
    private TrackService trackService;

    @RequestMapping
    ResponseEntity<Resources<RoundResource>> rounds(@PathVariable final int eventId) {
        final List<RoundModel> roundModels = roundService.findAll(eventId);
        final List<RoundResource> roundResources = roundModels.stream()
                .map(roundModel -> roundModel.toResource(eventId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(RoundResource.makeResources(roundResources, eventId));
    }

    @RequestMapping("/{roundId}")
    ResponseEntity<RoundResource> rounds(@PathVariable final int eventId, @PathVariable final int roundId) {
        final RoundModel roundModel = roundService.findOne(eventId, roundId);
        final RoundResource roundResource = roundModel.toResource(eventId);
        return ResponseEntity.ok(roundResource);
    }

    @RequestMapping("/{roundId}/track")
    ResponseEntity<TrackResource> roundTrack(@PathVariable final int eventId, @PathVariable final int roundId) {
        final TrackResource trackResource = new TrackResource(eventId, roundId);
        return ResponseEntity.ok(trackResource);
    }
}
