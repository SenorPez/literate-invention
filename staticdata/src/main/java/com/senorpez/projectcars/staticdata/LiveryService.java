package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class LiveryService {
    LiveryModel findOne(final int carId, final int liveryId) {
        return Application.CARS.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(carId))
                .getLiveries().stream()
                .filter(livery -> livery.getId() == liveryId)
                .findFirst()
                .map(LiveryModel::new)
                .orElseThrow(() -> new LiveryNotFoundException(liveryId));
    }

    List<LiveryModel> findAll(final int carId) {
        return Application.CARS.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(carId))
                .getLiveries().stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
    }

    LiveryModel findOne(final int eventId, final int carId, final int liveryId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars().stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(carId))
                .getLiveries().stream()
                .filter(livery -> livery.getId() == liveryId)
                .findFirst()
                .map(LiveryModel::new)
                .orElseThrow(() -> new LiveryNotFoundException(liveryId));
    }

}
