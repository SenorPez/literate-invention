package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/events",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class EventController {
    @Autowired
    private EventService eventService;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedEventResource>> events() {
        final List<EmbeddedEventModel> eventModels = eventService.findAll(Application.EVENTS);
        final Resources<EmbeddedEventResource> eventResources = new Resources<>(eventModels.stream()
                .map(EmbeddedEventModel::toResource)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(eventResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<EventResource> events(@PathVariable final int id) {
        final EventModel eventModel = eventService.findOne(Application.EVENTS, id);
        return ResponseEntity.ok(eventModel.toResource());
    }
}
