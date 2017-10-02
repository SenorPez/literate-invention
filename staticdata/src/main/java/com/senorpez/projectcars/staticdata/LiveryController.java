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
        value = "/cars/{carId}/liveries",
        method = RequestMethod.GET
)
@RestController
public class LiveryController {
    private final APIService apiService;
    private final Collection<Car> cars;
    private final Collection<Car2> car2s;

    @Autowired
    LiveryController(final APIService apiService) {
        this.apiService = apiService;
        this.cars = Application.CARS;
        this.car2s = Application.CARS2;
    }

    LiveryController(final APIService apiService, final Collection<Car> cars, final Collection<Car2> car2s) {
        this.apiService = apiService;
        this.cars = cars;
        this.car2s = car2s;
    }

    @RequestMapping(
    produces = {PROJECT_CARS_VALUE, APPLICATION_JSON_UTF8_VALUE}
    )
    ResponseEntity<Resources<LiveryResource>> liveries1(@PathVariable final int carId) {
        return liveries(cars, carId);
    }

    @RequestMapping(
    produces = {PROJECT_CARS_2_VALUE}
    )
    ResponseEntity<Resources<LiveryResource>> liveries2(@PathVariable final int carId) {
        return liveries(car2s, carId);
    }

    private <T extends CommonCar> ResponseEntity<Resources<LiveryResource>> liveries(final Collection<T> cars, @PathVariable final int carId) {
        final T car = apiService.findOne(
                cars,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Collection<Livery> liveries = car.getLiveries();
        final Collection<LiveryModel> liveryModels = liveries.stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
        final Collection<LiveryResource> liveryResources = liveryModels.stream()
                .map(liveryModel -> liveryModel.toResource(carId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(LiveryResource.makeResources(liveryResources, carId));
    }

    @RequestMapping(
            value = "/{liveryId}",
            produces = {PROJECT_CARS_VALUE, APPLICATION_JSON_UTF8_VALUE}
    )
    ResponseEntity<LiveryResource> liveries1(@PathVariable final int carId, @PathVariable final int liveryId) {
        return liveries(cars, carId, liveryId);
    }

    @RequestMapping(
            value = "/{liveryId}",
            produces = {PROJECT_CARS_2_VALUE}
    )
    ResponseEntity<LiveryResource> liveries2(@PathVariable final int carId, @PathVariable final int liveryId) {
        return liveries(car2s, carId, liveryId);
    }

    private <T extends CommonCar> ResponseEntity<LiveryResource> liveries(final Collection<T> cars, @PathVariable final int carId, @PathVariable final int liveryId) {
        final T car = apiService.findOne(
                cars,
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
