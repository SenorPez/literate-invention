package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "track", collectionRelation = "track")
class EmbeddedTrackModel implements Identifiable<Integer> {
    private final int id;
    private final String name;

    EmbeddedTrackModel(final Track track) {
        this.id = track.getId();
        this.name = track.getName();
    }

    Resource<EmbeddedTrackModel> toResource() {
        final APIEmbeddedResourceAssembler<EmbeddedTrackModel, EmbeddedTrackResource> assembler = new APIEmbeddedResourceAssembler<>(TrackController.class, EmbeddedTrackResource.class, () -> new EmbeddedTrackResource(this));
        return assembler.toResource(this);
    }

    private class EmbeddedTrackResource extends Resource<EmbeddedTrackModel> {
        private EmbeddedTrackResource(final EmbeddedTrackModel content, final Link... links) {
            super(content, links);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
