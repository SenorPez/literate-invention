package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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

    @Autowired
    private LiveryService liveryService;

    @RequestMapping
    ResponseEntity<EmbeddedEventResources> events() {
        final List<EmbeddedEventModel> eventModels = eventService.findAll();
        final List<Resource<EmbeddedEventModel>> eventResources = eventModels.stream()
                .map(EmbeddedEventModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedEventResources(eventResources));
    }

    @RequestMapping("/{id}")
    ResponseEntity<EventResource> events(@PathVariable final int id) {
        final EventModel eventModel = eventService.findOne(id);
        final EventResource eventResource = eventModel.toResource();
        return ResponseEntity.ok(eventResource);
    }

    @RequestMapping("/{eventId}/cars")
    ResponseEntity<Resources<Resource<EmbeddedCarModel>>> eventCars(@PathVariable final int eventId) {
        final List<EmbeddedCarModel> carModels = carService.findEventCars(eventId);
        final List<Resource<EmbeddedCarModel>> carResources = carModels.stream()
                .map(car -> car.toResource(eventId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCarResources(carResources, eventId));
    }

    @RequestMapping("/{eventId}/cars/{carId}")
    ResponseEntity<CarResource> eventCars(@PathVariable final int eventId, @PathVariable final int carId) {
        final CarModel carModel = carService.findEventCar(eventId, carId);
        final CarResource carResource = carModel.toResource(eventId);
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/class")
    ResponseEntity<CarClassResource> eventCarClass(@PathVariable final int eventId, @PathVariable final int carId) {
        final CarClassModel carClassModel = carService.findCarClass(carId);
        final CarClassResource carClassResource = carClassModel.toResource(eventId, carId);
        return ResponseEntity.ok(carClassResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/liveries")
    ResponseEntity<Resources<LiveryResource>> eventCarLiveries(@PathVariable final int eventId, @PathVariable final int carId) {
        final List<LiveryModel> liveryModels = carService.findEventCarLiveries(eventId, carId);
        final List<LiveryResource> liveryResources = liveryModels.stream()
                .map(liveryModel -> liveryModel.toResource(eventId, carId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(LiveryResource.makeResources(liveryResources, eventId, carId));
    }

    @RequestMapping("/{eventId}/cars/{carId}/liveries/{liveryId}")
    ResponseEntity<LiveryResource> eventCarLiveries(@PathVariable final int eventId, @PathVariable final int carId, @PathVariable final int liveryId) {
        final LiveryModel liveryModel = liveryService.findOne(eventId, carId, liveryId);
        final LiveryResource liveryResource = liveryModel.toResource(carId);
        return ResponseEntity.ok(liveryResource);
    }
}
