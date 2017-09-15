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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
        final Resources<RoundResource> roundResources = new Resources<>(roundModels.stream()
                .map(roundModel -> roundModel.toResource(eventId))
                .collect(Collectors.toList()));
        roundResources.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withSelfRel());
        roundResources.add(linkTo(methodOn(EventController.class).events(eventId)).withRel("event"));
        roundResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(roundResources);
    }

    @RequestMapping("/{roundId}")
    ResponseEntity<RoundResource> rounds(@PathVariable final int eventId, @PathVariable final int roundId) {
        final Track track = Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getTrack();
        final RoundModel roundModel = roundService.findOne(eventId, roundId);
        final RoundResource roundResource = roundModel.toResource(eventId);
        roundResource.add(linkTo(methodOn(RoundController.class).rounds(eventId)).withRel("rounds"));
        roundResource.add(linkTo(methodOn(RaceController.class).races(eventId, roundId)).withRel("races"));
        roundResource.add(linkTo(methodOn(TrackController.class).tracks(track.getId())).withRel("track"));
        roundResource.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, roundId)).withRel("track"));
        roundResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(roundResource);
    }

    @RequestMapping("/{roundId}/track")
    ResponseEntity<TrackResource> roundTrack(@PathVariable final int eventId, @PathVariable final int roundId) {
        final TrackModel trackModel = new TrackModel(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getTrack());
        final TrackResource trackResource = trackModel.toResource();
        trackResource.add(linkTo(methodOn(RoundController.class).roundTrack(eventId, roundId)).withSelfRel());
        trackResource.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
        trackResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(trackResource);
    }
}
