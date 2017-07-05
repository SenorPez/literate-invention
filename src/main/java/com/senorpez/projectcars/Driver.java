package com.senorpez.projectcars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.CurrentSector.*;

public class Driver {
    private final Integer index;
    private final String name;
    private final List<SectorTime> sectorTimes = new ArrayList<>();

    public Driver(Integer index) {
        this.index = index;
        this.name = String.format("Driver %d", index);
    }

    public Driver(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Driver)) return false;

        Driver driver = (Driver) obj;
        return name.equals(driver.name);
    }

    public void addSectorTime(CurrentSector currentSector, Float lastSectorTime) {
        if (lastSectorTime != -123.0) {
            if (sectorTimes.size() == 0) sectorTimes.add(new SectorTime(currentSector, lastSectorTime));
            else {
                SectorTime sectorTime = new SectorTime(currentSector, lastSectorTime);
                if (!(sectorTimes.get(sectorTimes.size() - 1).equals(sectorTime))) {
                    sectorTimes.add(sectorTime);
                }
            }
        }
    }

    public Float getBestLapTime() {
        return Collections.min(getLapTimes());
    }

    public Float getBestSector1Time() {
        return getBestSector(SECTOR_SECTOR1);
    }

    public Float getBestSector2Time() {
        return getBestSector(SECTOR_SECTOR2);
    }

    public Float getBestSector3Time() {
        return getBestSector(SECTOR_START);
    }

    Float getBestSector(CurrentSector currentSector) {
        return Collections.min(sectorTimes.stream()
                .filter(sectorTime -> sectorTime.getSector().equals(currentSector))
                .map(SectorTime::getTime)
                .collect(Collectors.toList()));
    }

    public Integer getLapsComplete() {
        return getLapTimes().size();
    }

    public List<Float> getLapTimes() {
        /* Make sure first sector time is SECTOR_SECTOR1. Trim until that's true. */
        if (sectorTimes.size() < 3) return new ArrayList<>();

        List<SectorTime> trimmedList = sectorTimes;
        while (trimmedList.get(0).getSector() != SECTOR_SECTOR1) trimmedList.remove(0);

        Integer laps = trimmedList.size() / 3;
        return IntStream.range(0, laps)
                .mapToObj(n -> trimmedList.subList(n * 3, (n * 3) + 3))
                .map(list -> list.stream()
                        .map(SectorTime::getTime)
                        .reduce(0f, Float::sum))
                .collect(Collectors.toList());
    }

    public Float getRaceTime() {
        return getLapTimes().stream().reduce(0f, Float::sum);
    }

    public List<Float> getSectorTimes() {
        return sectorTimes.stream()
                .map(SectorTime::getTime)
                .collect(Collectors.toList());
    }

    private class SectorTime {
        private final CurrentSector sector;
        private final Float time;

        private SectorTime(final CurrentSector sector, final Float time) {
            this.sector = sector;
            this.time = time;
        }

        private Float getTime() {
            return time;
        }

        private CurrentSector getSector() {
            return sector;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sector, time);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof SectorTime)) return false;

            SectorTime sectorTime = (SectorTime) obj;
            return sector.equals(sectorTime.sector)
                    && time.equals(sectorTime.time);
        }
    }
}
