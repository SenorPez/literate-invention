package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class CarClassService {
    CarClassModel findOne(final int searchId) {
        return Application.CAR_CLASSES.stream()
                .filter(carClass -> carClass.getId() == searchId)
                .findFirst()
                .map(CarClassModel::new)
                .orElseThrow(() -> new CarClassNotFoundException(searchId));
    }

    List<CarClassModel> findAll() {
        return Application.CAR_CLASSES.stream()
                .map(CarClassModel::new)
                .collect(Collectors.toList());
    }
}
