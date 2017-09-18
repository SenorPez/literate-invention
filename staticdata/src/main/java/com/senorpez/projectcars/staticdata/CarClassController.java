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
        value = "/classes",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class CarClassController {
    @Autowired
    private CarClassService carClassService;

    @RequestMapping
    ResponseEntity<Resources<Resource<CarClassModel>>> carClasses() {
        final List<CarClassModel> carClassModels = carClassService.findAll();
        final List<Resource<CarClassModel>> carClassResources = carClassModels.stream()
                .map(CarClassModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Resources<>(carClassResources));
    }

    @RequestMapping("/{carClassId}")
    ResponseEntity<CarClassResource> carClasses(@PathVariable final int carClassId) {
        final CarClassModel carClassModel = carClassService.findOne(carClassId);
        final CarClassResource carClassResource = carClassModel.toResource();
        return ResponseEntity.ok(carClassResource);
    }
}
