package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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
    ResponseEntity<EmbeddedCar2Resources> cars() {
        final List<EmbeddedCar2Model> car2Models = car2Service.findAll();
        final List<Resource<EmbeddedCar2Model>> car2Resources = car2Models.stream()
                .map(EmbeddedCar2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCar2Resources(car2Resources));
    }

    @RequestMapping("/{id}")
    ResponseEntity<Car2Resource> cars(@PathVariable final int id) {
        final Car2Model car2Model = car2Service.findOne(id);
        return ResponseEntity.ok(car2Model.toResource());
    }
}
