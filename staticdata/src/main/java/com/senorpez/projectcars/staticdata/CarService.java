package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
class CarService {
    CarModel findOne(final Collection<Car> entities, final int searchId) {
        return entities.stream()
                .filter(car -> car.getId() == searchId)
                .findFirst()
                .map(CarModel::new)
                .orElseThrow(() -> new CarNotFoundException(searchId));
    }

    List<EmbeddedCarModel> findAll(final Collection<Car> entities) {
        return entities.stream()
                .map(EmbeddedCarModel::new)
                .collect(Collectors.toList());
    }
}
