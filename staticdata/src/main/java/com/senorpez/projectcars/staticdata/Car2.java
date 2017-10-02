package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

class Car2 implements CommonCar {
    private final int id;
    private final String name;
    private final CarClass carClass;
    private final Set<Livery> liveries;

    public Car2(
            @JsonProperty("id") final int id,
            @JsonProperty("name") final String name,
            @JsonProperty("class") final String carClass) {
        this.id = id;
        this.name = name;
        this.carClass = Application.CAR_CLASSES2.stream().filter(cclass -> cclass.getName().equalsIgnoreCase(carClass)).findAny().orElseThrow(RuntimeException::new);

        final JsonNode carLiveryNode = Application.LIVERY_NODES.parallelStream()
                .filter(jsonNode -> jsonNode.get("id").intValue() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        this.liveries = Application.getData(Livery.class, carLiveryNode.get("liveries"));
    }

    Car2(final int id, final String name, final CarClass carClass, final Set<Livery> liveries) {
        this.id = id;
        this.name = name;
        this.carClass = carClass;
        this.liveries = liveries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CarClass getCarClass() {
        return carClass;
    }

    public Set<Livery> getLiveries() {
        return liveries;
    }
}
