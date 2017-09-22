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
        produces = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8"

)
@RestController
public class Car2Controller {
    @Autowired
    private APIService apiService;

    @RequestMapping
    ResponseEntity<EmbeddedCar2Resources> cars() {
        final Collection<Car2> car2s = Application.CARS2;
        final Collection<EmbeddedCar2Model> car2Models = car2s.stream()
                .map(EmbeddedCar2Model::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedCar2Model>> car2Resources = car2Models.stream()
                .map(EmbeddedCar2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCar2Resources(car2Resources));
    }

    @RequestMapping("/{id}")
    ResponseEntity<Car2Resource> cars(@PathVariable final int id) {
        final Car2 car2 = apiService.findOne(
                Application.CARS2,
                findCar -> findCar.getId() == id,
                () -> new CarNotFoundException(id));
        final Car2Model car2Model = new Car2Model(car2);
        return ResponseEntity.ok(car2Model.toResource());
    }
}
