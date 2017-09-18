package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
class CarService {
    CarModel findOne(final int searchId) {
        return Application.CARS.stream()
                .filter(car -> car.getId() == searchId)
                .findFirst()
                .map(CarModel::new)
                .orElseThrow(() -> new CarNotFoundException(searchId));
    }

    List<EmbeddedCarModel> findAll() {
        return Application.CARS.stream()
                .map(EmbeddedCarModel::new)
                .collect(Collectors.toList());
    }

    CarClassModel findCarClass(final int carId) {
        return Application.CARS.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .map(car -> new CarClassModel(car.getCarClass()))
                .orElseThrow(() -> new CarNotFoundException(carId));
    }

    List<EmbeddedCarModel> findEventCars(final int eventId) {
        return eventCars(eventId).stream()
                .map(EmbeddedCarModel::new)
                .collect(Collectors.toList());
    }

    CarModel findEventCar(final int eventId, final int carId) {
        final Collection<Car> eventCars = eventCars(eventId);
        return new CarModel(findSingle(eventCars, carId));
    }

    private Collection<Car> eventCars(final int eventId) {
        return Application.EVENTS.stream()
                .filter(event -> event.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(eventId))
                .getCars();
    }

    private Car findSingle(final Collection<Car> cars, final int carId) {
        return cars.stream()
                .filter(car -> car.getId() == carId)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(carId));
    }

    List<LiveryModel> findEventCarLiveries(final int eventId, final int carId) {
        final Collection<Car> cars = eventCars(eventId);
        final Car car = findSingle(cars, carId);
        return car.getLiveries().stream()
                .map(LiveryModel::new)
                .collect(Collectors.toList());
    }
}
