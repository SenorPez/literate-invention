package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "race", collectionRelation = "race")
public class EmbeddedRaceModel implements Identifiable<Integer> {
    private final int index;
    private final boolean completeRace;
    private final int packetCount;

    public EmbeddedRaceModel(int index, Race race) {
        race.getAll();
        this.index = index;
        this.completeRace = race.isCompleteRace();
        this.packetCount = race.getPacketCount();
    }

    @Override
    public Integer getId() {
        return index;
    }

    public boolean isCompleteRace() {
        return completeRace;
    }

    public int getPacketCount() {
        return packetCount;
    }
}
