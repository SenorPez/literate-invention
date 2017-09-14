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
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"
)
@RestController
public class Car2Controller {
    @Autowired
    private Car2Service car2Service;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedCar2Resource>> cars() {
        final List<EmbeddedCar2Model> carModels = car2Service.findAll(Application.CARS2);
        final Resources<EmbeddedCar2Resource> carResources = new Resources<>(carModels.stream()
                .map(EmbeddedCar2Model::toResource)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(carResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<Car2Resource> cars(@PathVariable final int id) {
        final Car2Model carModel = car2Service.findOne(Application.CARS2, id);
        return ResponseEntity.ok(carModel.toResource());
    }
}
