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
        value = "/classes",
        method = RequestMethod.GET,
        produces = PROJECT_CARS_2_VALUE
)
@RestController
public class CarClass2Controller {
    private final APIService apiService;
    private Collection<CarClass2> carClasses = null;

    @Autowired
    CarClass2Controller(final APIService apiService) {
        this.apiService = apiService;
        this.carClasses = Application.CAR_CLASSES2;
    }

    CarClass2Controller(final APIService apiService, final Collection<CarClass2> carClasses) {
        this.apiService = apiService;
        this.carClasses = carClasses;
    }

    @RequestMapping
    ResponseEntity<EmbeddedCarClass2Resources> carClasses() {
        final Collection<CarClass2> carClasses = apiService.findAll(this.carClasses);
        final Collection<EmbeddedCarClass2Model> carClassModels = carClasses.stream()
                .map(EmbeddedCarClass2Model::new)
                .collect(Collectors.toList());
        final Collection<Resource<EmbeddedCarClass2Model>> carClassResources = carClassModels.stream()
                .map(EmbeddedCarClass2Model::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new EmbeddedCarClass2Resources(carClassResources));
    }

    @RequestMapping("/{carClassId}")
    ResponseEntity<CarClass2Resource> carClasses(@PathVariable final int carClassId) {
        final CarClass2 carClass = apiService.findOne(
                carClasses,
                findCarClass -> findCarClass.getId() == carClassId,
                () -> new CarClassNotFoundException(carClassId));
        final CarClass2Model carClassModel = new CarClass2Model(carClass);
        final CarClass2Resource carClassResource = carClassModel.toResource();
        return ResponseEntity.ok(carClassResource);
    }
}
