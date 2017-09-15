package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TrackService {
    TrackModel findOne(final int searchId) {
        return Application.TRACKS.stream()
                .filter(track -> track.getId() == searchId)
                .findFirst()
                .map(TrackModel::new)
                .orElseThrow(() -> new TrackNotFoundException(searchId));
    }

    List<EmbeddedTrackModel> findAll() {
        return Application.TRACKS.stream()
                .map(EmbeddedTrackModel::new)
                .collect(Collectors.toList());
    }
}
