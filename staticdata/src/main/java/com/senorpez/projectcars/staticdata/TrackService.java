package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {
    TrackModel findOne(final Collection<Track> entities, final int searchId) {
        return entities.stream()
                .filter(track -> track.getId() == searchId)
                .findFirst()
                .map(TrackModel::new)
                .orElse(null);
    }

    List<EmbeddedTrackModel> findAll(final Collection<Track> entities) {
        return entities.stream()
                .map(EmbeddedTrackModel::new)
                .collect(Collectors.toList());
    }
}
