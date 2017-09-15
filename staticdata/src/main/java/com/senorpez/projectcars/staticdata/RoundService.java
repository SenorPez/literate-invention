package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundService {
    RoundModel findOne(final Collection<Event> entities, final int eventId, final int roundId) {
        return entities.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getRounds().stream()
                .filter(round -> round.getId() == roundId)
                .findFirst()
                .map(RoundModel::new)
                .orElse(null);
    }

    List<RoundModel> findAll(final Collection<Event> entities, final int eventId) {
        return entities.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getRounds().stream()
                .map(RoundModel::new)
                .collect(Collectors.toList());
    }
}
