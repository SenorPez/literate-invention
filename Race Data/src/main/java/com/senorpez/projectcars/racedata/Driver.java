package com.senorpez.projectcars.racedata;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.racedata.CurrentSector.*;

public class Driver {
    private final Byte index;
    private final String name;
    private final List<SectorTime> sectorTimes = new ArrayList<>();

    private Driver(final Byte index) {
        if (index < -1 || index > 56) throw new IllegalArgumentException("Index must be between -1 and 55");
        this.index = index;
        this.name = String.format("Driver %d", index);
    }

    private Driver(final Byte index, final String name) {
        if (index < -1 || index > 56) throw new IllegalArgumentException("Index must be between -1 and 56");
        this.index = index;
        this.name = name;
    }

    Driver(final Integer index, final String name) {
        this(index.byteValue(), name);
    }

    public Byte getIndex() {
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
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Driver)) return false;

        final Driver driver = (Driver) obj;
        return name.equals(driver.name);
    }

    void addSectorTime(final CurrentSector currentSector, final Float lastSectorTime) {
        if (lastSectorTime != -123.0) {
            if (sectorTimes.size() == 0) sectorTimes.add(new SectorTime(currentSector, lastSectorTime));
            else {
                final SectorTime sectorTime = new SectorTime(currentSector, lastSectorTime);
                if (!(sectorTimes.get(sectorTimes.size() - 1).equals(sectorTime))) {
                    sectorTimes.add(sectorTime);
                }
            }
        }
    }

    public Float getBestLapTime() {
        final List<Float> lapTimes = getLapTimes().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return (lapTimes.size() > 0) ? Collections.min(lapTimes) : null;
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

    Float getBestSector(final CurrentSector currentSector) {
        final List<Float> sectorTimes = this.sectorTimes.stream()
                .filter(sectorTime -> sectorTime.getSector().equals(currentSector))
                .map(SectorTime::getTime)
                .collect(Collectors.toList());
        return (sectorTimes.size() > 0) ? Collections.min(sectorTimes) : null;
    }

    public Integer getLapsComplete() {
        return getLapTimes().size();
    }

    private List<Float> getLapTimes() {
        /* Make sure first sector time is SECTOR_SECTOR1. Trim until that's true. */
        if (sectorTimes.size() < 3) return new ArrayList<>();

        final List<SectorTime> trimmedList = sectorTimes;
        while (trimmedList.get(0).getSector() != SECTOR_SECTOR1) trimmedList.remove(0);

        final Integer laps = trimmedList.size() / 3;
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
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof SectorTime)) return false;

            final SectorTime sectorTime = (SectorTime) obj;
            return sector.equals(sectorTime.sector)
                    && time.equals(sectorTime.time);
        }
    }
}
