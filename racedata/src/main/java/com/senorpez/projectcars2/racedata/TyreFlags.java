package com.senorpez.projectcars2.racedata;

enum TyreFlags {
    TYRE_ATTACHED(1),
    TYRE_INFLATED(1 << 1),
    TYRE_IS_ON_GROUND(1 << 2);

    private final byte stateValue;

    TyreFlags(final int stateValue) {
        this.stateValue = (byte) stateValue;
    }

    boolean isSet(final short tyreFlags) {
        return (stateValue & tyreFlags) != 0;
    }
}
