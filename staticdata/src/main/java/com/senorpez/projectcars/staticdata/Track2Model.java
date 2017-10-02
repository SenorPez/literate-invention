package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "track", collectionRelation = "track")
public class Track2Model implements Identifiable<Integer> {
    private final int id;
    private final String name;
    private final int gridSize;

    Track2Model(final Track2 track) {
        this.id = track.getId();
        this.name = track.getName();
        this.gridSize = track.getGridSize();
    }

    Track2Resource toResource() {
        final APIResourceAssembler<Track2Model, Track2Resource> assembler = new APIResourceAssembler<>(Track2Controller.class, Track2Resource.class, () -> new Track2Resource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGridSize() {
        return gridSize;
    }
}
