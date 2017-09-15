package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceService {
    RaceModel findOne(final Collection<Event> entities, final int eventId, final int roundId, final int raceId) {
        return entities.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElse(null)
                .getRaces().stream()
                .filter(race -> race.getId() == raceId)
                .findFirst()
                .map(RaceModel::new)
                .orElse(null);
    }

    List<RaceModel> findAll(final Collection<Event> entities, final int eventId, final int roundId) {
        return entities.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElse(null)
                .getRaces().stream()
                .map(RaceModel::new)
                .collect(Collectors.toList());
    }
}
