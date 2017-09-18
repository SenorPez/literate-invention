package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import java.util.List;

@Relation(value = "track", collectionRelation = "track")
class TrackModel implements Identifiable<Integer> {
    private final int id;
    private final String name;
    private final String location;
    private final String variation;
    private final float length;
    private final List<Float> pitEntry;
    private final List<Float> pitExit;
    private final int gridSize;

    TrackModel(final Track track) {
        this.id = track.getId();
        this.name = track.getName();
        this.location = track.getLocation();
        this.variation = track.getVariation();
        this.length = track.getLength();
        this.pitEntry = track.getPitEntry();
        this.pitExit = track.getPitExit();
        this.gridSize = track.getGridSize();
    }

    TrackResource toResource() {
        final TrackResourceAssembler assembler = new TrackResourceAssembler(() -> new TrackResource(this));
        return assembler.toResource(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getVariation() {
        return variation;
    }

    public float getLength() {
        return length;
    }

    public List<Float> getPitEntry() {
        return pitEntry;
    }

    public List<Float> getPitExit() {
        return pitExit;
    }

    public int getGridSize() {
        return gridSize;
    }
}
