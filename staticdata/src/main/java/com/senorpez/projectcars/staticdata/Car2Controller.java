package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/cars",
        method = RequestMethod.GET,
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"

)
@RestController
public class Car2Controller {
    private final APIService apiService;
    private final Collection<Car2> cars;

    @Autowired
    Car2Controller(final APIService apiService) {
        this.apiService = apiService;
        this.cars = Application.CARS2;
    }

    Car2Controller(final APIService apiService, final Collection<Car2> cars) {
        this.apiService = apiService;
        this.cars = cars;
    }

    @RequestMapping
    ResponseEntity<Resources<Car2Resource>> cars() {
        final Collection<Car2> car2s = apiService.findAll(this.cars);
        final Collection<Car2Model> car2Models = car2s.stream()
                .map(Car2Model::new)
                .collect(Collectors.toList());
        final Collection<Car2Resource> car2Resources = car2Models.stream()
                .map(Car2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(Car2Resource.makeResources(car2Resources));
    }

    @RequestMapping("/{carId}")
    ResponseEntity<Car2Resource> cars(@PathVariable final int carId) {
        final Car2 car2 = apiService.findOne(
                this.cars,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Car2Model car2Model = new Car2Model(car2);
        final Car2Resource car2Resource = car2Model.toResource();
        return ResponseEntity.ok(car2Resource);
    }

    @RequestMapping("/{carId}/class")
    ResponseEntity<CarClassResource> carClass(@PathVariable final int carId) {
        final Car2 car = apiService.findOne(
                this.cars,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarClassModel carClassModel = new CarClassModel(car.getCarClass());
        final CarClassResource carClassResource = carClassModel.toResource(carId);
        return ResponseEntity.ok(carClassResource);
    }
}
