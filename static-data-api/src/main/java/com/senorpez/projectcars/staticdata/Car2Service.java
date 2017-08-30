package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Car2Service {
    private static Car2ResourceAssembler assembler;

    Car2Model findOne(final Collection<Car2> entities, int findId) {
        final AtomicInteger id = new AtomicInteger(0);
        return entities.stream()
                .map(car -> new Car2Model(id.incrementAndGet(), car))
                .filter(car -> car.getId().equals(findId))
                .findAny()
                .orElse(null);
    }

    Car2Resource toResource(final Car2Model model, final Object... parameters) {
        assembler = new Car2ResourceAssembler(() -> new Car2Resource(model));
        return assembler.toResource(model);
    }
}