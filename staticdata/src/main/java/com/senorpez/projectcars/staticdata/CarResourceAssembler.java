package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class CarResourceAssembler extends IdentifiableResourceAssemblerSupport<CarModel, CarResource> {
    final private Supplier<CarResource> supplier;

    CarResourceAssembler(final Supplier<CarResource> supplier) {
        super(CarController.class, CarResource.class);
        this.supplier = supplier;
    }

    @Override
    public CarResource toResource(CarModel entity) {
        return createResource(entity);
    }

    @Override
    protected CarResource instantiateResource(CarModel entity) {
        return supplier.get();
    }
}
