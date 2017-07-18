package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "lap", collectionRelation = "lap")
class LapModel implements Identifiable<Integer> {
    private final Integer id;

    LapModel(final Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
