package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    EventModel findOne(final Collection<Event> entities, final int searchId) {
        return entities.stream()
                .filter(event -> event.getId() == searchId)
                .findFirst()
                .map(EventModel::new)
                .orElse(null);
    }

    List<EmbeddedEventModel> findAll(final Collection<Event> entities) {
        return entities.stream()
                .map(EmbeddedEventModel::new)
                .collect(Collectors.toList());
    }
}
