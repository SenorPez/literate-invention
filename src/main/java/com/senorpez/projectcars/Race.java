package com.senorpez.projectcars;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.TelemetryDataPacket.State.FINISHED;
import static com.senorpez.projectcars.TelemetryDataPacket.State.LOADING;
import static com.senorpez.projectcars.TelemetryDataPacket.State.PRE_RACE;

public class Race implements Iterator<Packet> {
    private final Deque<Packet> racePackets = new ArrayDeque<>();
    private final Set<Driver> drivers = new HashSet<>();

    public Race(Telemetry telemetry) {
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

        this.next();
    }

    public Deque<Packet> getRacePackets() {
        return racePackets;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public Boolean completeRace() {
        TelemetryDataPacket firstPacket = (TelemetryDataPacket) racePackets.getFirst();
        TelemetryDataPacket lastPacket = (TelemetryDataPacket) racePackets.getLast();

        return firstPacket.getState() == PRE_RACE && lastPacket.getState() == FINISHED;
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
        Packet packet = racePackets.removeFirst();

        if (packet instanceof TelemetryDataPacket) {
            TelemetryDataPacket telemetryDataPacket = (TelemetryDataPacket) packet;
            if (drivers.size() != ((TelemetryDataPacket) packet).getNumParticipants()) {
                populateDrivers(((TelemetryDataPacket) packet).getNumParticipants());
            }

            populateSectorTimes(telemetryDataPacket);
        }

        return packet;
    }

    private void populateSectorTimes(TelemetryDataPacket packet) {
        drivers.forEach(driver -> {
            TelemetryDataPacket.ParticipantInfo participantInfo = packet.getParticipantInfo().get(driver.getIndex());
            driver.addSectorTime(participantInfo.getCurrentSector(), participantInfo.getLastSectorTime());
        });
    }

    private void populateDrivers(Byte numParticipants) {
        Deque<Packet> readPackets = new ArrayDeque<>();

        try {
            while (drivers.size() != numParticipants) {
                Packet packet = racePackets.removeFirst();
                readPackets.addLast(packet);

                if (packet instanceof ParticipantPacket) {
                    ParticipantPacket participantPacket = (ParticipantPacket) packet;
                    AtomicInteger index = new AtomicInteger(0);
                    participantPacket.getNames().stream()
                            .limit(numParticipants)
                            .forEach(name -> drivers.add(new Driver(index.getAndIncrement(), name)));
                } else if (packet instanceof AdditionalParticipantPacket) {
                    AdditionalParticipantPacket participantPacket = (AdditionalParticipantPacket) packet;
                    AtomicInteger index = new AtomicInteger(participantPacket.getOffset());
                    participantPacket.getNames().stream()
                            .limit(participantPacket.getOffset() % 16)
                            .forEach(s -> drivers.add(new Driver(index.getAndIncrement(), s)));
                }
            }
        } catch (NoSuchElementException e) {
            /* Driver names never populated, so make fake names. */
            IntStream.rangeClosed(1, numParticipants)
                    .forEach(value -> drivers.add(new Driver(value, String.format("Driver %d", value))));
        }

        /* Repopulate race deque. */
        while (readPackets.size() > 0) {
            racePackets.addFirst(readPackets.removeLast());
        }
    }
}
