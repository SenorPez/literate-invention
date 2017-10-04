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

import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS_2_VALUE;

@RequestMapping(
        value = "/cars",
        method = RequestMethod.GET,
        produces = PROJECT_CARS_2_VALUE

)
@RestController
public class Car2Controller {
    private final APIService apiService;
    private final Collection<Car2> cars;

    @Autowired
    Car2Controller(final APIService apiService) {
        this.apiService = apiService;
        this.cars = Application.CARS2;
    }

    Car2Controller(final APIService apiService, final Collection<Car2> cars) {
        this.apiService = apiService;
        this.cars = cars;
    }

    @RequestMapping
    ResponseEntity<EmbeddedCar2Resources> cars() {
        final Collection<Car2> cars = apiService.findAll(this.cars);
        final Collection<EmbeddedCar2Model> carModels = cars.stream()
                .map(EmbeddedCar2Model::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedCar2Model>> carResources = carModels.stream()
                .map(EmbeddedCar2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCar2Resources(carResources));
    }

    @RequestMapping("/{carId}")
    ResponseEntity<Car2Resource> cars(@PathVariable final int carId) {
        final Car2 car2 = apiService.findOne(
                this.cars,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final Car2Model car2Model = new Car2Model(car2);
        final Car2Resource car2Resource = car2Model.toResource();
        return ResponseEntity.ok(car2Resource);
    }

    @RequestMapping("/{carId}/class")
    ResponseEntity<CarClass2Resource> carClass(@PathVariable final int carId) {
        final Car2 car = apiService.findOne(
                this.cars,
                findCar -> findCar.getId() == carId,
                () -> new CarNotFoundException(carId));
        final CarClass2Model carClassModel = new CarClass2Model(car.getCarClass());
        final CarClass2Resource carClassResource = carClassModel.toResource(carId);
        return ResponseEntity.ok(carClassResource);
    }
}
