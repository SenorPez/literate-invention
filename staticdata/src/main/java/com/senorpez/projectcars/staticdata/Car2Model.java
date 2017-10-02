package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "car", collectionRelation = "car")
class Car2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;
    @JsonProperty("class")
    private final String carClass;
    @JsonIgnore
    private final int carClassId;

    Car2Model(final Car2 car) {
        this.id = car.getId();
        this.name = car.getName();
        this.carClass = car.getCarClass().getName();
        this.carClassId = car.getCarClass().getId();
    }

    Car2Resource toResource() {
        final APIResourceAssembler<Car2Model, Car2Resource> assembler = new APIResourceAssembler<>(Car2Controller.class, Car2Resource.class, () -> new Car2Resource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCarClass() {
        return carClass;
    }

    int getCarClassId() {
        return carClassId;
    }
}
