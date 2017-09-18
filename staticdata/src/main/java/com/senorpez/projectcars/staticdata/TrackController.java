package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    ResponseEntity<EmbeddedTrackResources> tracks() {
        final List<EmbeddedTrackModel> trackModels = trackService.findAll();
        final List<Resource<EmbeddedTrackModel>> trackResources = trackModels.stream()
                .map(EmbeddedTrackModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedTrackResources(trackResources));
    }

    @RequestMapping("/{id}")
    ResponseEntity<TrackResource> tracks(@PathVariable final int id) {
        final TrackModel trackModel = trackService.findOne(id);
        final TrackResource trackResource = trackModel.toResource();
        return ResponseEntity.ok(trackResource);
    }
}
