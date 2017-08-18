package com.senorpez.projectcars.racereport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senorpez.projectcars.racedata.Driver;
import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import java.util.Set;

@Relation(value = "race", collectionRelation = "race")
class RaceModel implements Identifiable<Integer> {
    private final Integer id;
    private final Boolean completeRace;
    private final Float bestLapTime;
    private final Float bestSector1Time;
    private final Float bestSector2Time;
    private final Float bestSector3Time;
    private final Integer packetCount;
    private final Integer currentLapNumber;

    private final Set<Driver> drivers;

    RaceModel(final Integer id, final Race race) {
        race.getAll();
        this.id = id;
        this.completeRace = race.isCompleteRace();
        this.bestLapTime = race.getBestLapTime();
        this.bestSector1Time = race.getBestSector1Time();
        this.bestSector2Time = race.getBestSector2Time();
        this.bestSector3Time = race.getBestSector3Time();
        this.packetCount = race.getPacketCount();
        this.currentLapNumber = race.getCurrentLapNumber();

        this.drivers = race.getDrivers();
    }

    @Override
    public Integer getId() {
        return id;
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

    public Integer getCurrentLapNumber() {
        return currentLapNumber;
    }

    @JsonIgnore
    Set<Driver> getDrivers() {
        return drivers;
    }
}
