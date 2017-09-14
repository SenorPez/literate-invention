package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class Car2ResourceAssembler extends IdentifiableResourceAssemblerSupport<Car2Model, Car2Resource> {
    final private Supplier<Car2Resource> supplier;

    Car2ResourceAssembler(final Supplier<Car2Resource> supplier) {
        super(Car2Controller.class, Car2Resource.class);
        this.supplier = supplier;
    }

    @Override
    public Car2Resource toResource(final Car2Model entity) {
        return createResource(entity);
    }

    @Override
    protected Car2Resource instantiateResource(final Car2Model entity) {
        return supplier.get();
    }
}
