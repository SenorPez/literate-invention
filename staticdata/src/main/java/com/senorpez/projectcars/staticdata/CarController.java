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
        value = "/cars",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedCarResource>> cars() {
        final List<EmbeddedCarModel> carModels = carService.findAll(Application.CARS);
        final Resources<EmbeddedCarResource> carResources = new Resources<>(carModels.stream()
                .map(EmbeddedCarModel::toResource)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(carResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<CarResource> cars(@PathVariable final int id) {
        final CarModel carModel = carService.findOne(Application.CARS, id);
        return ResponseEntity.ok(carModel.toResource());
    }
}
