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
        value = "/cars",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping
    ResponseEntity<Resources<Resource<EmbeddedCarModel>>> cars() {
        final List<EmbeddedCarModel> carModels = carService.findAll();
        final List<Resource<EmbeddedCarModel>> carResources = carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCarResources(carResources));
    }

    @RequestMapping("/{carId}")
    ResponseEntity<CarResource> cars(@PathVariable final int carId) {
        final CarModel carModel = carService.findOne(carId);
        final CarResource carResource = carModel.toResource();
        return ResponseEntity.ok(carResource);
    }

    @RequestMapping("/{carId}/class")
    ResponseEntity<CarClassResource> carClass(@PathVariable final int carId) {
        final CarClassModel carClassModel = carService.findCarClass(carId);
        final CarClassResource carClassResource = carClassModel.toResource(carId);
        return ResponseEntity.ok(carClassResource);
    }
}
