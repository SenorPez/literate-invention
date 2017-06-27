package com.senorpez.projectcars;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TelemetryDataPacket extends Packet {
    enum State {
        /*
        State transition graph:
        LOADING -> PRE_RACE_PAUSED
        PRE_RACE_PAUSED -> PRE_RACE
        PRE_RACE -> RACING
        RACING -> LOADING
               -> FINISHED
         */
        LOADING {  /* Not sure on this. Could be Restarting? */
            @Override
            public Boolean raceFinished(final State previousState) {
                return previousState == State.RACING;
            }
        },
        PRE_RACE_PAUSED,
        PRE_RACE {
            @Override
            public Boolean raceStarted() {
                return true;
            }
        },
        RACING,
        FINISHED {
            @Override
            public Boolean raceFinished(final State previousState) {
                return false;
            }

            @Override
            public Boolean raceFinishedTelemetryExhausted() {
                return true;
            }
        },
        UNDEFINED {
            @Override
            public Boolean raceFinishedTelemetryExhausted() {
                return true;
            }
        };

        public Boolean raceStarted() {
            return false;
        }

        public Boolean raceFinished(final State previousState) {
            return previousState == State.FINISHED;
        }

        public Boolean raceFinishedTelemetryExhausted() {
            return false;
        }
    }

    class ParticipantInfo {
        private final List<Short> worldPosition;
        private final Integer currentLapDistance;
        private final Short racePosition;
        private final Short lapsCompleted;
        private final Short currentLap;
        private final Short sector;
        private final Float lastSectorTime;

        ParticipantInfo(DataInputStream data) throws IOException {
            this.worldPosition = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Short>) value -> data.readShort()).collect(Collectors.toList()));
            this.currentLapDistance = data.readUnsignedShort();
            this.racePosition = (short) data.readUnsignedByte();
            this.lapsCompleted = (short) data.readUnsignedByte();
            this.currentLap = (short) data.readUnsignedByte();
            this.sector = (short) data.readUnsignedByte();
            this.lastSectorTime = getLittleFloat(data);
        }

        public List<Float> getWorldPosition() {
            return Collections.unmodifiableList(
                    Stream
                            .of(
                                    worldPosition.get(0) + ((sector >>> 6) / 4.0f),
                                    (float) worldPosition.get(1),
                                    worldPosition.get(2) + (((48 & sector) >>> 4) / 4.0f))
                            .collect(Collectors.toList()));
        }

        public Integer getCurrentLapDistance() {
            return currentLapDistance;
        }

        public Short getRacePosition() {
            Integer mask = 127; /* 0111 1111 */
            return Integer.valueOf(mask & racePosition).shortValue();
        }

        public Boolean isActive() {
            Integer mask = 128; /* 1000 0000 */
            return (mask & racePosition) != 0;
        }

        public Short getLapsCompleted() {
            Integer mask = 127; /* 0111 1111 */
            return Integer.valueOf(mask * lapsCompleted).shortValue();
        }

        public Boolean isLapInvalidated() {
            Integer mask = 128; /* 1000 0000 */
            return (mask & lapsCompleted) != 0;
        }

        public Short getCurrentLap() {
            return currentLap;
        }

        public CurrentSector getCurrentSector() {
            Integer mask = 7; /* 0000 0111 */
            return CurrentSector.valueOf(mask & sector);
        }

        public Boolean isSameClass() {
            Integer mask = 8; /* 0000 1000 */
            return (mask & sector) != 0;
        }

        public Float getLastSectorTime() {
            return lastSectorTime;
        }
    }

    private final static PacketType packetType = PacketType.TELEMETRY_DATA;

    private final Short gameSessionState;

    private final Byte viewedParticipantIndex;
    private final Byte numParticipants;

    private final Short unfilteredThrottle;
    private final Short unfilteredBrake;
    private final Byte unfilteredSteering;
    private final Short unfilteredClutch;
    private final Short raceStateFlags;

    private final Short lapsInEvent;

    private final Float bestLapTime;
    private final Float lastLapTime;
    private final Float currentTime;
    private final Float splitTimeAhead;
    private final Float splitTimeBehind;
    private final Float splitTime;
    private final Float eventTimeRemaining;
    private final Float personalFastestLapTime;
    private final Float worldFastestLapTime;
    private final Float currentSector1Time;
    private final Float currentSector2Time;
    private final Float currentSector3Time;
    private final Float fastestSector1Time;
    private final Float fastestSector2Time;
    private final Float fastestSector3Time;
    private final Float personalFastestSector1Time;
    private final Float personalFastestSector2Time;
    private final Float personalFastestSector3Time;
    private final Float worldFastestSector1Time;
    private final Float worldFastestSector2Time;
    private final Float worldFastestSector3Time;

    private final Integer joyPad;

    private final Short highestFlag;

    private final Short pitModeSchedule;

    private final Short oilTemp;
    private final Integer oilPressure;
    private final Short waterTemp;
    private final Integer waterPressure;
    private final Integer fuelPressure;
    private final Short carFlags;
    private final Short fuelCapacity;
    private final Short brake;
    private final Short throttle;
    private final Short clutch;
    private final Byte steering;
    private final Float fuelLevel;
    private final Float speed;
    private final Integer rpm;
    private final Integer maxRpm;
    private final Short gearNumGears;
    private final Short boostAmount;
    private final Byte enforcedPitStopLap;
    private final Short crashState;

    private final Float odometer;
    private final List<Float> orientation;
    private final List<Float> localVelocity;
    private final List<Float> worldVelocity;
    private final List<Float> angularVelocity;
    private final List<Float> localAcceleration;
    private final List<Float> worldAcceleration;
    private final List<Float> extentsCentre;

    private final List<Short> tyreFlags;
    private final List<Short> terrain;
    private final List<Float> tyreY;
    private final List<Float> tyreRps;
    private final List<Float> tyreSlipSpeed;
    private final List<Short> tyreTemp;
    private final List<Short> tyreGrip;
    private final List<Float> tyreHeightAboveGround;
    private final List<Float> tyreLateralStiffness;
    private final List<Short> tyreWear;
    private final List<Short> brakeDamage;
    private final List<Short> suspensionDamage;
    private final List<Short> brakeTemp;
    private final List<Integer> tyreTreadTemp;
    private final List<Integer> tyreLayerTemp;
    private final List<Integer> tyreCarcassTemp;
    private final List<Integer> tyreRimTemp;
    private final List<Integer> tyreInternalAirTemp;
    private final List<Float> wheelLocalPositionY;
    private final List<Float> rideHeight;
    private final List<Float> suspensionTravel;
    private final List<Float> suspensionVelocity;
    private final List<Integer> airPressure;

    private final Float engineSpeed;
    private final Float engineTorque;

    private final Short aeroDamage;
    private final Short engineDamage;

    private final Byte ambientTemperature;
    private final Byte trackTemperature;
    private final Short rainDensity;
    private final Byte windSpeed;
    private final Byte windDirectionX;
    private final Byte windDirectionY;

    private final List<ParticipantInfo> participantInfo;

    private final Float trackLength;
    private final List<Short> wings;
    private final Short dPad;

    TelemetryDataPacket(DataInputStream data) throws IOException, InvalidPacketException {
        super(data);
        if (!isCorrectPacketType(packetType)) {
            throw new InvalidPacketException();
        }

        this.gameSessionState = (short) data.readUnsignedByte();

        this.viewedParticipantIndex = data.readByte();
        this.numParticipants = data.readByte();

        this.unfilteredThrottle = (short) data.readUnsignedByte();
        this.unfilteredBrake = (short) data.readUnsignedByte();
        this.unfilteredSteering = data.readByte();
        this.unfilteredClutch = (short) data.readUnsignedByte();
        this.raceStateFlags = (short) data.readUnsignedByte();

        this.lapsInEvent = (short) data.readUnsignedByte();

        this.bestLapTime = getLittleFloat(data);
        this.lastLapTime = getLittleFloat(data);
        this.currentTime = getLittleFloat(data);
        this.splitTimeAhead = getLittleFloat(data);
        this.splitTimeBehind = getLittleFloat(data);
        this.splitTime = getLittleFloat(data);
        this.eventTimeRemaining = getLittleFloat(data);
        this.personalFastestLapTime = getLittleFloat(data);
        this.worldFastestLapTime = getLittleFloat(data);
        this.currentSector1Time = getLittleFloat(data);
        this.currentSector2Time = getLittleFloat(data);
        this.currentSector3Time = getLittleFloat(data);
        this.fastestSector1Time = getLittleFloat(data);
        this.fastestSector2Time = getLittleFloat(data);
        this.fastestSector3Time = getLittleFloat(data);
        this.personalFastestSector1Time = getLittleFloat(data);
        this.personalFastestSector2Time = getLittleFloat(data);
        this.personalFastestSector3Time = getLittleFloat(data);
        this.worldFastestSector1Time = getLittleFloat(data);
        this.worldFastestSector2Time = getLittleFloat(data);
        this.worldFastestSector3Time = getLittleFloat(data);

        this.joyPad = data.readUnsignedShort();

        this.highestFlag = (short) data.readUnsignedByte();

        this.pitModeSchedule = (short) data.readUnsignedByte();

        this.oilTemp = data.readShort();
        this.oilPressure = data.readUnsignedShort();
        this.waterTemp = data.readShort();
        this.waterPressure = data.readUnsignedShort();
        this.fuelPressure = data.readUnsignedShort();
        this.carFlags = (short) data.readUnsignedByte();
        this.fuelCapacity = (short) data.readUnsignedByte();
        this.brake = (short) data.readUnsignedByte();
        this.throttle = (short) data.readUnsignedByte();
        this.clutch = (short) data.readUnsignedByte();
        this.steering = data.readByte();
        this.fuelLevel = getLittleFloat(data);
        this.speed = getLittleFloat(data);
        this.rpm = data.readUnsignedShort();
        this.maxRpm = data.readUnsignedShort();
        this.gearNumGears = (short) data.readUnsignedByte();
        this.boostAmount = (short) data.readUnsignedByte();
        this.enforcedPitStopLap = data.readByte();
        this.crashState = (short) data.readUnsignedByte();

        this.odometer = getLittleFloat(data);
        this.orientation = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.localVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.worldVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.angularVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.localAcceleration = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.worldAcceleration = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.extentsCentre = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));

        this.tyreFlags = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.terrain = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.tyreY = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.tyreRps = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.tyreSlipSpeed = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.tyreTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.tyreGrip = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.tyreHeightAboveGround = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.tyreLateralStiffness = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.tyreWear = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.brakeDamage = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.suspensionDamage = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.brakeTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Short>) value -> data.readShort()).collect(Collectors.toList()));
        this.tyreTreadTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));
        this.tyreLayerTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));
        this.tyreCarcassTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));
        this.tyreRimTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));
        this.tyreInternalAirTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));
        this.wheelLocalPositionY = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.rideHeight = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.suspensionTravel = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.suspensionVelocity = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Float>) value -> getLittleFloat(data)).collect(Collectors.toList()));
        this.airPressure = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj((IntFunctionThrows<Integer>) value -> data.readUnsignedShort()).collect(Collectors.toList()));

        this.engineSpeed = getLittleFloat(data);
        this.engineTorque = getLittleFloat(data);

        this.aeroDamage = (short) data.readUnsignedByte();
        this.engineDamage = (short) data.readUnsignedByte();

        this.ambientTemperature = data.readByte();
        this.trackTemperature = data.readByte();
        this.rainDensity = (short) data.readUnsignedByte();
        this.windSpeed = data.readByte();
        this.windDirectionX = data.readByte();
        this.windDirectionY = data.readByte();

        this.participantInfo = Collections.unmodifiableList(IntStream.range(0, 56).mapToObj((IntFunctionThrows<ParticipantInfo>) value -> new ParticipantInfo(data)).collect(Collectors.toList()));

        this.trackLength = getLittleFloat(data);
        this.wings = Collections.unmodifiableList(IntStream.range(0, 2).mapToObj((IntFunctionThrows<Short>) value -> (short) data.readUnsignedByte()).collect(Collectors.toList()));
        this.dPad = (short) data.readUnsignedByte();
    }

    @Override
    public PacketType getPacketType() {
        return packetType;
    }

    public GameState getGameState() {
        Integer mask = 15; /* 0000 1111 */
        return GameState.valueOf(mask & gameSessionState);
    }

    public SessionState getSessionState() {
        return SessionState.valueOf(gameSessionState >>> 4);
    }

    public RaceState getRaceState() {
        Integer mask = 7; /* 0000 0111 */
        return RaceState.valueOf(mask & raceStateFlags);
    }

    public Boolean isLapInvalidated() {
        Integer mask = 8; /* 0000 1000 */
        return (mask & raceStateFlags) != 0;
    }

    public Boolean isAntiLockActive() {
        Integer mask = 16; /* 0001 0000 */
        return (mask & raceStateFlags) != 0;
    }

    public Boolean isBoostActive() {
        Integer mask = 32; /* 0010 0000 */
        return (mask & raceStateFlags) != 0;
    }

    public Byte getViewedParticipantIndex() {
        return viewedParticipantIndex;
    }

    public Byte getNumParticipants() {
        return numParticipants;
    }

    public Float getUnfilteredThrottle() {
        return unfilteredThrottle / 255.0f;
    }

    public Float getUnfilteredBrake() {
        return unfilteredBrake / 255.0f;
    }

    public Float getUnfilteredSteering() {
        return unfilteredSteering / 127.0f;
    }

    public Float getUnfilteredClutch() {
        return unfilteredClutch / 255.0f;
    }

    public Float getTrackLength() {
        return trackLength;
    }

    public Short getLapsInEvent() {
        return lapsInEvent;
    }

    public Float getBestLapTime() {
        return bestLapTime;
    }

    public Float getLastLapTime() {
        return lastLapTime;
    }

    public Float getCurrentTime() {
        return currentTime;
    }

    public Float getSplitTimeAhead() {
        return splitTimeAhead;
    }

    public Float getSplitTimeBehind() {
        return splitTimeBehind;
    }

    public Float getSplitTime() {
        return splitTime;
    }

    public Float getEventTimeRemaining() {
        return eventTimeRemaining;
    }

    public Float getPersonalFastestLapTime() {
        return personalFastestLapTime;
    }

    public Float getWorldFastestLapTime() {
        return worldFastestLapTime;
    }

    public Float getCurrentSector1Time() {
        return currentSector1Time;
    }

    public Float getCurrentSector2Time() {
        return currentSector2Time;
    }

    public Float getCurrentSector3Time() {
        return currentSector3Time;
    }

    public Float getFastestSector1Time() {
        return fastestSector1Time;
    }

    public Float getFastestSector2Time() {
        return fastestSector2Time;
    }

    public Float getFastestSector3Time() {
        return fastestSector3Time;
    }

    public Float getPersonalFastestSector1Time() {
        return personalFastestSector1Time;
    }

    public Float getPersonalFastestSector2Time() {
        return personalFastestSector2Time;
    }

    public Float getPersonalFastestSector3Time() {
        return personalFastestSector3Time;
    }

    public Float getWorldFastestSector1Time() {
        return worldFastestSector1Time;
    }

    public Float getWorldFastestSector2Time() {
        return worldFastestSector2Time;
    }

    public Float getWorldFastestSector3Time() {
        return worldFastestSector3Time;
    }

    public FlagColour getHighestFlagColor() {
        Integer mask = 15; /* 0000 1111 */
        return FlagColour.valueOf(mask & highestFlag);
    }

    public FlagReason getHighestFlagReason() {
        return FlagReason.valueOf(highestFlag >>> 4);
    }

    public PitMode getPitMode() {
        Integer mask = 15; /* 0000 1111 */
        return PitMode.valueOf(mask & pitModeSchedule);
    }

    public PitSchedule getPitSchedule() {
        return PitSchedule.valueOf(pitModeSchedule >>> 4);
    }

    public Boolean isHeadlight() {
        return CarFlags.CAR_HEADLIGHT.isSet(carFlags);
    }

    public Boolean isEngineActive() {
        return CarFlags.CAR_ENGINE_ACTIVE.isSet(carFlags);
    }

    public Boolean isEngineWarning() {
        return CarFlags.CAR_ENGINE_WARNING.isSet(carFlags);
    }

    public Boolean isSpeedLimiter() {
        return CarFlags.CAR_SPEED_LIMITER.isSet(carFlags);
    }

    public Boolean isAbs() {
        return CarFlags.CAR_ABS.isSet(carFlags);
    }

    public Boolean isHandbrake() {
        return CarFlags.CAR_HANDBRAKE.isSet(carFlags);
    }

    public Boolean isStability() {
        return CarFlags.CAR_STABILITY.isSet(carFlags);
    }

    public Boolean isTractionControl() {
        return CarFlags.CAR_TRACTION_CONTROL.isSet(carFlags);
    }

    public Short getOilTemp() {
        return oilTemp;
    }

    public Integer getOilPressure() {
        return oilPressure;
    }

    public Short getWaterTemp() {
        return waterTemp;
    }

    public Integer getWaterPressure() {
        return waterPressure;
    }

    public Integer getFuelPressure() {
        return fuelPressure;
    }

    public Short getFuelCapacity() {
        return fuelCapacity;
    }

    public Float getBrake() {
        return brake / 255.0f;
    }

    public Float getFuelLevel() {
        return fuelLevel;
    }

    public Float getSpeed() {
        return speed;
    }

    public Integer getRpm() {
        return rpm;
    }

    public Integer getMaxRpm() {
        return maxRpm;
    }

    public Float getThrottle() {
        return throttle / 255.0f;
    }

    public Float getClutch() {
        return clutch / 255.0f;
    }

    public Float getSteering() {
        return steering / 127.0f;
    }

    public Short getGear() {
        Integer mask = 15; /* 0000 1111 */
        return Integer.valueOf(mask * gearNumGears).shortValue();
    }

    public Short getNumGears() {
        return Integer.valueOf(gearNumGears >>> 4).shortValue();
    }

    public Float getOdometer() {
        return odometer;
    }

    public Short getBoostAmount() {
        return boostAmount;
    }

    public List<Float> getOrientation() {
        return orientation;
    }

    public List<Float> getLocalVelocity() {
        return localVelocity;
    }

    public List<Float> getWorldVelocity() {
        return worldVelocity;
    }

    public List<Float> getAngularVelocity() {
        return angularVelocity;
    }

    public List<Float> getLocalAcceleration() {
        return localAcceleration;
    }

    public List<Float> getWorldAcceleration() {
        return worldAcceleration;
    }

    public List<Float> getExtentsCentre() {
        return extentsCentre;
    }

    public List<Boolean> isTyreAttached() {
        return Collections.unmodifiableList(tyreFlags.stream().map(TyreFlags.TYRE_ATTACHED::isSet).collect(Collectors.toList()));
    }

    public List<Boolean> isTyreInflated() {
        return Collections.unmodifiableList(tyreFlags.stream().map(TyreFlags.TYRE_INFLATED::isSet).collect(Collectors.toList()));
    }

    public List<Boolean> isTyreIsOnGround() {
        return Collections.unmodifiableList(tyreFlags.stream().map(TyreFlags.TYRE_IS_ON_GROUND::isSet).collect(Collectors.toList()));
    }

    public List<TerrainMaterial> getTerrain() {
        return Collections.unmodifiableList(terrain.stream().map(TerrainMaterial::valueOf).collect(Collectors.toList()));
    }

    public List<Float> getTyreY() {
        return tyreY;
    }

    public List<Float> getTyreRps() {
        return tyreRps;
    }

    public List<Float> getTyreSlipSpeed() {
        return tyreSlipSpeed;
    }

    public List<Short> getTyreTemp() {
        return tyreTemp;
    }

    public List<Float> getTyreGrip() {
        return Collections.unmodifiableList(tyreGrip.stream().map(tyreGrip -> tyreGrip / 255.0f).collect(Collectors.toList()));
    }

    public List<Float> getTyreHeightAboveGround() {
        return tyreHeightAboveGround;
    }

    public List<Float> getTyreLateralStiffness() {
        return tyreLateralStiffness;
    }

    public List<Float> getTyreWear() {
        return Collections.unmodifiableList(tyreWear.stream().map(tyreWear -> tyreWear / 255.0f).collect(Collectors.toList()));
    }

    public List<Float> getBrakeDamage() {
        return Collections.unmodifiableList(brakeDamage.stream().map(brakeDamage -> brakeDamage / 255.0f).collect(Collectors.toList()));
    }

    public List<Float> getSuspensionDamage() {
        return Collections.unmodifiableList(suspensionDamage.stream().map(suspensionDamage -> suspensionDamage / 255.0f).collect(Collectors.toList()));
    }

    public List<Short> getBrakeTemp() {
        return brakeTemp;
    }

    public List<Integer> getTyreTreadTemp() {
        return tyreTreadTemp;
    }

    public List<Integer> getTyreLayerTemp() {
        return tyreLayerTemp;
    }

    public List<Integer> getTyreCarcassTemp() {
        return tyreCarcassTemp;
    }

    public List<Integer> getTyreRimTemp() {
        return tyreRimTemp;
    }

    public List<Integer> getTyreInternalAirTemp() {
        return tyreInternalAirTemp;
    }

    public List<Float> getWheelLocalPositionY() {
        return wheelLocalPositionY;
    }

    public List<Float> getRideHeight() {
        return rideHeight;
    }

    public List<Float> getSuspensionTravel() {
        return suspensionTravel;
    }

    public List<Float> getSuspensionVelocity() {
        return suspensionVelocity;
    }

    public List<Integer> getAirPressure() {
        return airPressure;
    }

    public Float getEngineSpeed() {
        return engineSpeed;
    }

    public Float getEngineTorque() {
        return engineTorque;
    }

    public List<Short> getWings() {
        return wings;
    }

    public Byte getEnforcedPitStopLap() {
        return enforcedPitStopLap;
    }

    public CrashDamageState getCrashState() {
        Integer mask = 15; /* 0000 1111 */
        return CrashDamageState.valueOf(mask & crashState);
    }

    public Float getAeroDamage() {
        return aeroDamage / 255.0f;
    }

    public Float getEngineDamage() {
        return engineDamage / 255.0f;
    }

    public Byte getAmbientTemperature() {
        return ambientTemperature;
    }

    public Byte getTrackTemperature() {
        return trackTemperature;
    }

    public Short getRainDensity() {
        return rainDensity;
    }

    public Byte getWindSpeed() {
        return windSpeed;
    }

    public Byte getWindDirectionX() {
        return windDirectionX;
    }

    public Byte getWindDirectionY() {
        return windDirectionY;
    }

    public Boolean isJoyPadButton1() {
        return JoyPad.BUTTON_1.isSet(joyPad);
    }

    public Boolean isJoyPadButton2() {
        return JoyPad.BUTTON_2.isSet(joyPad);
    }

    public Boolean isJoyPadButton3() {
        return JoyPad.BUTTON_3.isSet(joyPad);
    }

    public Boolean isJoyPadButton4() {
        return JoyPad.BUTTON_4.isSet(joyPad);
    }

    public Boolean isJoyPadButton5() {
        return JoyPad.BUTTON_5.isSet(joyPad);
    }

    public Boolean isJoyPadButton6() {
        return JoyPad.BUTTON_6.isSet(joyPad);
    }

    public Boolean isJoyPadButton7() {
        return JoyPad.BUTTON_7.isSet(joyPad);
    }

    public Boolean isJoyPadButton8() {
        return JoyPad.BUTTON_8.isSet(joyPad);
    }

    public Boolean isJoyPadButton9() {
        return JoyPad.BUTTON_9.isSet(joyPad);
    }

    public Boolean isJoyPadButton10() {
        return JoyPad.BUTTON_10.isSet(joyPad);
    }

    public Boolean isJoyPadButton11() {
        return JoyPad.BUTTON_11.isSet(joyPad);
    }

    public Boolean isJoyPadButton12() {
        return JoyPad.BUTTON_12.isSet(joyPad);
    }

    public Boolean isJoyPadButton13() {
        return JoyPad.BUTTON_13.isSet(joyPad);
    }

    public Boolean isJoyPadButton14() {
        return JoyPad.BUTTON_14.isSet(joyPad);
    }

    public Boolean isJoyPadButton15() {
        return JoyPad.BUTTON_15.isSet(joyPad);
    }

    public Boolean isJoyPadButton16() {
        return JoyPad.BUTTON_16.isSet(joyPad);
    }

    public Boolean isDPadButton1() {
        return DPad.BUTTON_1.isSet((int) (dPad >>> 4));
    }

    public Boolean isDPadButton2() {
        return DPad.BUTTON_2.isSet((int) (dPad >>> 4));
    }

    public Boolean isDPadButton3() {
        return DPad.BUTTON_3.isSet((int) (dPad >>> 4));
    }

    public Boolean isDPadButton4() {
        return DPad.BUTTON_4.isSet((int) (dPad >>> 4));
    }

    public Boolean isDPadButton5() {
        return DPad.BUTTON_5.isSet((int) (crashState >>> 4));
    }

    public Boolean isDPadButton6() {
        return DPad.BUTTON_6.isSet((int) (crashState >>> 4));
    }

    public Boolean isDPadButton7() {
        return DPad.BUTTON_7.isSet((int) (crashState >>> 4));
    }

    public Boolean isDPadButton8() {
        return DPad.BUTTON_8.isSet((int) (crashState >>> 4));
    }

    public List<ParticipantInfo> getParticipantInfo() {
        return participantInfo;
    }

    public State getState() {
        if (getRaceState() == RaceState.RACE_NOT_STARTED) {
            if (getGameState() == GameState.GAME_INGAME_PAUSED) {
                return State.PRE_RACE_PAUSED;
            } else if (getGameState() == GameState.GAME_INGAME_PLAYING) {
                return State.PRE_RACE;
            }
        } else if (getRaceState() == RaceState.RACE_RACING) {
            return State.RACING;
        } else if (getRaceState() == RaceState.RACE_FINISHED) {
            return State.FINISHED;
        } else if (getGameState() == GameState.GAME_MAX
                && getSessionState() == SessionState.SESSION_INVALID
                && getRaceState() == RaceState.RACE_INVALID) {
            return State.LOADING;
        }
        System.out.printf("%s %s %s\n",
                getGameState(),
                getSessionState(),
                getRaceState());
        return State.UNDEFINED;
    }

    private static Float getLittleFloat(DataInputStream data) throws IOException {
        byte[] bytes = new byte[4];
        data.readFully(bytes);
        int asInt = (bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8)
                | ((bytes[2] & 0xFF) << 16)
                | ((bytes[3] & 0xFF) << 24);
        return Float.intBitsToFloat(asInt);
    }
}
