package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "race", collectionRelation = "race")
class RaceModel implements Identifiable<Integer> {
    private final int id;
    private final Integer laps;
    private final Integer time;
    private final String type;

    RaceModel(final Race race) {
        this.id = race.getId();
        this.laps = race.getLaps();
        this.time = race.getTime();
        this.type = race.getType();
    }

    RaceResource toResource(final int eventId, final int roundId) {
        final APIResourceAssembler<RaceModel, RaceResource> assembler = new APIResourceAssembler<>(RaceController.class, RaceResource.class, () -> new RaceResource(this, eventId, roundId));
        return assembler.toResource(this, eventId, roundId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getLaps() {
        return laps;
    }

    public Integer getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
