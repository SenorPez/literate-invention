package com.senorpez.projectcars.racereport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "race", collectionRelation = "race")
public class RaceModel implements Identifiable<Integer>, Embeddable<EmbeddedRaceModel> {
    private final Integer index;
    private final Boolean completeRace;
    private final Float bestLapTime;
    private final Float bestSector1Time;
    private final Float bestSector2Time;
    private final Float bestSector3Time;
    private final Integer packetCount;

    RaceModel(final Integer index, final Race race) {
        race.getAll();
        this.index = index;
        this.completeRace = race.isCompleteRace();
        this.bestLapTime = race.getBestLapTime();
        this.bestSector1Time = race.getBestSector1Time();
        this.bestSector2Time = race.getBestSector2Time();
        this.bestSector3Time = race.getBestSector3Time();
        this.packetCount = race.getPacketCount();
    }

    @JsonIgnore
    @Override
    public EmbeddedRaceModel getEmbeddable() {
        return new EmbeddedRaceModel(this);
    }

    @Override
    public Integer getId() {
        return index;
    }

    public Boolean isCompleteRace() {
        return completeRace;
    }

    public Float getBestLapTime() {
        return bestLapTime;
    }

    public Float getBestSector1Time() {
        return bestSector1Time;
    }

    public Float getBestSector2Time() {
        return bestSector2Time;
    }

    public Float getBestSector3Time() {
        return bestSector3Time;
    }

    public Integer getPacketCount() {
        return packetCount;
    }
}
