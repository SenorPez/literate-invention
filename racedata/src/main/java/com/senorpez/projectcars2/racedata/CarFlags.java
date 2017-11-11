package com.senorpez.projectcars2.racedata;

enum CarFlags {
    CAR_HEADLIGHT(1),
    CAR_ENGINE_ACTIVE(1 << 1),
    CAR_ENGINE_WARNING(1 << 2),
    CAR_SPEED_LIMITER(1 << 3),
    CAR_ABS(1 << 4),
    CAR_HANDBRAKE(1 << 5);

    private final byte stateValue;

    CarFlags(final int stateValue) {
        this.stateValue = (byte) stateValue;
    }

    boolean isSet(final short carFlags) {
        return (stateValue & carFlags) != 0;
    }
}
