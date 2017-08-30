package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EmbeddedCar2Service {
    private static EmbeddedCar2ResourceAssembler assembler;

    List<EmbeddedCar2Model> findAll(final Collection<Car2> entities) {
        final AtomicInteger id = new AtomicInteger(0);
        return entities.stream()
                .map(car -> new EmbeddedCar2Model(id.incrementAndGet(), car))
                .collect(Collectors.toList());
    }

    Resources<EmbeddedCar2Resource> toResource(final List<EmbeddedCar2Model> models, final Object... parameters) {
        final Resources<EmbeddedCar2Resource> resources = new Resources<>(models.stream()
                .map(model -> toResource(model))
                .collect(Collectors.toList()));
        return resources;
    }

    private EmbeddedCar2Resource toResource(final EmbeddedCar2Model model, final Object... parameters) {
        assembler = new EmbeddedCar2ResourceAssembler(() -> new EmbeddedCar2Resource(model));
        return assembler.toResource(model);
    }
}
