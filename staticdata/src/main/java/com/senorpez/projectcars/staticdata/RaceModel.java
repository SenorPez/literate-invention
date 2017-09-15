package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "race", collectionRelation = "race")
class RaceModel implements Identifiable<Integer> {
    private final int id;
    private final int laps;
    private final int time;
    private final String type;

    RaceModel(final Race race) {
        this.id = race.getId();
        this.laps = race.getLaps();
        this.time = race.getTime();
        this.type = race.getType();
    }

    RaceResource toResource() {
        final RaceResourceAssembler assembler = new RaceResourceAssembler(() -> new RaceResource(this));
        return assembler.toResource(this);
    }

    RaceResource toResource(final int eventId, final int roundId) {
        final RaceResourceAssembler assembler = new RaceResourceAssembler(() -> new RaceResource(this));
        return assembler.toResource(this, eventId, roundId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getLaps() {
        return laps;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
