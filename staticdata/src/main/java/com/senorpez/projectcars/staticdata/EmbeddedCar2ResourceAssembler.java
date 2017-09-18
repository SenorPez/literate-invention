package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import java.util.function.Supplier;

class EmbeddedCar2ResourceAssembler extends IdentifiableResourceAssemblerSupport<EmbeddedCar2Model, EmbeddedCar2Resource> {
    final private Supplier<EmbeddedCar2Resource> supplier;

    EmbeddedCar2ResourceAssembler(final Supplier<EmbeddedCar2Resource> supplier) {
        super(Car2Controller.class, EmbeddedCar2Resource.class);
        this.supplier = supplier;
    }

    @Override
    public EmbeddedCar2Resource toResource(final EmbeddedCar2Model entity) {
        return createResource(entity);
    }

    @Override
    protected EmbeddedCar2Resource instantiateResource(final EmbeddedCar2Model entity) {
        return supplier.get();
    }
}
