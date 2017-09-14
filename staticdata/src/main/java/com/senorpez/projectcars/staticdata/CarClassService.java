package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarClassService {
    CarClassModel findOne(final Collection<CarClass> entities, final int searchId) {
        return entities.stream()
                .filter(carClass -> carClass.getId() == searchId)
                .findFirst()
                .map(CarClassModel::new)
                .orElse(null);
    }

    List<CarClassModel> findAll(final Collection<CarClass> entities) {
        return entities.stream()
                .map(CarClassModel::new)
                .collect(Collectors.toList());
    }
}
