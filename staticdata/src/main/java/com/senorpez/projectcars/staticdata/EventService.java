package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class EventService {
    EventModel findOne(final int searchId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == searchId)
                .findFirst()
                .map(EventModel::new)
                .orElseThrow(() -> new EventNotFoundException(searchId));
    }

    List<EmbeddedEventModel> findAll() {
        return Application.EVENTS.stream()
                .map(EmbeddedEventModel::new)
                .collect(Collectors.toList());
    }
}
