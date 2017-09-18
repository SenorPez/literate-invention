package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class RoundService {
    RoundModel findOne(final int eventId, final int roundId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .map(RoundModel::new)
                .orElseThrow(() -> new RoundNotFoundException(roundId));
    }

    List<RoundModel> findAll(final int eventId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getRounds().stream()
                .map(RoundModel::new)
                .collect(Collectors.toList());
    }
}
