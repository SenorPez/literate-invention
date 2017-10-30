package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "round", collectionRelation = "round")
class RoundModel implements Identifiable<Integer> {
    private final int id;
    private final String track;
    private final int trackId;

    RoundModel(final Round round) {
        this.id = round.getId();
        this.track = round.getTrack().getName();
        this.trackId = round.getTrack().getId();
    }

    RoundResource toResource(final int eventId) {
        final APIResourceAssembler<RoundModel, RoundResource> assembler = new APIResourceAssembler<>(RoundController.class, RoundResource.class, () -> new RoundResource(this, eventId));
        return assembler.toResource(this, eventId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getTrack() {
        return track;
    }

    @JsonIgnore
    int getTrackId() {
        return trackId;
    }
}
