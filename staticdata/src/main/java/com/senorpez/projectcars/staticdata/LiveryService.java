package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveryService {
    LiveryModel findOne(final Collection<Car> entities, final int carId, final int liveryId) {
        return entities.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElse(null)
                .getLiveries().stream()
                .filter(livery -> livery.getId() == liveryId)
                .findFirst()
                .map(LiveryModel::new)
                .orElse(null);
    }

    List<LiveryModel> findAll(final Collection<Car> entities, final int carId) {
        return entities.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElse(null)
                .getLiveries().stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
    }
}
