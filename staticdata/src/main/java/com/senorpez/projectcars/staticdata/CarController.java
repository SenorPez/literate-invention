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
        value = "/cars",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    private CarClassService carClassService;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedCarResource>> cars() {
        final List<EmbeddedCarModel> carModels = carService.findAll(Application.CARS);
        final Resources<EmbeddedCarResource> carResources = new Resources<>(carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList()));
        carResources.add(linkTo(methodOn(CarController.class).cars()).withSelfRel());
        carResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<CarResource> cars(@PathVariable final int id) {
        final CarModel carModel = carService.findOne(Application.CARS, id);
        final CarResource carResource = carModel.toResource();
        carResource.add(linkTo(methodOn(CarController.class).cars()).withRel("cars"));
        carResource.add(linkTo(methodOn(CarClassController.class).carClasses(carModel.getCarClassId())).withRel("class"));
        carResource.add(linkTo(methodOn(CarController.class).carClass(id)).withRel("class"));
        carResource.add(linkTo(methodOn(LiveryController.class).liveries(id)).withRel("liveries"));
        carResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{id}/class")
    ResponseEntity<CarClassResource> carClass(@PathVariable final int id) {
        final CarModel carModel = carService.findOne(Application.CARS, id);
        final CarClassResource carClassResource = new CarClassModel(Application.CARS.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .orElse(null)
                .getCarClass()).toResource();
        carClassResource.add(linkTo(methodOn(CarController.class).carClass(carModel.getCarClassId())).withSelfRel());
        carClassResource.add(linkTo(methodOn(CarController.class).cars(id)).withRel("car"));
        carClassResource.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
        carClassResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carClassResource);

    }
}
