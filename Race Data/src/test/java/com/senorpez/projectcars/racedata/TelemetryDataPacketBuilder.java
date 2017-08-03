package com.senorpez.projectcars.racedata;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TelemetryDataPacketBuilder extends PacketBuilder {
    private GameState expectedGameState = GameState.valueOf(random.nextInt(GameState.GAME_MAX.ordinal()));
    private SessionState expectedSessionState = SessionState.valueOf(random.nextInt(SessionState.SESSION_MAX.ordinal()));

    private Byte expectedViewedParticipantIndex = (byte) random.nextInt(Byte.MAX_VALUE);
    private Byte expectedNumParticipants = (byte) random.nextInt(Byte.MAX_VALUE);

    private Short expectedUnfilteredThrottle = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedUnfilteredBrake = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Byte expectedUnfilteredSteering = (byte) random.nextInt(Byte.MAX_VALUE);
    private Short expectedUnfilteredClutch = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private RaceState expectedRaceState = RaceState.valueOf(random.nextInt(RaceState.RACE_MAX.ordinal()));
    private Boolean expectedIsLapInvalidated = random.nextBoolean();
    private Boolean expectedIsAntiLockActive = random.nextBoolean();
    private Boolean expectedIsBoostActive = random.nextBoolean();

    private Short expectedLapsInEvent = (short) random.nextInt(MAX_UNSIGNED_BYTE);

    private Float expectedBestLapTime = random.nextFloat() * random.nextInt(10);
    private Float expectedLastLapTime = random.nextFloat() * random.nextInt(10);
    private Float expectedCurrentTime = random.nextFloat() * random.nextInt(10);
    private Float expectedSplitTimeAhead = random.nextFloat() * random.nextInt(10);
    private Float expectedSplitTimeBehind = random.nextFloat() * random.nextInt(10);
    private Float expectedSplitTime = random.nextFloat() * random.nextInt(10);
    private Float expectedEventTimeRemaining = random.nextFloat() * random.nextInt(10);
    private Float expectedPersonalFastestLapTime = random.nextFloat() * random.nextInt(10);
    private Float expectedWorldFastestLapTime = random.nextFloat() * random.nextInt(10);
    private Float expectedCurrentSector1Time = random.nextFloat() * random.nextInt(10);
    private Float expectedCurrentSector2Time = random.nextFloat() * random.nextInt(10);
    private Float expectedCurrentSector3Time = random.nextFloat() * random.nextInt(10);
    private Float expectedFastestSector1Time = random.nextFloat() * random.nextInt(10);
    private Float expectedFastestSector2Time = random.nextFloat() * random.nextInt(10);
    private Float expectedFastestSector3Time = random.nextFloat() * random.nextInt(10);
    private Float expectedPersonalFastestSector1Time = random.nextFloat() * random.nextInt(10);
    private Float expectedPersonalFastestSector2Time = random.nextFloat() * random.nextInt(10);
    private Float expectedPersonalFastestSector3Time = random.nextFloat() * random.nextInt(10);
    private Float expectedWorldFastestSector1Time = random.nextFloat() * random.nextInt(10);
    private Float expectedWorldFastestSector2Time = random.nextFloat() * random.nextInt(10);
    private Float expectedWorldFastestSector3Time = random.nextFloat() * random.nextInt(10);

    private Integer expectedJoyPad = random.nextInt(MAX_UNSIGNED_SHORT);

    private FlagColour expectedFlagColour = FlagColour.valueOf(random.nextInt(FlagColour.FLAG_COLOUR_MAX.ordinal()));
    private FlagReason expectedFlagReason = FlagReason.valueOf(random.nextInt(FlagReason.FLAG_REASON_MAX.ordinal()));

    private PitMode expectedPitMode = PitMode.valueOf(random.nextInt(PitMode.PIT_MODE_MAX.ordinal()));
    private PitSchedule expectedPitSchedule = PitSchedule.valueOf(random.nextInt(PitSchedule.PIT_SCHEDULE_MAX.ordinal()));

    private Short expectedOilTemp = (short) random.nextInt(Short.MAX_VALUE);
    private Integer expectedOilPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private Short expectedWaterTemp = (short) random.nextInt(Short.MAX_VALUE);
    private Integer expectedWaterPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private Integer expectedFuelPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private Short expectedCarFlags = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedFuelCapacity = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedBrake = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedThrottle = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedClutch = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Byte expectedSteering = (byte) random.nextInt(Byte.MAX_VALUE);
    private Float expectedFuelLevel = random.nextFloat();
    private Float expectedSpeed = random.nextFloat();
    private Integer expectedRpm = random.nextInt(MAX_UNSIGNED_SHORT);
    private Integer expectedMaxRpm = random.nextInt(MAX_UNSIGNED_SHORT);
    private Short expectedGear = (short) (random.nextInt(16));
    private Short expectedNumGears = (short) (random.nextInt(16));
    private Short expectedBoostAmount = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Byte expectedEnforcedPitStopLap = (byte) random.nextInt(Byte.MAX_VALUE);
    private Short expectedCrashState = (short) random.nextInt(CrashDamageState.CRASH_DAMAGE_MAX.ordinal());

    private Float expectedOdometer = random.nextFloat();
    private List<Float> expectedOrientation = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedLocalVelocity = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedWorldVelocity = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedAngularVelocity = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedLocalAcceleration = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedWorldAcceleration = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedExtentsCentre = IntStream.range(0, 3).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());

    private List<Short> expectedTyreFlags = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedTerrain = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(TerrainMaterial.TERRAIN_MAX.ordinal())).collect(Collectors.toList());
    private List<Float> expectedTyreY = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedTyreRps = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedTyreSlipSpeed = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Short> expectedTyreTemp = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedTyreGrip = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Float> expectedTyreHeightAboveGround = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedTyreLateralStiffness = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Short> expectedTyreWear = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedBrakeDamage = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedSuspensionDamage = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedBrakeTemp = IntStream.range(0, 4).mapToObj(v -> (short) random.nextInt(Short.MAX_VALUE)).collect(Collectors.toList());
    private List<Integer> expectedTyreTreadTemp = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreLayerTemp = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreCarcassTemp = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreRimTemp = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreInternalAirTemp = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Float> expectedWheelLocalPositionY = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedRideHeight = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedSuspensionTravel = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedSuspensionVelocity = IntStream.range(0, 4).mapToObj(v -> random.nextFloat()).collect(Collectors.toList());
    private List<Integer> expectedAirPressure = IntStream.range(0, 4).mapToObj(v -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());

    private Float expectedEngineSpeed = random.nextFloat();
    private Float expectedEngineTorque = random.nextFloat();

    private Short expectedAeroDamage = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Short expectedEngineDamage = (short) random.nextInt(MAX_UNSIGNED_BYTE);

    private Byte expectedAmbientTemperature = (byte) random.nextInt(Byte.MAX_VALUE);
    private Byte expectedTrackTemperature = (byte) random.nextInt(Byte.MAX_VALUE);
    private Short expectedRainDensity = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private Byte expectedWindSpeed = (byte) random.nextInt(Byte.MAX_VALUE);
    private Byte expectedWindDirectionX = (byte) random.nextInt(Byte.MAX_VALUE);
    private Byte expectedWindDirectionY = (byte) random.nextInt(Byte.MAX_VALUE);

    private Float expectedTrackLength = random.nextFloat();
    private List<Short> expectedWings = IntStream.range(0, 2).mapToObj(v -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private Short expectedDPad = (short) random.nextInt(16);

    TelemetryDataPacketBuilder() {
        super();
        this.setExpectedPacketType((short) 0);
    }

    GameState getExpectedGameState() {
        return expectedGameState;
    }

    TelemetryDataPacketBuilder setExpectedGameState(final GameState expectedGameState) {
        this.expectedGameState = expectedGameState;
        return this;
    }

    SessionState getExpectedSessionState() {
        return expectedSessionState;
    }

    TelemetryDataPacketBuilder setExpectedSessionState(final SessionState expectedSessionState) {
        this.expectedSessionState = expectedSessionState;
        return this;
    }

    Byte getExpectedViewedParticipantIndex() {
        return expectedViewedParticipantIndex;
    }

    TelemetryDataPacketBuilder setExpectedViewedParticipantIndex(final Byte expectedViewedParticipantIndex) {
        this.expectedViewedParticipantIndex = expectedViewedParticipantIndex;
        return this;
    }

    Byte getExpectedNumParticipants() {
        return expectedNumParticipants;
    }

    TelemetryDataPacketBuilder setExpectedNumParticipants(final Byte expectedNumParticipants) {
        this.expectedNumParticipants = expectedNumParticipants;
        return this;
    }

    Short getExpectedUnfilteredThrottle() {
        return expectedUnfilteredThrottle;
    }

    TelemetryDataPacketBuilder setExpectedUnfilteredThrottle(final Short expectedUnfilteredThrottle) {
        this.expectedUnfilteredThrottle = expectedUnfilteredThrottle;
        return this;
    }

    Short getExpectedUnfilteredBrake() {
        return expectedUnfilteredBrake;
    }

    TelemetryDataPacketBuilder setExpectedUnfilteredBrake(final Short expectedUnfilteredBrake) {
        this.expectedUnfilteredBrake = expectedUnfilteredBrake;
        return this;
    }

    Byte getExpectedUnfilteredSteering() {
        return expectedUnfilteredSteering;
    }

    TelemetryDataPacketBuilder setExpectedUnfilteredSteering(final Byte expectedUnfilteredSteering) {
        this.expectedUnfilteredSteering = expectedUnfilteredSteering;
        return this;
    }

    Short getExpectedUnfilteredClutch() {
        return expectedUnfilteredClutch;
    }

    TelemetryDataPacketBuilder setExpectedUnfilteredClutch(final Short expectedUnfilteredClutch) {
        this.expectedUnfilteredClutch = expectedUnfilteredClutch;
        return this;
    }

    RaceState getExpectedRaceState() {
        return expectedRaceState;
    }

    TelemetryDataPacketBuilder setExpectedRaceState(final RaceState expectedRaceState) {
        this.expectedRaceState = expectedRaceState;
        return this;
    }

    Boolean getExpectedIsLapInvalidated() {
        return expectedIsLapInvalidated;
    }

    TelemetryDataPacketBuilder setExpectedIsLapInvalidated(final Boolean expectedIsLapInvalidated) {
        this.expectedIsLapInvalidated = expectedIsLapInvalidated;
        return this;
    }

    Boolean getExpectedIsAntiLockActive() {
        return expectedIsAntiLockActive;
    }

    TelemetryDataPacketBuilder setExpectedIsAntiLockActive(final Boolean expectedIsAntiLockActive) {
        this.expectedIsAntiLockActive = expectedIsAntiLockActive;
        return this;
    }

    Boolean getExpectedIsBoostActive() {
        return expectedIsBoostActive;
    }

    TelemetryDataPacketBuilder setExpectedIsBoostActive(final Boolean expectedIsBoostActive) {
        this.expectedIsBoostActive = expectedIsBoostActive;
        return this;
    }

    Short getExpectedLapsInEvent() {
        return expectedLapsInEvent;
    }

    TelemetryDataPacketBuilder setExpectedLapsInEvent(final Short expectedLapsInEvent) {
        this.expectedLapsInEvent = expectedLapsInEvent;
        return this;
    }

    Float getExpectedBestLapTime() {
        return expectedBestLapTime;
    }

    TelemetryDataPacketBuilder setExpectedBestLapTime(final Float expectedBestLapTime) {
        this.expectedBestLapTime = expectedBestLapTime;
        return this;
    }

    Float getExpectedLastLapTime() {
        return expectedLastLapTime;
    }

    TelemetryDataPacketBuilder setExpectedLastLapTime(final Float expectedLastLapTime) {
        this.expectedLastLapTime = expectedLastLapTime;
        return this;
    }

    Float getExpectedCurrentTime() {
        return expectedCurrentTime;
    }

    TelemetryDataPacketBuilder setExpectedCurrentTime(final Float expectedCurrentTime) {
        this.expectedCurrentTime = expectedCurrentTime;
        return this;
    }

    Float getExpectedSplitTimeAhead() {
        return expectedSplitTimeAhead;
    }

    TelemetryDataPacketBuilder setExpectedSplitTimeAhead(final Float expectedSplitTimeAhead) {
        this.expectedSplitTimeAhead = expectedSplitTimeAhead;
        return this;
    }

    Float getExpectedSplitTimeBehind() {
        return expectedSplitTimeBehind;
    }

    TelemetryDataPacketBuilder setExpectedSplitTimeBehind(final Float expectedSplitTimeBehind) {
        this.expectedSplitTimeBehind = expectedSplitTimeBehind;
        return this;
    }

    Float getExpectedSplitTime() {
        return expectedSplitTime;
    }

    TelemetryDataPacketBuilder setExpectedSplitTime(final Float expectedSplitTime) {
        this.expectedSplitTime = expectedSplitTime;
        return this;
    }

    Float getExpectedEventTimeRemaining() {
        return expectedEventTimeRemaining;
    }

    TelemetryDataPacketBuilder setExpectedEventTimeRemaining(final Float expectedEventTimeRemaining) {
        this.expectedEventTimeRemaining = expectedEventTimeRemaining;
        return this;
    }

    Float getExpectedPersonalFastestLapTime() {
        return expectedPersonalFastestLapTime;
    }

    TelemetryDataPacketBuilder setExpectedPersonalFastestLapTime(final Float expectedPersonalFastestLapTime) {
        this.expectedPersonalFastestLapTime = expectedPersonalFastestLapTime;
        return this;
    }

    Float getExpectedWorldFastestLapTime() {
        return expectedWorldFastestLapTime;
    }

    TelemetryDataPacketBuilder setExpectedWorldFastestLapTime(final Float expectedWorldFastestLapTime) {
        this.expectedWorldFastestLapTime = expectedWorldFastestLapTime;
        return this;
    }

    Float getExpectedCurrentSector1Time() {
        return expectedCurrentSector1Time;
    }

    TelemetryDataPacketBuilder setExpectedCurrentSector1Time(final Float expectedCurrentSector1Time) {
        this.expectedCurrentSector1Time = expectedCurrentSector1Time;
        return this;
    }

    Float getExpectedCurrentSector2Time() {
        return expectedCurrentSector2Time;
    }

    TelemetryDataPacketBuilder setExpectedCurrentSector2Time(final Float expectedCurrentSector2Time) {
        this.expectedCurrentSector2Time = expectedCurrentSector2Time;
        return this;
    }

    Float getExpectedCurrentSector3Time() {
        return expectedCurrentSector3Time;
    }

    TelemetryDataPacketBuilder setExpectedCurrentSector3Time(final Float expectedCurrentSector3Time) {
        this.expectedCurrentSector3Time = expectedCurrentSector3Time;
        return this;
    }

    Float getExpectedFastestSector1Time() {
        return expectedFastestSector1Time;
    }

    TelemetryDataPacketBuilder setExpectedFastestSector1Time(final Float expectedFastestSector1Time) {
        this.expectedFastestSector1Time = expectedFastestSector1Time;
        return this;
    }

    Float getExpectedFastestSector2Time() {
        return expectedFastestSector2Time;
    }

    TelemetryDataPacketBuilder setExpectedFastestSector2Time(final Float expectedFastestSector2Time) {
        this.expectedFastestSector2Time = expectedFastestSector2Time;
        return this;
    }

    Float getExpectedFastestSector3Time() {
        return expectedFastestSector3Time;
    }

    TelemetryDataPacketBuilder setExpectedFastestSector3Time(final Float expectedFastestSector3Time) {
        this.expectedFastestSector3Time = expectedFastestSector3Time;
        return this;
    }

    Float getExpectedPersonalFastestSector1Time() {
        return expectedPersonalFastestSector1Time;
    }

    TelemetryDataPacketBuilder setExpectedPersonalFastestSector1Time(final Float expectedPersonalFastestSector1Time) {
        this.expectedPersonalFastestSector1Time = expectedPersonalFastestSector1Time;
        return this;
    }

    Float getExpectedPersonalFastestSector2Time() {
        return expectedPersonalFastestSector2Time;
    }

    TelemetryDataPacketBuilder setExpectedPersonalFastestSector2Time(final Float expectedPersonalFastestSector2Time) {
        this.expectedPersonalFastestSector2Time = expectedPersonalFastestSector2Time;
        return this;
    }

    Float getExpectedPersonalFastestSector3Time() {
        return expectedPersonalFastestSector3Time;
    }

    TelemetryDataPacketBuilder setExpectedPersonalFastestSector3Time(final Float expectedPersonalFastestSector3Time) {
        this.expectedPersonalFastestSector3Time = expectedPersonalFastestSector3Time;
        return this;
    }

    Float getExpectedWorldFastestSector1Time() {
        return expectedWorldFastestSector1Time;
    }

    TelemetryDataPacketBuilder setExpectedWorldFastestSector1Time(final Float expectedWorldFastestSector1Time) {
        this.expectedWorldFastestSector1Time = expectedWorldFastestSector1Time;
        return this;
    }

    Float getExpectedWorldFastestSector2Time() {
        return expectedWorldFastestSector2Time;
    }

    TelemetryDataPacketBuilder setExpectedWorldFastestSector2Time(final Float expectedWorldFastestSector2Time) {
        this.expectedWorldFastestSector2Time = expectedWorldFastestSector2Time;
        return this;
    }

    Float getExpectedWorldFastestSector3Time() {
        return expectedWorldFastestSector3Time;
    }

    TelemetryDataPacketBuilder setExpectedWorldFastestSector3Time(final Float expectedWorldFastestSector3Time) {
        this.expectedWorldFastestSector3Time = expectedWorldFastestSector3Time;
        return this;
    }

    Integer getExpectedJoyPad() {
        return expectedJoyPad;
    }

    TelemetryDataPacketBuilder setExpectedJoyPad(final Integer expectedJoyPad) {
        this.expectedJoyPad = expectedJoyPad;
        return this;
    }

    FlagColour getExpectedFlagColour() {
        return expectedFlagColour;
    }

    TelemetryDataPacketBuilder setExpectedFlagColour(final FlagColour expectedFlagColour) {
        this.expectedFlagColour = expectedFlagColour;
        return this;
    }

    FlagReason getExpectedFlagReason() {
        return expectedFlagReason;
    }

    TelemetryDataPacketBuilder setExpectedFlagReason(final FlagReason expectedFlagReason) {
        this.expectedFlagReason = expectedFlagReason;
        return this;
    }

    PitMode getExpectedPitMode() {
        return expectedPitMode;
    }

    TelemetryDataPacketBuilder setExpectedPitMode(final PitMode expectedPitMode) {
        this.expectedPitMode = expectedPitMode;
        return this;
    }

    PitSchedule getExpectedPitSchedule() {
        return expectedPitSchedule;
    }

    TelemetryDataPacketBuilder setExpectedPitSchedule(final PitSchedule expectedPitSchedule) {
        this.expectedPitSchedule = expectedPitSchedule;
        return this;
    }

    Short getExpectedOilTemp() {
        return expectedOilTemp;
    }

    TelemetryDataPacketBuilder setExpectedOilTemp(final Short expectedOilTemp) {
        this.expectedOilTemp = expectedOilTemp;
        return this;
    }

    Integer getExpectedOilPressure() {
        return expectedOilPressure;
    }

    TelemetryDataPacketBuilder setExpectedOilPressure(final Integer expectedOilPressure) {
        this.expectedOilPressure = expectedOilPressure;
        return this;
    }

    Short getExpectedWaterTemp() {
        return expectedWaterTemp;
    }

    TelemetryDataPacketBuilder setExpectedWaterTemp(final Short expectedWaterTemp) {
        this.expectedWaterTemp = expectedWaterTemp;
        return this;
    }

    Integer getExpectedWaterPressure() {
        return expectedWaterPressure;
    }

    TelemetryDataPacketBuilder setExpectedWaterPressure(final Integer expectedWaterPressure) {
        this.expectedWaterPressure = expectedWaterPressure;
        return this;
    }

    Integer getExpectedFuelPressure() {
        return expectedFuelPressure;
    }

    TelemetryDataPacketBuilder setExpectedFuelPressure(final Integer expectedFuelPressure) {
        this.expectedFuelPressure = expectedFuelPressure;
        return this;
    }

    Short getExpectedCarFlags() {
        return expectedCarFlags;
    }

    TelemetryDataPacketBuilder setExpectedCarFlags(final Short expectedCarFlags) {
        this.expectedCarFlags = expectedCarFlags;
        return this;
    }

    Short getExpectedFuelCapacity() {
        return expectedFuelCapacity;
    }

    TelemetryDataPacketBuilder setExpectedFuelCapacity(final Short expectedFuelCapacity) {
        this.expectedFuelCapacity = expectedFuelCapacity;
        return this;
    }

    Short getExpectedBrake() {
        return expectedBrake;
    }

    TelemetryDataPacketBuilder setExpectedBrake(final Short expectedBrake) {
        this.expectedBrake = expectedBrake;
        return this;
    }

    Short getExpectedThrottle() {
        return expectedThrottle;
    }

    TelemetryDataPacketBuilder setExpectedThrottle(final Short expectedThrottle) {
        this.expectedThrottle = expectedThrottle;
        return this;
    }

    Short getExpectedClutch() {
        return expectedClutch;
    }

    TelemetryDataPacketBuilder setExpectedClutch(final Short expectedClutch) {
        this.expectedClutch = expectedClutch;
        return this;
    }

    Byte getExpectedSteering() {
        return expectedSteering;
    }

    TelemetryDataPacketBuilder setExpectedSteering(final Byte expectedSteering) {
        this.expectedSteering = expectedSteering;
        return this;
    }

    Float getExpectedFuelLevel() {
        return expectedFuelLevel;
    }

    TelemetryDataPacketBuilder setExpectedFuelLevel(final Float expectedFuelLevel) {
        this.expectedFuelLevel = expectedFuelLevel;
        return this;
    }

    Float getExpectedSpeed() {
        return expectedSpeed;
    }

    TelemetryDataPacketBuilder setExpectedSpeed(final Float expectedSpeed) {
        this.expectedSpeed = expectedSpeed;
        return this;
    }

    Integer getExpectedRpm() {
        return expectedRpm;
    }

    TelemetryDataPacketBuilder setExpectedRpm(final Integer expectedRpm) {
        this.expectedRpm = expectedRpm;
        return this;
    }

    Integer getExpectedMaxRpm() {
        return expectedMaxRpm;
    }

    TelemetryDataPacketBuilder setExpectedMaxRpm(final Integer expectedMaxRpm) {
        this.expectedMaxRpm = expectedMaxRpm;
        return this;
    }

    Short getExpectedGear() {
        return expectedGear;
    }

    TelemetryDataPacketBuilder setExpectedGear(final Short expectedGear) {
        this.expectedGear = expectedGear;
        return this;
    }

    Short getExpectedNumGears() {
        return expectedNumGears;
    }

    TelemetryDataPacketBuilder setExpectedNumGears(final Short expectedNumGears) {
        this.expectedNumGears = expectedNumGears;
        return this;
    }

    Short getExpectedBoostAmount() {
        return expectedBoostAmount;
    }

    TelemetryDataPacketBuilder setExpectedBoostAmount(final Short expectedBoostAmount) {
        this.expectedBoostAmount = expectedBoostAmount;
        return this;
    }

    Byte getExpectedEnforcedPitStopLap() {
        return expectedEnforcedPitStopLap;
    }

    TelemetryDataPacketBuilder setExpectedEnforcedPitStopLap(final Byte expectedEnforcedPitStopLap) {
        this.expectedEnforcedPitStopLap = expectedEnforcedPitStopLap;
        return this;
    }

    Short getExpectedCrashState() {
        return expectedCrashState;
    }

    TelemetryDataPacketBuilder setExpectedCrashState(final Short expectedCrashState) {
        this.expectedCrashState = expectedCrashState;
        return this;
    }

    Float getExpectedOdometer() {
        return expectedOdometer;
    }

    TelemetryDataPacketBuilder setExpectedOdometer(final Float expectedOdometer) {
        this.expectedOdometer = expectedOdometer;
        return this;
    }

    List<Float> getExpectedOrientation() {
        return expectedOrientation;
    }

    TelemetryDataPacketBuilder setExpectedOrientation(final List<Float> expectedOrientation) {
        this.expectedOrientation = expectedOrientation;
        return this;
    }

    List<Float> getExpectedLocalVelocity() {
        return expectedLocalVelocity;
    }

    TelemetryDataPacketBuilder setExpectedLocalVelocity(final List<Float> expectedLocalVelocity) {
        this.expectedLocalVelocity = expectedLocalVelocity;
        return this;
    }

    List<Float> getExpectedWorldVelocity() {
        return expectedWorldVelocity;
    }

    TelemetryDataPacketBuilder setExpectedWorldVelocity(final List<Float> expectedWorldVelocity) {
        this.expectedWorldVelocity = expectedWorldVelocity;
        return this;
    }

    List<Float> getExpectedAngularVelocity() {
        return expectedAngularVelocity;
    }

    TelemetryDataPacketBuilder setExpectedAngularVelocity(final List<Float> expectedAngularVelocity) {
        this.expectedAngularVelocity = expectedAngularVelocity;
        return this;
    }

    List<Float> getExpectedLocalAcceleration() {
        return expectedLocalAcceleration;
    }

    TelemetryDataPacketBuilder setExpectedLocalAcceleration(final List<Float> expectedLocalAcceleration) {
        this.expectedLocalAcceleration = expectedLocalAcceleration;
        return this;
    }

    List<Float> getExpectedWorldAcceleration() {
        return expectedWorldAcceleration;
    }

    TelemetryDataPacketBuilder setExpectedWorldAcceleration(final List<Float> expectedWorldAcceleration) {
        this.expectedWorldAcceleration = expectedWorldAcceleration;
        return this;
    }

    List<Float> getExpectedExtentsCentre() {
        return expectedExtentsCentre;
    }

    TelemetryDataPacketBuilder setExpectedExtentsCentre(final List<Float> expectedExtentsCentre) {
        this.expectedExtentsCentre = expectedExtentsCentre;
        return this;
    }

    List<Short> getExpectedTyreFlags() {
        return expectedTyreFlags;
    }

    TelemetryDataPacketBuilder setExpectedTyreFlags(final List<Short> expectedTyreFlags) {
        this.expectedTyreFlags = expectedTyreFlags;
        return this;
    }

    List<Short> getExpectedTerrain() {
        return expectedTerrain;
    }

    TelemetryDataPacketBuilder setExpectedTerrain(final List<Short> expectedTerrain) {
        this.expectedTerrain = expectedTerrain;
        return this;
    }

    List<Float> getExpectedTyreY() {
        return expectedTyreY;
    }

    TelemetryDataPacketBuilder setExpectedTyreY(final List<Float> expectedTyreY) {
        this.expectedTyreY = expectedTyreY;
        return this;
    }

    List<Float> getExpectedTyreRps() {
        return expectedTyreRps;
    }

    TelemetryDataPacketBuilder setExpectedTyreRps(final List<Float> expectedTyreRps) {
        this.expectedTyreRps = expectedTyreRps;
        return this;
    }

    List<Float> getExpectedTyreSlipSpeed() {
        return expectedTyreSlipSpeed;
    }

    TelemetryDataPacketBuilder setExpectedTyreSlipSpeed(final List<Float> expectedTyreSlipSpeed) {
        this.expectedTyreSlipSpeed = expectedTyreSlipSpeed;
        return this;
    }

    List<Short> getExpectedTyreTemp() {
        return expectedTyreTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreTemp(final List<Short> expectedTyreTemp) {
        this.expectedTyreTemp = expectedTyreTemp;
        return this;
    }

    List<Short> getExpectedTyreGrip() {
        return expectedTyreGrip;
    }

    TelemetryDataPacketBuilder setExpectedTyreGrip(final List<Short> expectedTyreGrip) {
        this.expectedTyreGrip = expectedTyreGrip;
        return this;
    }

    List<Float> getExpectedTyreHeightAboveGround() {
        return expectedTyreHeightAboveGround;
    }

    TelemetryDataPacketBuilder setExpectedTyreHeightAboveGround(final List<Float> expectedTyreHeightAboveGround) {
        this.expectedTyreHeightAboveGround = expectedTyreHeightAboveGround;
        return this;
    }

    List<Float> getExpectedTyreLateralStiffness() {
        return expectedTyreLateralStiffness;
    }

    TelemetryDataPacketBuilder setExpectedTyreLateralStiffness(final List<Float> expectedTyreLateralStiffness) {
        this.expectedTyreLateralStiffness = expectedTyreLateralStiffness;
        return this;
    }

    List<Short> getExpectedTyreWear() {
        return expectedTyreWear;
    }

    TelemetryDataPacketBuilder setExpectedTyreWear(final List<Short> expectedTyreWear) {
        this.expectedTyreWear = expectedTyreWear;
        return this;
    }

    List<Short> getExpectedBrakeDamage() {
        return expectedBrakeDamage;
    }

    TelemetryDataPacketBuilder setExpectedBrakeDamage(final List<Short> expectedBrakeDamage) {
        this.expectedBrakeDamage = expectedBrakeDamage;
        return this;
    }

    List<Short> getExpectedSuspensionDamage() {
        return expectedSuspensionDamage;
    }

    TelemetryDataPacketBuilder setExpectedSuspensionDamage(final List<Short> expectedSuspensionDamage) {
        this.expectedSuspensionDamage = expectedSuspensionDamage;
        return this;
    }

    List<Short> getExpectedBrakeTemp() {
        return expectedBrakeTemp;
    }

    TelemetryDataPacketBuilder setExpectedBrakeTemp(final List<Short> expectedBrakeTemp) {
        this.expectedBrakeTemp = expectedBrakeTemp;
        return this;
    }

    List<Integer> getExpectedTyreTreadTemp() {
        return expectedTyreTreadTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreTreadTemp(final List<Integer> expectedTyreTreadTemp) {
        this.expectedTyreTreadTemp = expectedTyreTreadTemp;
        return this;
    }

    List<Integer> getExpectedTyreLayerTemp() {
        return expectedTyreLayerTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreLayerTemp(final List<Integer> expectedTyreLayerTemp) {
        this.expectedTyreLayerTemp = expectedTyreLayerTemp;
        return this;
    }

    List<Integer> getExpectedTyreCarcassTemp() {
        return expectedTyreCarcassTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreCarcassTemp(final List<Integer> expectedTyreCarcassTemp) {
        this.expectedTyreCarcassTemp = expectedTyreCarcassTemp;
        return this;
    }

    List<Integer> getExpectedTyreRimTemp() {
        return expectedTyreRimTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreRimTemp(final List<Integer> expectedTyreRimTemp) {
        this.expectedTyreRimTemp = expectedTyreRimTemp;
        return this;
    }

    List<Integer> getExpectedTyreInternalAirTemp() {
        return expectedTyreInternalAirTemp;
    }

    TelemetryDataPacketBuilder setExpectedTyreInternalAirTemp(final List<Integer> expectedTyreInternalAirTemp) {
        this.expectedTyreInternalAirTemp = expectedTyreInternalAirTemp;
        return this;
    }

    List<Float> getExpectedWheelLocalPositionY() {
        return expectedWheelLocalPositionY;
    }

    TelemetryDataPacketBuilder setExpectedWheelLocalPositionY(final List<Float> expectedWheelLocalPositionY) {
        this.expectedWheelLocalPositionY = expectedWheelLocalPositionY;
        return this;
    }

    List<Float> getExpectedRideHeight() {
        return expectedRideHeight;
    }

    TelemetryDataPacketBuilder setExpectedRideHeight(final List<Float> expectedRideHeight) {
        this.expectedRideHeight = expectedRideHeight;
        return this;
    }

    List<Float> getExpectedSuspensionTravel() {
        return expectedSuspensionTravel;
    }

    TelemetryDataPacketBuilder setExpectedSuspensionTravel(final List<Float> expectedSuspensionTravel) {
        this.expectedSuspensionTravel = expectedSuspensionTravel;
        return this;
    }

    List<Float> getExpectedSuspensionVelocity() {
        return expectedSuspensionVelocity;
    }

    TelemetryDataPacketBuilder setExpectedSuspensionVelocity(final List<Float> expectedSuspensionVelocity) {
        this.expectedSuspensionVelocity = expectedSuspensionVelocity;
        return this;
    }

    List<Integer> getExpectedAirPressure() {
        return expectedAirPressure;
    }

    TelemetryDataPacketBuilder setExpectedAirPressure(final List<Integer> expectedAirPressure) {
        this.expectedAirPressure = expectedAirPressure;
        return this;
    }

    Float getExpectedEngineSpeed() {
        return expectedEngineSpeed;
    }

    TelemetryDataPacketBuilder setExpectedEngineSpeed(final Float expectedEngineSpeed) {
        this.expectedEngineSpeed = expectedEngineSpeed;
        return this;
    }

    Float getExpectedEngineTorque() {
        return expectedEngineTorque;
    }

    TelemetryDataPacketBuilder setExpectedEngineTorque(final Float expectedEngineTorque) {
        this.expectedEngineTorque = expectedEngineTorque;
        return this;
    }

    Short getExpectedAeroDamage() {
        return expectedAeroDamage;
    }

    TelemetryDataPacketBuilder setExpectedAeroDamage(final Short expectedAeroDamage) {
        this.expectedAeroDamage = expectedAeroDamage;
        return this;
    }

    Short getExpectedEngineDamage() {
        return expectedEngineDamage;
    }

    TelemetryDataPacketBuilder setExpectedEngineDamage(final Short expectedEngineDamage) {
        this.expectedEngineDamage = expectedEngineDamage;
        return this;
    }

    Byte getExpectedAmbientTemperature() {
        return expectedAmbientTemperature;
    }

    TelemetryDataPacketBuilder setExpectedAmbientTemperature(final Byte expectedAmbientTemperature) {
        this.expectedAmbientTemperature = expectedAmbientTemperature;
        return this;
    }

    Byte getExpectedTrackTemperature() {
        return expectedTrackTemperature;
    }

    TelemetryDataPacketBuilder setExpectedTrackTemperature(final Byte expectedTrackTemperature) {
        this.expectedTrackTemperature = expectedTrackTemperature;
        return this;
    }

    Short getExpectedRainDensity() {
        return expectedRainDensity;
    }

    TelemetryDataPacketBuilder setExpectedRainDensity(final Short expectedRainDensity) {
        this.expectedRainDensity = expectedRainDensity;
        return this;
    }

    Byte getExpectedWindSpeed() {
        return expectedWindSpeed;
    }

    TelemetryDataPacketBuilder setExpectedWindSpeed(final Byte expectedWindSpeed) {
        this.expectedWindSpeed = expectedWindSpeed;
        return this;
    }

    Byte getExpectedWindDirectionX() {
        return expectedWindDirectionX;
    }

    TelemetryDataPacketBuilder setExpectedWindDirectionX(final Byte expectedWindDirectionX) {
        this.expectedWindDirectionX = expectedWindDirectionX;
        return this;
    }

    Byte getExpectedWindDirectionY() {
        return expectedWindDirectionY;
    }

    TelemetryDataPacketBuilder setExpectedWindDirectionY(final Byte expectedWindDirectionY) {
        this.expectedWindDirectionY = expectedWindDirectionY;
        return this;
    }

    Float getExpectedTrackLength() {
        return expectedTrackLength;
    }

    TelemetryDataPacketBuilder setExpectedTrackLength(final Float expectedTrackLength) {
        this.expectedTrackLength = expectedTrackLength;
        return this;
    }

    List<Short> getExpectedWings() {
        return expectedWings;
    }

    TelemetryDataPacketBuilder setExpectedWings(final List<Short> expectedWings) {
        this.expectedWings = expectedWings;
        return this;
    }

    Short getExpectedDPad() {
        return expectedDPad;
    }

    TelemetryDataPacketBuilder setExpectedDPad(final Short expectedDPad) {
        this.expectedDPad = expectedDPad;
        return this;
    }

    @Override
    DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = build(new ByteArrayOutputStream(1367));

        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        final Byte gameSessionState = (byte) ((expectedSessionState.ordinal() << 4) | (expectedGameState.ordinal()));
        dataOutputStream.writeByte(gameSessionState);

        dataOutputStream.writeByte(expectedViewedParticipantIndex);
        dataOutputStream.writeByte(expectedNumParticipants);

        dataOutputStream.writeByte(expectedUnfilteredThrottle);
        dataOutputStream.writeByte(expectedUnfilteredBrake);
        dataOutputStream.writeByte(expectedUnfilteredSteering);
        dataOutputStream.writeByte(expectedUnfilteredClutch);
        final Byte raceStateFlags = (byte) (
                (expectedIsBoostActive ? 1 << 5 : 0)
                        | (expectedIsAntiLockActive ? 1 << 4 : 0)
                        | (expectedIsLapInvalidated ? 1 << 3 : 0)
                        | (expectedRaceState.ordinal()));
        dataOutputStream.writeByte(raceStateFlags);

        dataOutputStream.writeByte(expectedLapsInEvent);

        dataOutputStream.write(flipFloat(expectedBestLapTime));
        dataOutputStream.write(flipFloat(expectedLastLapTime));
        dataOutputStream.write(flipFloat(expectedCurrentTime));
        dataOutputStream.write(flipFloat(expectedSplitTimeAhead));
        dataOutputStream.write(flipFloat(expectedSplitTimeBehind));
        dataOutputStream.write(flipFloat(expectedSplitTime));
        dataOutputStream.write(flipFloat(expectedEventTimeRemaining));
        dataOutputStream.write(flipFloat(expectedPersonalFastestLapTime));
        dataOutputStream.write(flipFloat(expectedWorldFastestLapTime));
        dataOutputStream.write(flipFloat(expectedCurrentSector1Time));
        dataOutputStream.write(flipFloat(expectedCurrentSector2Time));
        dataOutputStream.write(flipFloat(expectedCurrentSector3Time));
        dataOutputStream.write(flipFloat(expectedFastestSector1Time));
        dataOutputStream.write(flipFloat(expectedFastestSector2Time));
        dataOutputStream.write(flipFloat(expectedFastestSector3Time));
        dataOutputStream.write(flipFloat(expectedPersonalFastestSector1Time));
        dataOutputStream.write(flipFloat(expectedPersonalFastestSector2Time));
        dataOutputStream.write(flipFloat(expectedPersonalFastestSector3Time));
        dataOutputStream.write(flipFloat(expectedWorldFastestSector1Time));
        dataOutputStream.write(flipFloat(expectedWorldFastestSector2Time));
        dataOutputStream.write(flipFloat(expectedWorldFastestSector3Time));

        dataOutputStream.writeShort(expectedJoyPad);

        final Byte highestFlagColor = (byte) ((expectedFlagReason.ordinal() << 4) | (expectedFlagColour.ordinal()));
        dataOutputStream.writeByte(highestFlagColor);

        final Byte pitModeSchedule = (byte) ((expectedPitSchedule.ordinal() << 4) | (expectedPitMode.ordinal()));
        dataOutputStream.writeByte(pitModeSchedule);

        dataOutputStream.writeShort(expectedOilTemp);
        dataOutputStream.writeShort(expectedOilPressure);
        dataOutputStream.writeShort(expectedWaterTemp);
        dataOutputStream.writeShort(expectedWaterPressure);
        dataOutputStream.writeShort(expectedFuelPressure);
        dataOutputStream.writeByte(expectedCarFlags);
        dataOutputStream.writeByte(expectedFuelCapacity);
        dataOutputStream.writeByte(expectedBrake);
        dataOutputStream.writeByte(expectedThrottle);
        dataOutputStream.writeByte(expectedClutch);
        dataOutputStream.writeByte(expectedSteering);
        dataOutputStream.write(flipFloat(expectedFuelLevel));
        dataOutputStream.write(flipFloat(expectedSpeed));
        dataOutputStream.writeShort(expectedRpm);
        dataOutputStream.writeShort(expectedMaxRpm);
        final Byte gearNumGears = (byte) ((expectedNumGears << 4) | (expectedGear));
        dataOutputStream.writeByte(gearNumGears);
        dataOutputStream.writeByte(expectedBoostAmount);
        dataOutputStream.writeByte(expectedEnforcedPitStopLap);
        dataOutputStream.writeByte(expectedCrashState);

        dataOutputStream.write(flipFloat(expectedOdometer));
        expectedOrientation.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedLocalVelocity.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedWorldVelocity.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedAngularVelocity.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedLocalAcceleration.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedWorldAcceleration.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedExtentsCentre.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });

        expectedTyreFlags.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTerrain.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreY.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreRps.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreSlipSpeed.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreTemp.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreGrip.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreHeightAboveGround.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreLateralStiffness.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreWear.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedBrakeDamage.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedSuspensionDamage.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedBrakeTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreTreadTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreLayerTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreCarcassTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreRimTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedTyreInternalAirTemp.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedWheelLocalPositionY.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedRideHeight.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedSuspensionTravel.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedSuspensionVelocity.forEach(v -> {
            try {
                dataOutputStream.write(flipFloat(v));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        expectedAirPressure.forEach(v -> {
            try {
                dataOutputStream.writeShort(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });

        dataOutputStream.write(flipFloat(expectedEngineSpeed));
        dataOutputStream.write(flipFloat(expectedEngineTorque));

        dataOutputStream.writeByte(expectedAeroDamage);
        dataOutputStream.writeByte(expectedEngineDamage);

        dataOutputStream.writeByte(expectedAmbientTemperature);
        dataOutputStream.writeByte(expectedTrackTemperature);
        dataOutputStream.writeByte(expectedRainDensity);
        dataOutputStream.writeByte(expectedWindSpeed);
        dataOutputStream.writeByte(expectedWindDirectionX);
        dataOutputStream.writeByte(expectedWindDirectionY);

        final byte[] participantData = new byte[896];
        random.nextBytes(participantData);
        dataOutputStream.write(participantData);

        dataOutputStream.write(flipFloat(expectedTrackLength));
        expectedWings.forEach(v -> {
            try {
                dataOutputStream.writeByte(v);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        dataOutputStream.writeByte(expectedDPad << 4);

        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
}
