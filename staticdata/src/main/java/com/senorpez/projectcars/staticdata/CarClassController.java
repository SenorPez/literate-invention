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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
    ResponseEntity<Resources<CarClassResource>> carClasses() {
        final List<CarClassModel> carClassModels = carClassService.findAll();
        final Resources<CarClassResource> carClassResources = new Resources<>(carClassModels.stream()
                .map(CarClassModel::toResource)
                .collect(Collectors.toList()));
        carClassResources.add(linkTo(methodOn(CarClassController.class).carClasses()).withSelfRel());
        carClassResources.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carClassResources);
    }

    @RequestMapping("/{id}")
    ResponseEntity<CarClassResource> carClasses(@PathVariable final int id) {
        final CarClassModel carClassModel = carClassService.findOne(id);
        final CarClassResource carClassResource = carClassModel.toResource();
        carClassResource.add(linkTo(methodOn(CarClassController.class).carClasses()).withRel("classes"));
        carClassResource.add(linkTo(methodOn(RootController.class).root()).withRel("index"));
        return ResponseEntity.ok(carClassResource);
    }
}
