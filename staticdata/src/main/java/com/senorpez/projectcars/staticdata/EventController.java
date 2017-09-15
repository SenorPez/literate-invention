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
    private CarService carService;

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

    @RequestMapping("/{eventId}/cars")
    ResponseEntity<Resources<EmbeddedCarResource>> eventCars(@PathVariable final int eventId) {
        final List<EmbeddedCarModel> carModels = carService.findAll(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getCars());
        final Resources<EmbeddedCarResource> carResources = new Resources<>(carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(carResources);
    }

    @RequestMapping("/{eventId}/cars/{carId}")
    ResponseEntity<CarResource> eventCars(@PathVariable final int eventId, @PathVariable final int carId) {
        final CarModel carModel = carService.findOne(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElse(null)
                .getCars(), carId);
        return ResponseEntity.ok(carModel.toResource());
    }
}
