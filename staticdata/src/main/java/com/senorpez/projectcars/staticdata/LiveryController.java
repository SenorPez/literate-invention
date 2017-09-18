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
        value = "/cars/{carId}/liveries",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class LiveryController {
    @Autowired
    private LiveryService liveryService;

    @RequestMapping
    ResponseEntity<Resources<LiveryResource>> liveries(@PathVariable final int carId) {
        final List<LiveryModel> liveryModels = liveryService.findAll(carId);
        final List<LiveryResource> liveryResources = liveryModels.stream()
                .map(LiveryModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(LiveryResource.makeResources(liveryResources, carId));
    }

    @RequestMapping("/{liveryId}")
    ResponseEntity<LiveryResource> liveries(@PathVariable final int carId, @PathVariable final int liveryId) {
        final LiveryModel liveryModel = liveryService.findOne(carId, liveryId);
        final LiveryResource liveryResource = liveryModel.toResource(carId);
        return ResponseEntity.ok(liveryResource);
    }
}
