package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "track", collectionRelation = "track")
class EmbeddedTrackModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedTrackModel(final Track track) {
        this.id = track.getId();
        this.name = track.getName();
    }

    EmbeddedTrackResource toResource() {
        final EmbeddedTrackResourceAssembler assembler = new EmbeddedTrackResourceAssembler(() -> new EmbeddedTrackResource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
