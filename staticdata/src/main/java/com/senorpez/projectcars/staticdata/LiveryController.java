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

@RequestMapping(
        value = "/cars/{carId}/liveries",
        method = RequestMethod.GET,
        produces = {"application/vnd.senorpez.pcars.v1+json; charset=UTF-8", "application/json; charset=UTF-8"}
)
@RestController
public class LiveryController {
    @Autowired
    private APIService apiService;

    @RequestMapping
    ResponseEntity<Resources<LiveryResource>> liveries(@PathVariable final int carId) {
        final Car car = apiService.findOne(
                Application.CARS,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Collection<Livery> liveries = car.getLiveries();
        final Collection<LiveryModel> liveryModels = liveries.stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
        final Collection<LiveryResource> liveryResources = liveryModels.stream()
                .map(LiveryModel::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(LiveryResource.makeResources(liveryResources, carId));
    }

    @RequestMapping("/{liveryId}")
    ResponseEntity<LiveryResource> liveries(@PathVariable final int carId, @PathVariable final int liveryId) {
        final Car car = apiService.findOne(
                Application.CARS,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Livery livery = apiService.findOne(
                car.getLiveries(),
                findLivery -> findLivery.getId() == liveryId,
                () -> new LiveryNotFoundException(liveryId));
        final LiveryModel liveryModel = new LiveryModel(livery);
        final LiveryResource liveryResource = liveryModel.toResource(carId);
        return ResponseEntity.ok(liveryResource);
    }
}
