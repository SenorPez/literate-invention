package com.senorpez.projectcars.racedata;

public enum TyreFlags {
    TYRE_ATTACHED(1),
    TYRE_INFLATED(1 << 1),
    TYRE_IS_ON_GROUND(1 << 2);

    private final Byte stateValue;

    TyreFlags(final int stateValue) {
        this.stateValue = (byte) stateValue;
    }

    Boolean isSet(final Short carFlags) {
        return (stateValue & carFlags) != 0;
    }
}
