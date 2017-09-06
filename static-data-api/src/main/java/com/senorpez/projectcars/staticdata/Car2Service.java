package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
class Car2Service {
    Car2Model findOne(final Collection<Car2> entities, final int searchId) {
        return entities.stream()
                .filter(car -> car.getId() == searchId)
                .findFirst()
                .map(Car2Model::new)
                .orElse(null);
    }

    List<EmbeddedCar2Model> findAll(final Collection<Car2> entities) {
       return entities.stream()
               .map(EmbeddedCar2Model::new)
               .collect(Collectors.toList());
    }
}