package com.senorpez.projectcars.racereport;

import com.senorpez.projectcars.racedata.Race;
import com.senorpez.projectcars.racedata.Telemetry;

import java.nio.file.Paths;

public class Thing {
    public static void main(final String[] args) {
        final Telemetry telemetry = new Telemetry(Paths.get(args[0]));
        while (telemetry.hasNext()) {
            final Race race = new Race(telemetry);

            while (race.hasNext()) {
                race.next();
                System.out.println(race.getElapsedTime());
            }
        }
    }
}
