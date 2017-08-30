package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(
        value = "/cars",
        method = RequestMethod.GET,
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"
)
@RestController
public class Car2Controller {
    @Autowired
    private EmbeddedCar2Service embeddedCar2Service;

    @Autowired
    private Car2Service car2Service;

    @RequestMapping
    ResponseEntity<Resources<EmbeddedCar2Resource>> cars() {
        final List<EmbeddedCar2Model> carModels = embeddedCar2Service.findAll(Application.CARS2);
        return ResponseEntity.ok(embeddedCar2Service.toResource(carModels));
    }

    @RequestMapping("/{id}")
    ResponseEntity<Car2Resource> cars(@PathVariable int id) {
        final Car2Model carModel = car2Service.findOne(Application.CARS2, id);
        return ResponseEntity.ok(car2Service.toResource(carModel));
    }
}
