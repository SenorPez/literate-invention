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

    @RequestMapping
    ResponseEntity<Resources<RoundResource>> rounds(@PathVariable final int eventId) {
        final List<RoundModel> roundModels = roundService.findAll(Application.EVENTS, eventId);
        final Resources<RoundResource> roundResources = new Resources<>(roundModels.stream()
                .map(roundModel -> roundModel.toResource(eventId))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(roundResources);
    }

    @RequestMapping("/{roundId}")
    ResponseEntity<RoundResource> rounds(@PathVariable final int eventId, @PathVariable final int roundId) {
        final RoundModel roundModel = roundService.findOne(Application.EVENTS, eventId, roundId);
        return ResponseEntity.ok(roundModel.toResource(eventId));
    }
}
