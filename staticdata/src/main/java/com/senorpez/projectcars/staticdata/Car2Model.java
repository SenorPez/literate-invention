package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class Car2Model implements Identifiable<Integer> {
    private final int id;
    private final String manufacturer;
    private final String model;
    private final int year;
    private final String carClass;

    Car2Model(final Car2 car) {
        this.id = car.getId();
        this.manufacturer = car.getManufacturer();
        this.model = car.getModel();
        this.year = car.getYear();
        this.carClass = car.getCarClass();
    }

    Car2Resource toResource() {
        final APIResourceAssembler<Car2Model, Car2Resource> assembler = new APIResourceAssembler<>(Car2Controller.class, Car2Resource.class, () -> new Car2Resource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getCarClass() {
        return carClass;
    }
}
