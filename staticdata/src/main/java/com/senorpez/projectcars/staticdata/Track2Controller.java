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
        value = "/tracks",
        method = RequestMethod.GET,
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"
)
@RestController
public class Track2Controller {
    private final APIService apiService;
    private final Collection<Track2> tracks;

    @Autowired
    Track2Controller(final APIService apiService) {
        this.tracks = Application.TRACKS2;
        this.apiService = apiService;
    }

    Track2Controller(final APIService apiService, final Collection<Track2> tracks) {
        this.apiService = apiService;
        this.tracks = tracks;
    }

    @RequestMapping
    ResponseEntity<Resources<Track2Resource>> tracks() {
        final Collection<Track2> tracks = apiService.findAll(this.tracks);
        final Collection<Track2Model> trackModels = tracks.stream()
                .map(Track2Model::new)
                .collect(Collectors.toList());
        final Collection<Track2Resource> trackResources = trackModels.stream()
                .map(Track2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(Track2Resource.makeResources(trackResources));
    }

    @RequestMapping("/{trackId}")
    ResponseEntity<Track2Resource> tracks(@PathVariable final int trackId) {
        final Track2 track = apiService.findOne(
                this.tracks,
                findTrack -> findTrack.getId() == trackId,
                () -> new TrackNotFoundException(trackId));
        final Track2Model trackModel = new Track2Model(track);
        final Track2Resource trackResource = trackModel.toResource();
        return ResponseEntity.ok(trackResource);
    }
}
