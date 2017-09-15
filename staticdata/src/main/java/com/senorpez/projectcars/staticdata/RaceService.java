package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class RaceService {
    RaceModel findOne(final int eventId, final int roundId, final int raceId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getRaces().stream()
                .filter(race -> race.getId() == raceId)
                .findFirst()
                .map(RaceModel::new)
                .orElseThrow(() -> new RaceNotFoundException(raceId));
    }

    List<RaceModel> findAll(final int eventId, final int roundId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .orElseThrow(() -> new RoundNotFoundException(roundId))
                .getRaces().stream()
                .map(RaceModel::new)
                .collect(Collectors.toList());
    }
}
