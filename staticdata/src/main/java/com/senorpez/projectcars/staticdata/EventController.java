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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
    ResponseEntity<Resources<EmbeddedEventResource>> events() {
        final List<EmbeddedEventModel> eventModels = eventService.findAll();
        final Resources<EmbeddedEventResource> eventResources = new Resources<>(eventModels.stream()
                .map(EmbeddedEventModel::toResource)
                .collect(Collectors.toList()));
        eventResources.add(linkTo(methodOn(EventController.class).events()).withSelfRel());
        eventResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(eventResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<EventResource> events(@PathVariable final int id) {
        final EventModel eventModel = eventService.findOne(id);
        final EventResource eventResource = eventModel.toResource();
        eventResource.add(linkTo(methodOn(EventController.class).events()).withRel("events"));
        eventResource.add(linkTo(methodOn(EventController.class).eventCars(id)).withRel("cars"));
        eventResource.add(linkTo(methodOn(RoundController.class).rounds(id)).withRel("rounds"));
        eventResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(eventResource);
    }

    @RequestMapping("/{eventId}/cars")
    ResponseEntity<Resources<EmbeddedCarResource>> eventCars(@PathVariable final int eventId) {
        final List<EmbeddedCarModel> carModels = carService.findAll(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars());
        final Resources<EmbeddedCarResource> carResources = new Resources<>(carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList()));
        carResources.forEach(embeddedCarResource -> embeddedCarResource.add(linkTo(methodOn(EventController.class).eventCars(eventId, embeddedCarResource.getContent().getId())).withSelfRel()));
        carResources.add(linkTo(methodOn(EventController.class).eventCars(eventId)).withSelfRel());
        carResources.add(linkTo(methodOn(EventController.class).events(eventId)).withRel("event"));
        carResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carResources);
    }

    @RequestMapping("/{eventId}/cars/{carId}")
    ResponseEntity<CarResource> eventCars(@PathVariable final int eventId, @PathVariable final int carId) {
        final CarModel carModel = carService.findOne(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars(), carId);
        final CarResource carResource = carModel.toResource();
        carResource.add(linkTo(methodOn(EventController.class).eventCars(eventId, carId)).withSelfRel());
        carResource.add(linkTo(methodOn(EventController.class).eventCars(eventId)).withRel("cars"));
        carResource.add(linkTo(methodOn(CarClassController.class).carClasses(carModel.getCarClassId())).withRel("class"));
        carResource.add(linkTo(methodOn(EventController.class).eventCarClass(eventId, carId)).withRel("class"));
        carResource.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withRel("liveries"));
        carResource.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId)).withRel("liveries"));
        carResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/class")
    ResponseEntity<CarClassResource> eventCarClass(@PathVariable final int eventId, @PathVariable final int carId) {
        final CarModel carModel = carService.findOne(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars(), carId);
        final CarClassResource carClassResource = new CarClassModel(Application.CARS.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(carId))
                .getCarClass()).toResource();
        carClassResource.add(linkTo(methodOn(EventController.class).eventCarClass(eventId, carId)).withSelfRel());
        carClassResource.add(linkTo(methodOn(CarController.class).carClass(carId)).withSelfRel());
        carClassResource.add(linkTo(methodOn(CarController.class).cars(carId)).withRel("car"));
        carClassResource.add(linkTo(methodOn(EventController.class).eventCars(eventId, carId)).withRel("car"));
        carClassResource.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
        carClassResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carClassResource);
    }

    @RequestMapping("/{eventId}/cars/{carId}/liveries")
    ResponseEntity<Resources<LiveryResource>> eventCarLiveries(@PathVariable final int eventId, @PathVariable final int carId) {
        final List<LiveryModel> liveryModels = liveryService.findAll(Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars(), carId);
        final Resources<LiveryResource> liveryResources = new Resources<>(liveryModels.stream()
                .map(liveryModel -> liveryModel.toResource(carId))
                .collect(Collectors.toList()));
        liveryResources.add(linkTo(methodOn(LiveryController.class).liveries(carId)).withSelfRel());
        liveryResources.add(linkTo(methodOn(EventController.class).eventCarLiveries(eventId, carId)).withSelfRel());
        liveryResources.add(linkTo(methodOn(CarController.class).cars(carId)).withRel("car"));
        liveryResources.add(linkTo(methodOn(EventController.class).eventCars(eventId, carId)).withRel("car"));
        liveryResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(liveryResources);
    }
}
