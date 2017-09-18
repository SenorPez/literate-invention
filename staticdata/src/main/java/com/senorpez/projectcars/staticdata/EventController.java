package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/events",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class EventController {
    @Autowired
    private APIService apiService;

    @RequestMapping
    ResponseEntity<EmbeddedEventResources> events() {
        final Collection<Event> events = Application.EVENTS;
        final Collection<EmbeddedEventModel> eventModels = events.stream()
                .map(EmbeddedEventModel::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedEventModel>> eventResources = eventModels.stream()
                .map(EmbeddedEventModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedEventResources(eventResources));
    }

    @RequestMapping("/{eventId}")
    ResponseEntity<EventResource> events(@PathVariable final int eventId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final EventModel eventModel = new EventModel(event);
        final EventResource eventResource = eventModel.toResource();
        return ResponseEntity.ok(eventResource);
    }

    @RequestMapping("/{eventId}/cars")
    ResponseEntity<Resources<Resource<EmbeddedCarModel>>> eventCars(@PathVariable final int eventId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Collection<Car> cars = event.getCars();
        final Collection<EmbeddedCarModel> carModels = cars.stream()
                .map(EmbeddedCarModel::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedCarModel>> carResources = carModels.stream()
                .map(car -> car.toResource(eventId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCarResources(carResources, eventId));
    }

    @RequestMapping("/{eventId}/cars/{carId}")
    ResponseEntity<CarResource> eventCars(@PathVariable final int eventId, @PathVariable final int carId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Car car = apiService.findOne(
                event.getCars(),
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarModel carModel = new CarModel(car);
        final CarResource carResource = carModel.toResource(eventId);
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/class")
    ResponseEntity<CarClassResource> eventCarClass(@PathVariable final int eventId, @PathVariable final int carId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Car car = apiService.findOne(
                event.getCars(),
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarClassModel carClassModel = new CarClassModel(car.getCarClass());
        final CarClassResource carClassResource = carClassModel.toResource(eventId, carId);
        return ResponseEntity.ok(carClassResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/liveries")
    ResponseEntity<Resources<LiveryResource>> eventCarLiveries(@PathVariable final int eventId, @PathVariable final int carId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Car car = apiService.findOne(
                event.getCars(),
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Collection<Livery> liveries = car.getLiveries();
        final Collection<LiveryModel> liveryModels = liveries.stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
        final Collection<LiveryResource> liveryResources = liveryModels.stream()
                .map(liveryModel -> liveryModel.toResource(eventId, carId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(LiveryResource.makeResources(liveryResources, eventId, carId));
    }

    @RequestMapping("/{eventId}/cars/{carId}/liveries/{liveryId}")
    ResponseEntity<LiveryResource> eventCarLiveries(@PathVariable final int eventId, @PathVariable final int carId, @PathVariable final int liveryId) {
        final Event event = apiService.findOne(
                Application.EVENTS,
                findEvent -> findEvent.getId() == eventId,
                () -> new EventNotFoundException(eventId));
        final Car car = apiService.findOne(
                event.getCars(),
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Livery livery = apiService.findOne(
                car.getLiveries(),
                findLivery -> findLivery.getId() == liveryId,
                () -> new LiveryNotFoundException(liveryId));
        final LiveryModel liveryModel = new LiveryModel(livery);
        final LiveryResource liveryResource = liveryModel.toResource(carId);
        return ResponseEntity.ok(liveryResource);
    }
}
