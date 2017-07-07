package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import org.springframework.hateoas.Identifiable;

public class RaceModel implements Identifiable<Integer> {
    private final Integer index;
    private final Boolean completeRace;
    private final Float bestLapTime;
    private final Float bestSector1Time;
    private final Float bestSector2Time;
    private final Float bestSector3Time;
    private final Integer packetCount;


    public RaceModel(Integer index, Race race) {
        this.index = index;
        this.completeRace = race.isCompleteRace();
        race.getAll();
        this.bestLapTime = race.getBestLapTime();
        this.bestSector1Time = race.getBestSector1Time();
        this.bestSector2Time = race.getBestSector2Time();
        this.bestSector3Time = race.getBestSector3Time();
        this.packetCount = race.getRacePackets().size();
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
