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
        value = "/tracks",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class TrackController {
    @Autowired
    private TrackService trackService;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedTrackResource>> tracks() {
        final List<EmbeddedTrackModel> trackModels = trackService.findAll();
        final Resources<EmbeddedTrackResource> trackResources = new Resources<>(trackModels.stream()
                .map(EmbeddedTrackModel::toResource)
                .collect(Collectors.toList()));
        trackResources.add(linkTo(methodOn(TrackController.class).tracks()).withSelfRel());
        trackResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(trackResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<TrackResource> tracks(@PathVariable final int id) {
        final TrackModel trackModel = trackService.findOne(id);
        final TrackResource trackResource = trackModel.toResource();
        trackResource.add(linkTo(methodOn(TrackController.class).tracks()).withRel("tracks"));
        trackResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(trackResource);
    }
}
