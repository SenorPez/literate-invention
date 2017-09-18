package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class Car2Service {
    Car2Model findOne(final int searchId) {
        return Application.CARS2.stream()
                .filter(car -> car.getId() == searchId)
                .findFirst()
                .map(Car2Model::new)
                .orElseThrow(() -> new CarNotFoundException(searchId));
    }

    List<EmbeddedCar2Model> findAll() {
        return Application.CARS2.stream()
                .map(EmbeddedCar2Model::new)
                .collect(Collectors.toList());
    }
}
