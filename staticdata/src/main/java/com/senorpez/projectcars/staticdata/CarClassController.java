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

import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS_2_VALUE;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequestMapping(
        value = "/classes",
        method = RequestMethod.GET
)
@RestController
public class CarClassController {
    private final APIService apiService;
    private Collection<CarClass> carClasses = null;

    @Autowired
    CarClassController(final APIService apiService) {
        this.apiService = apiService;
    }

    CarClassController(final APIService apiService, final Collection<CarClass> carClasses) {
        this.apiService = apiService;
        this.carClasses = carClasses;
    }

    @RequestMapping(
            produces = {PROJECT_CARS_VALUE, APPLICATION_JSON_UTF8_VALUE}
    )
    ResponseEntity<Resources<CarClassResource>> carClasses1() {
        carClasses = Application.CAR_CLASSES;
        return carClasses();
    }

    @RequestMapping(
            produces = {PROJECT_CARS_2_VALUE}
    )
    ResponseEntity<Resources<CarClassResource>> carClasses2() {
        carClasses = Application.CAR_CLASSES2;
        return carClasses();
    }

    private ResponseEntity<Resources<CarClassResource>> carClasses() {
        final Collection<CarClass> carClasses = apiService.findAll(this.carClasses);
        final Collection<CarClassModel> carClassModels = carClasses.stream()
                .map(CarClassModel::new)
                .collect(Collectors.toList());
        final Collection<CarClassResource> carClassResources = carClassModels.stream()
                .map(CarClassModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CarClassResource.makeResources(carClassResources));
    }

    @RequestMapping(
            value = "/{carClassId}",
            produces = {PROJECT_CARS_VALUE, APPLICATION_JSON_UTF8_VALUE}
    )
    ResponseEntity<CarClassResource> carClasses1(@PathVariable final int carClassId) {
        carClasses = Application.CAR_CLASSES;
        return carClasses(carClassId);
    }

    @RequestMapping(
            value = "/{carClassId}",
            produces = {PROJECT_CARS_2_VALUE}
    )
    ResponseEntity<CarClassResource> carClasses2(@PathVariable final int carClassId) {
        carClasses = Application.CAR_CLASSES2;
        return carClasses(carClassId);
    }

    private ResponseEntity<CarClassResource> carClasses(@PathVariable final int carClassId) {
        final CarClass carClass = apiService.findOne(
                carClasses,
                findCarClass -> findCarClass.getId() == carClassId,
                () -> new CarClassNotFoundException(carClassId));
        final CarClassModel carClassModel = new CarClassModel(carClass);
        final CarClassResource carClassResource = carClassModel.toResource();
        return ResponseEntity.ok(carClassResource);
    }
}
