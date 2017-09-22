package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/tracks",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class TrackController {
    @Autowired
    private final APIService apiService;

    TrackController(final APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping
    ResponseEntity<EmbeddedTrackResources> tracks() {
        final Collection<Track> tracks = apiService.findAll(Application.TRACKS);
        final Collection<EmbeddedTrackModel> trackModels = tracks.stream()
                .map(EmbeddedTrackModel::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedTrackModel>> trackResources = trackModels.stream()
                .map(EmbeddedTrackModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedTrackResources(trackResources));
    }

    @RequestMapping("/{trackId}")
    ResponseEntity<TrackResource> tracks(@PathVariable final int trackId) {
        final Track track = apiService.findOne(
                Application.TRACKS,
                findTrack -> findTrack.getId() == trackId,
                () -> new TrackNotFoundException(trackId));
        final TrackModel trackModel = new TrackModel(track);
        final TrackResource trackResource = trackModel.toResource();
        return ResponseEntity.ok(trackResource);
    }
}
