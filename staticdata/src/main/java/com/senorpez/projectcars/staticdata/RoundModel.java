package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "round", collectionRelation = "round")
class RoundModel implements Identifiable<Integer> {
    private final int id;
    private final String track;

    RoundModel(final Round round) {
        this.id = round.getId();
        this.track = round.getTrack().getName();
    }

    RoundResource toResource() {
        final RoundResourceAssembler assembler = new RoundResourceAssembler(() -> new RoundResource(this));
        return assembler.toResource(this);
    }

    RoundResource toResource(final int eventId) {
        final RoundResourceAssembler assembler = new RoundResourceAssembler(() -> new RoundResource(this));
        return assembler.toResource(this, eventId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getTrack() {
        return track;
    }
}
