package com.senorpez.projectcars.racedata;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.racedata.CurrentSector.*;
import static com.senorpez.projectcars.racedata.TelemetryDataPacket.State.*;

public class Race implements Iterator<Packet> {
    private final Deque<Packet> racePackets = new ArrayDeque<>();
    private final Set<Driver> drivers = new HashSet<>();

    private TelemetryDataPacket currentPacket;
    private Float elapsedTime;

    private Boolean completeRace = false;
    private Integer packetCount = 0;

    public Race(final Telemetry telemetry) {
        Packet packet;
        do {
            packet = telemetry.next();
        } while(!(packet instanceof TelemetryDataPacket)
                || ((TelemetryDataPacket) packet).getState() != PRE_RACE);

        while (!(packet instanceof TelemetryDataPacket)
                || ((TelemetryDataPacket) packet).getState() != LOADING) {
            racePackets.add(packet);
            packet = telemetry.next();
            if (packet == null) break;
        }

        final TelemetryDataPacket firstPacket = (TelemetryDataPacket) racePackets.getFirst();
        final TelemetryDataPacket lastPacket = (TelemetryDataPacket) racePackets.getLast();

        this.completeRace = firstPacket.getState() == PRE_RACE && lastPacket.getState() == FINISHED;
        this.packetCount = racePackets.size();

        this.next();
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void getAll() {
        while (hasNext()) {
            next();
        }
    }

    @Override
    public boolean hasNext() {
        return racePackets.size() > 0;
    }

    @Override
    public Packet next() {
        final Packet packet = racePackets.removeFirst();

        if (packet instanceof TelemetryDataPacket) {
            final TelemetryDataPacket telemetryDataPacket = (TelemetryDataPacket) packet;
            if (drivers.size() != ((TelemetryDataPacket) packet).getNumParticipants()) {
                populateDrivers(((TelemetryDataPacket) packet).getNumParticipants());
            }

            populateSectorTimes(telemetryDataPacket);
            currentPacket = (TelemetryDataPacket) packet;

            calculateElapsedTime();
        }

        return packet;
    }

    private void calculateElapsedTime() {
        if (currentPacket.getCurrentTime() == -1.0) {
            elapsedTime = 0f;
        } else {
            final Driver viewedDriver = drivers.stream()
                    .filter(driver -> driver.getIndex().equals(currentPacket.getViewedParticipantIndex()))
                    .findFirst()
                    .orElse(null);
            elapsedTime = viewedDriver.getRaceTime() + currentPacket.getCurrentTime();
        }
    }

    private void populateSectorTimes(final TelemetryDataPacket packet) {
        drivers.forEach(driver -> {
            final TelemetryDataPacket.ParticipantInfo participantInfo = packet.getParticipantInfo().get(driver.getIndex());
            driver.addSectorTime(participantInfo.getCurrentSector(), participantInfo.getLastSectorTime());
        });
    }

    private void populateDrivers(final Byte numParticipants) {
        final Deque<Packet> readPackets = new ArrayDeque<>();

        try {
            while (drivers.size() != numParticipants) {
                final Packet packet = racePackets.removeFirst();
                readPackets.addLast(packet);

                if (packet instanceof ParticipantPacket) {
                    final ParticipantPacket participantPacket = (ParticipantPacket) packet;
                    final AtomicInteger index = new AtomicInteger(0);
                    participantPacket.getNames().stream()
                            .limit(numParticipants)
                            .forEach(name -> drivers.add(new Driver(index.getAndIncrement(), name)));
                } else if (packet instanceof AdditionalParticipantPacket) {
                    final AdditionalParticipantPacket participantPacket = (AdditionalParticipantPacket) packet;
                    final AtomicInteger index = new AtomicInteger(participantPacket.getOffset());
                    participantPacket.getNames().stream()
                            .limit(participantPacket.getOffset() % 16)
                            .forEach(s -> drivers.add(new Driver(index.getAndIncrement(), s)));
                }
            }
        } catch (final NoSuchElementException e) {
            /* Driver names never populated, so make fake names. */
            IntStream.range(0, numParticipants)
                    .forEach(value -> drivers.add(new Driver(value, String.format("Driver %d", value))));
        }

        /* Repopulate race deque. */
        while (readPackets.size() > 0) {
            racePackets.addFirst(readPackets.removeLast());
        }
    }

    public Float getBestLapTime() {
        final List<Float> lapTimes = drivers.stream()
                .map(Driver::getBestLapTime)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return (lapTimes.size() > 0) ? Collections.min(lapTimes) : null;
    }

    public Float getBestSector1Time() {
        return getBestSectorTime(SECTOR_SECTOR1);
    }

    public Float getBestSector2Time() {
        return getBestSectorTime(SECTOR_SECTOR2);
    }

    public Float getBestSector3Time() {
        return getBestSectorTime(SECTOR_START);
    }

    private Float getBestSectorTime(final CurrentSector currentSector) {
        final List<Float> sectorTimes = drivers.stream()
                .map(driver -> driver.getBestSector(currentSector))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return (sectorTimes.size() > 0) ? Collections.min(sectorTimes) : null;
    }

    public Map<Short, Driver> getClassification() {
        return drivers.stream()
                .collect(Collectors.toMap(
                        driver -> currentPacket.getParticipantInfo().get(driver.getIndex()).getRacePosition(),
                        Function.identity()));
    }

    public Float getCurrentEventTimeRemaining() {
        return currentPacket.getEventTimeRemaining();
    }

    public Integer getCurrentLapNumber() {
        final Short leaderLap = Collections.max(currentPacket.getParticipantInfo().stream()
                .map(TelemetryDataPacket.ParticipantInfo::getCurrentLap)
                .collect(Collectors.toList()));
        return Math.min(leaderLap, currentPacket.getLapsInEvent());
    }

    public Float getCurrentTime() {
        return currentPacket.getCurrentTime();
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public Boolean isCompleteRace() {
        return completeRace;
    }

    public Integer getPacketCount() {
        return packetCount;
    }
}
