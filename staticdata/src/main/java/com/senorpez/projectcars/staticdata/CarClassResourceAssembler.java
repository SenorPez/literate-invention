package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

public class CarClassResourceAssembler extends IdentifiableResourceAssemblerSupport<CarClassModel, CarClassResource> {
    final private Supplier<CarClassResource> supplier;

    CarClassResourceAssembler(final Supplier<CarClassResource> supplier) {
        super(CarClassController.class, CarClassResource.class);
        this.supplier = supplier;
    }

    @Override
    public CarClassResource toResource(CarClassModel entity) {
        return createResource(entity);
    }

    @Override
    protected CarClassResource instantiateResource(CarClassModel entity) {
        return supplier.get();
    }
}
