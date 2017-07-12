package com.senorpez.projectcars.racereport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senorpez.projectcars.racedata.Driver;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "driver", collectionRelation = "driver")
class DriverModel implements Identifiable<Integer>, Embeddable<EmbeddedDriverModel> {
    private final Integer index;
    private final String name;
    private final Float bestLapTime;
    private final Float bestSector1Time;
    private final Float bestSector2Time;
    private final Float bestSector3Time;
    private final Integer lapsComplete;
    private final Float raceTime;

    DriverModel(final Driver driver) {
        this.index = driver.getIndex().intValue();
        this.name = driver.getName();
        this.bestLapTime = driver.getBestLapTime();
        this.bestSector1Time = driver.getBestSector1Time();
        this.bestSector2Time = driver.getBestSector2Time();
        this.bestSector3Time = driver.getBestSector3Time();
        this.lapsComplete = driver.getLapsComplete();
        this.raceTime = driver.getRaceTime();
    }

    @JsonIgnore
    @Override
    public EmbeddedDriverModel getEmbeddable() {
        return new EmbeddedDriverModel(this);
    }

    @Override
    public Integer getId() {
        return index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
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

    public Integer getLapsComplete() {
        return lapsComplete;
    }

    public Float getRaceTime() {
        return raceTime;
    }
}
