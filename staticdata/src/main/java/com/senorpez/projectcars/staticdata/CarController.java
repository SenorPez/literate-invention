package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
class CarController {
    @Autowired
    private APIService apiService;

    CarController(final APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping
    ResponseEntity<EmbeddedCarResources> cars() {
        final Collection<Car> cars = apiService.findAll(Application.CARS);
        final Collection<EmbeddedCarModel> carModels = cars.stream()
                .map(EmbeddedCarModel::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedCarModel>> carResources = carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCarResources(carResources));
    }

    @RequestMapping("/{carId}")
    ResponseEntity<CarResource> cars(@PathVariable final int carId) {
        final Car car = apiService.findOne(
                Application.CARS,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarModel carModel = new CarModel(car);
        final CarResource carResource = carModel.toResource();
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{carId}/class")
    ResponseEntity<CarClassResource> carClass(@PathVariable final int carId) {
        final Car car = apiService.findOne(
                Application.CARS,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarClassModel carClassModel = new CarClassModel(car.getCarClass());
        final CarClassResource carClassResource = carClassModel.toResource(carId);
        return ResponseEntity.ok(carClassResource);
    }
}
