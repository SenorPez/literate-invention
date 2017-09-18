package com.senorpez.projectcars.staticdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(
        value = "/classes",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class CarClassController {
    @Autowired
    private APIService apiService;

    @RequestMapping
    ResponseEntity<Resources<Resource<CarClassModel>>> carClasses() {
        final Collection<CarClass> carClasses = Application.CAR_CLASSES;
        final Collection<CarClassModel> carClassModels = carClasses.stream()
                .map(CarClassModel::new)
                .collect(Collectors.toList());
        final Collection<Resource<CarClassModel>> carClassResources = carClassModels.stream()
                .map(CarClassModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Resources<>(carClassResources));
    }

    @RequestMapping("/{carClassId}")
    ResponseEntity<CarClassResource> carClasses(@PathVariable final int carClassId) {
        final CarClass carClass = apiService.findOne(
                Application.CAR_CLASSES,
                findCarClass -> findCarClass.getId() == carClassId,
                () -> new CarClassNotFoundException(carClassId));
        final CarClassModel carClassModel = new CarClassModel(carClass);
        final CarClassResource carClassResource = carClassModel.toResource();
        return ResponseEntity.ok(carClassResource);
    }
}
