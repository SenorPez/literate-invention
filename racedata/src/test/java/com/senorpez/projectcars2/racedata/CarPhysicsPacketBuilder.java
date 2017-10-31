package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class CarPhysicsPacketBuilder extends PacketBuilder {
    private final static Random random = new Random();
    private final static int TYRE_NAME_LENGTH_MAX = 40;

    private byte expectedViewedParticipantIndex = (byte) random.nextInt(Byte.MAX_VALUE);

    private short expectedUnfilteredThrottle = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedUnfilteredBrake = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private byte expectedUnfilteredSteering = (byte) random.nextInt(Byte.MAX_VALUE);
    private short expectedUnfilteredClutch = (short) random.nextInt(MAX_UNSIGNED_BYTE);

    private short expectedCarFlags = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedOilTemp = (short) random.nextInt(Short.MAX_VALUE);
    private int expectedOilPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private short expectedWaterTemp = (short) random.nextInt(Short.MAX_VALUE);
    private int expectedWaterPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private int expectedFuelPressure = random.nextInt(MAX_UNSIGNED_SHORT);
    private short expectedFuelCapacity = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedBrake = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedThrottle = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedClutch = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private float expectedFuelLevel = random.nextFloat();
    private float expectedSpeed = random.nextFloat();
    private int expectedRpm = random.nextInt(MAX_UNSIGNED_SHORT);
    private int expectedMaxRpm = random.nextInt(MAX_UNSIGNED_SHORT);
    private byte expectedSteering = (byte) random.nextInt(Byte.MAX_VALUE);
    private short expectedGearNumGears = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedBoostAmount = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedCrashState = (short) random.nextInt(CrashDamageState.CRASH_DAMAGE_MAX.ordinal());
    private float expectedOdometer = random.nextFloat();
    private List<Float> expectedOrientation = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedLocalVelocity = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedWorldVelocity = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedAngularVelocity = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedLocalAcceleration = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedWorldAcceleration = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedExtentsCentre = IntStream.range(0, 3).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Short> expectedTyreFlags = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedTerrain = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(Terrain.TERRAIN_MAX.ordinal())).collect(Collectors.toList());
    private List<Float> expectedTyreY = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedTyreRps = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Short> expectedTyreTemp = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Float> expectedTyreHeightAboveGround = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Short> expectedTyreWear = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedBrakeDamage = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedSuspensionDamage = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private List<Short> expectedBrakeTemp = IntStream.range(0, 4).mapToObj(value -> (short) random.nextInt(Short.MAX_VALUE)).collect(Collectors.toList());
    private List<Integer> expectedTyreTreadTemp = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreLayerTemp = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreCarcassTemp = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreRimTemp = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreInternalAirTemp = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreTempLeft = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreTempCenter = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedTyreTempRight = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Float> expectedWheelLocalPositionY = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedRideHeight = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedSuspensionTravel = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Float> expectedSuspensionVelocity = IntStream.range(0, 4).mapToObj(value -> random.nextFloat()).collect(Collectors.toList());
    private List<Integer> expectedSuspensionRideHeight = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private List<Integer> expectedAirPressure = IntStream.range(0, 4).mapToObj(value -> random.nextInt(MAX_UNSIGNED_SHORT)).collect(Collectors.toList());
    private float expectedEngineSpeed = random.nextFloat();
    private float expectedEngineTorque = random.nextFloat();
    private List<Short> expectedWings = IntStream.range(0, 2).mapToObj(value -> (short) random.nextInt(MAX_UNSIGNED_BYTE)).collect(Collectors.toList());
    private short expectedHandbrake = (short) random.nextInt(MAX_UNSIGNED_BYTE);

    private short expectedAeroDamage = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedEngineDamage = (short) random.nextInt(MAX_UNSIGNED_BYTE);

    private long expectedJoypad0 = getBoundedLong();
    private short expectedDPad = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private List<String> expectedTyreCompound = IntStream
            .range(0, 4)
            .mapToObj(value -> generateString(TYRE_NAME_LENGTH_MAX))
            .collect(Collectors.toList());

    CarPhysicsPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_CAR_PHYSICS.ordinal());
    }

    byte getExpectedViewedParticipantIndex() {
        return expectedViewedParticipantIndex;
    }

    CarPhysicsPacketBuilder setExpectedViewedParticipantIndex(final byte expectedViewedParticipantIndex) {
        this.expectedViewedParticipantIndex = expectedViewedParticipantIndex;
        return this;
    }

    short getExpectedUnfilteredThrottle() {
        return expectedUnfilteredThrottle;
    }

    CarPhysicsPacketBuilder setExpectedUnfilteredThrottle(final short expectedUnfilteredThrottle) {
        this.expectedUnfilteredThrottle = expectedUnfilteredThrottle;
        return this;
    }

    short getExpectedUnfilteredBrake() {
        return expectedUnfilteredBrake;
    }

    CarPhysicsPacketBuilder setExpectedUnfilteredBrake(final short expectedUnfilteredBrake) {
        this.expectedUnfilteredBrake = expectedUnfilteredBrake;
        return this;
    }

    byte getExpectedUnfilteredSteering() {
        return expectedUnfilteredSteering;
    }

    CarPhysicsPacketBuilder setExpectedUnfilteredSteering(final byte expectedUnfilteredSteering) {
        this.expectedUnfilteredSteering = expectedUnfilteredSteering;
        return this;
    }

    short getExpectedUnfilteredClutch() {
        return expectedUnfilteredClutch;
    }

    CarPhysicsPacketBuilder setExpectedUnfilteredClutch(final short expectedUnfilteredClutch) {
        this.expectedUnfilteredClutch = expectedUnfilteredClutch;
        return this;
    }

    short getExpectedCarFlags() {
        return expectedCarFlags;
    }

    CarPhysicsPacketBuilder setExpectedCarFlags(final short expectedCarFlags) {
        this.expectedCarFlags = expectedCarFlags;
        return this;
    }

    short getExpectedOilTemp() {
        return expectedOilTemp;
    }

    CarPhysicsPacketBuilder setExpectedOilTemp(final short expectedOilTemp) {
        this.expectedOilTemp = expectedOilTemp;
        return this;
    }

    int getExpectedOilPressure() {
        return expectedOilPressure;
    }

    CarPhysicsPacketBuilder setExpectedOilPressure(final int expectedOilPressure) {
        this.expectedOilPressure = expectedOilPressure;
        return this;
    }

    short getExpectedWaterTemp() {
        return expectedWaterTemp;
    }

    CarPhysicsPacketBuilder setExpectedWaterTemp(final short expectedWaterTemp) {
        this.expectedWaterTemp = expectedWaterTemp;
        return this;
    }

    int getExpectedWaterPressure() {
        return expectedWaterPressure;
    }

    CarPhysicsPacketBuilder setExpectedWaterPressure(final int expectedWaterPressure) {
        this.expectedWaterPressure = expectedWaterPressure;
        return this;
    }

    int getExpectedFuelPressure() {
        return expectedFuelPressure;
    }

    CarPhysicsPacketBuilder setExpectedFuelPressure(final int expectedFuelPressure) {
        this.expectedFuelPressure = expectedFuelPressure;
        return this;
    }

    short getExpectedFuelCapacity() {
        return expectedFuelCapacity;
    }

    CarPhysicsPacketBuilder setExpectedFuelCapacity(final short expectedFuelCapacity) {
        this.expectedFuelCapacity = expectedFuelCapacity;
        return this;
    }

    short getExpectedBrake() {
        return expectedBrake;
    }

    CarPhysicsPacketBuilder setExpectedBrake(final short expectedBrake) {
        this.expectedBrake = expectedBrake;
        return this;
    }

    short getExpectedThrottle() {
        return expectedThrottle;
    }

    CarPhysicsPacketBuilder setExpectedThrottle(final short expectedThrottle) {
        this.expectedThrottle = expectedThrottle;
        return this;
    }

    short getExpectedClutch() {
        return expectedClutch;
    }

    CarPhysicsPacketBuilder setExpectedClutch(final short expectedClutch) {
        this.expectedClutch = expectedClutch;
        return this;
    }

    float getExpectedFuelLevel() {
        return expectedFuelLevel;
    }

    CarPhysicsPacketBuilder setExpectedFuelLevel(final float expectedFuelLevel) {
        this.expectedFuelLevel = expectedFuelLevel;
        return this;
    }

    float getExpectedSpeed() {
        return expectedSpeed;
    }

    CarPhysicsPacketBuilder setExpectedSpeed(final float expectedSpeed) {
        this.expectedSpeed = expectedSpeed;
        return this;
    }

    int getExpectedRpm() {
        return expectedRpm;
    }

    CarPhysicsPacketBuilder setExpectedRpm(final int expectedRpm) {
        this.expectedRpm = expectedRpm;
        return this;
    }

    int getExpectedMaxRpm() {
        return expectedMaxRpm;
    }

    CarPhysicsPacketBuilder setExpectedMaxRpm(final int expectedMaxRpm) {
        this.expectedMaxRpm = expectedMaxRpm;
        return this;
    }

    byte getExpectedSteering() {
        return expectedSteering;
    }

    CarPhysicsPacketBuilder setExpectedSteering(final byte expectedSteering) {
        this.expectedSteering = expectedSteering;
        return this;
    }

    short getExpectedGearNumGears() {
        return expectedGearNumGears;
    }

    CarPhysicsPacketBuilder setExpectedGearNumGears(final short expectedGearNumGears) {
        this.expectedGearNumGears = expectedGearNumGears;
        return this;
    }

    short getExpectedBoostAmount() {
        return expectedBoostAmount;
    }

    CarPhysicsPacketBuilder setExpectedBoostAmount(final short expectedBoostAmount) {
        this.expectedBoostAmount = expectedBoostAmount;
        return this;
    }

    short getExpectedCrashState() {
        return expectedCrashState;
    }

    CarPhysicsPacketBuilder setExpectedCrashState(final short expectedCrashState) {
        this.expectedCrashState = expectedCrashState;
        return this;
    }

    float getExpectedOdometer() {
        return expectedOdometer;
    }

    CarPhysicsPacketBuilder setExpectedOdometer(final float expectedOdometer) {
        this.expectedOdometer = expectedOdometer;
        return this;
    }

    List<Float> getExpectedOrientation() {
        return expectedOrientation;
    }

    CarPhysicsPacketBuilder setExpectedOrientation(final List<Float> expectedOrientation) {
        this.expectedOrientation = expectedOrientation;
        return this;
    }

    List<Float> getExpectedLocalVelocity() {
        return expectedLocalVelocity;
    }

    CarPhysicsPacketBuilder setExpectedLocalVelocity(final List<Float> expectedLocalVelocity) {
        this.expectedLocalVelocity = expectedLocalVelocity;
        return this;
    }

    List<Float> getExpectedWorldVelocity() {
        return expectedWorldVelocity;
    }

    CarPhysicsPacketBuilder setExpectedWorldVelocity(final List<Float> expectedWorldVelocity) {
        this.expectedWorldVelocity = expectedWorldVelocity;
        return this;
    }

    List<Float> getExpectedAngularVelocity() {
        return expectedAngularVelocity;
    }

    CarPhysicsPacketBuilder setExpectedAngularVelocity(final List<Float> expectedAngularVelocity) {
        this.expectedAngularVelocity = expectedAngularVelocity;
        return this;
    }

    List<Float> getExpectedLocalAcceleration() {
        return expectedLocalAcceleration;
    }

    CarPhysicsPacketBuilder setExpectedLocalAcceleration(final List<Float> expectedLocalAcceleration) {
        this.expectedLocalAcceleration = expectedLocalAcceleration;
        return this;
    }

    List<Float> getExpectedWorldAcceleration() {
        return expectedWorldAcceleration;
    }

    CarPhysicsPacketBuilder setExpectedWorldAcceleration(final List<Float> expectedWorldAcceleration) {
        this.expectedWorldAcceleration = expectedWorldAcceleration;
        return this;
    }

    List<Float> getExpectedExtentsCentre() {
        return expectedExtentsCentre;
    }

    CarPhysicsPacketBuilder setExpectedExtentsCentre(final List<Float> expectedExtentsCentre) {
        this.expectedExtentsCentre = expectedExtentsCentre;
        return this;
    }

    List<Short> getExpectedTyreFlags() {
        return expectedTyreFlags;
    }

    CarPhysicsPacketBuilder setExpectedTyreFlags(final List<Short> expectedTyreFlags) {
        this.expectedTyreFlags = expectedTyreFlags;
        return this;
    }

    List<Short> getExpectedTerrain() {
        return expectedTerrain;
    }

    CarPhysicsPacketBuilder setExpectedTerrain(final List<Short> expectedTerrain) {
        this.expectedTerrain = expectedTerrain;
        return this;
    }

    List<Float> getExpectedTyreY() {
        return expectedTyreY;
    }

    CarPhysicsPacketBuilder setExpectedTyreY(final List<Float> expectedTyreY) {
        this.expectedTyreY = expectedTyreY;
        return this;
    }

    List<Float> getExpectedTyreRps() {
        return expectedTyreRps;
    }

    CarPhysicsPacketBuilder setExpectedTyreRps(final List<Float> expectedTyreRps) {
        this.expectedTyreRps = expectedTyreRps;
        return this;
    }

    List<Short> getExpectedTyreTemp() {
        return expectedTyreTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreTemp(final List<Short> expectedTyreTemp) {
        this.expectedTyreTemp = expectedTyreTemp;
        return this;
    }

    List<Float> getExpectedTyreHeightAboveGround() {
        return expectedTyreHeightAboveGround;
    }

    CarPhysicsPacketBuilder setExpectedTyreHeightAboveGround(final List<Float> expectedTyreHeightAboveGround) {
        this.expectedTyreHeightAboveGround = expectedTyreHeightAboveGround;
        return this;
    }

    List<Short> getExpectedTyreWear() {
        return expectedTyreWear;
    }

    CarPhysicsPacketBuilder setExpectedTyreWear(final List<Short> expectedTyreWear) {
        this.expectedTyreWear = expectedTyreWear;
        return this;
    }

    List<Short> getExpectedBrakeDamage() {
        return expectedBrakeDamage;
    }

    CarPhysicsPacketBuilder setExpectedBrakeDamage(final List<Short> expectedBrakeDamage) {
        this.expectedBrakeDamage = expectedBrakeDamage;
        return this;
    }

    List<Short> getExpectedSuspensionDamage() {
        return expectedSuspensionDamage;
    }

    CarPhysicsPacketBuilder setExpectedSuspensionDamage(final List<Short> expectedSuspensionDamage) {
        this.expectedSuspensionDamage = expectedSuspensionDamage;
        return this;
    }

    List<Short> getExpectedBrakeTemp() {
        return expectedBrakeTemp;
    }

    CarPhysicsPacketBuilder setExpectedBrakeTemp(final List<Short> expectedBrakeTemp) {
        this.expectedBrakeTemp = expectedBrakeTemp;
        return this;
    }

    List<Integer> getExpectedTyreTreadTemp() {
        return expectedTyreTreadTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreTreadTemp(final List<Integer> expectedTyreTreadTemp) {
        this.expectedTyreTreadTemp = expectedTyreTreadTemp;
        return this;
    }

    List<Integer> getExpectedTyreLayerTemp() {
        return expectedTyreLayerTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreLayerTemp(final List<Integer> expectedTyreLayerTemp) {
        this.expectedTyreLayerTemp = expectedTyreLayerTemp;
        return this;
    }

    List<Integer> getExpectedTyreCarcassTemp() {
        return expectedTyreCarcassTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreCarcassTemp(final List<Integer> expectedTyreCarcassTemp) {
        this.expectedTyreCarcassTemp = expectedTyreCarcassTemp;
        return this;
    }

    List<Integer> getExpectedTyreRimTemp() {
        return expectedTyreRimTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreRimTemp(final List<Integer> expectedTyreRimTemp) {
        this.expectedTyreRimTemp = expectedTyreRimTemp;
        return this;
    }

    List<Integer> getExpectedTyreInternalAirTemp() {
        return expectedTyreInternalAirTemp;
    }

    CarPhysicsPacketBuilder setExpectedTyreInternalAirTemp(final List<Integer> expectedTyreInternalAirTemp) {
        this.expectedTyreInternalAirTemp = expectedTyreInternalAirTemp;
        return this;
    }

    List<Integer> getExpectedTyreTempLeft() {
        return expectedTyreTempLeft;
    }

    CarPhysicsPacketBuilder setExpectedTyreTempLeft(final List<Integer> expectedTyreTempLeft) {
        this.expectedTyreTempLeft = expectedTyreTempLeft;
        return this;
    }

    List<Integer> getExpectedTyreTempCenter() {
        return expectedTyreTempCenter;
    }

    CarPhysicsPacketBuilder setExpectedTyreTempCenter(final List<Integer> expectedTyreTempCenter) {
        this.expectedTyreTempCenter = expectedTyreTempCenter;
        return this;
    }

    List<Integer> getExpectedTyreTempRight() {
        return expectedTyreTempRight;
    }

    CarPhysicsPacketBuilder setExpectedTyreTempRight(final List<Integer> expectedTyreTempRight) {
        this.expectedTyreTempRight = expectedTyreTempRight;
        return this;
    }

    List<Float> getExpectedWheelLocalPositionY() {
        return expectedWheelLocalPositionY;
    }

    CarPhysicsPacketBuilder setExpectedWheelLocalPositionY(final List<Float> expectedWheelLocalPositionY) {
        this.expectedWheelLocalPositionY = expectedWheelLocalPositionY;
        return this;
    }

    List<Float> getExpectedRideHeight() {
        return expectedRideHeight;
    }

    CarPhysicsPacketBuilder setExpectedRideHeight(final List<Float> expectedRideHeight) {
        this.expectedRideHeight = expectedRideHeight;
        return this;
    }

    List<Float> getExpectedSuspensionTravel() {
        return expectedSuspensionTravel;
    }

    CarPhysicsPacketBuilder setExpectedSuspensionTravel(final List<Float> expectedSuspensionTravel) {
        this.expectedSuspensionTravel = expectedSuspensionTravel;
        return this;
    }

    List<Float> getExpectedSuspensionVelocity() {
        return expectedSuspensionVelocity;
    }

    CarPhysicsPacketBuilder setExpectedSuspensionVelocity(final List<Float> expectedSuspensionVelocity) {
        this.expectedSuspensionVelocity = expectedSuspensionVelocity;
        return this;
    }

    List<Integer> getExpectedSuspensionRideHeight() {
        return expectedSuspensionRideHeight;
    }

    CarPhysicsPacketBuilder setExpectedSuspensionRideHeight(final List<Integer> expectedSuspensionRideHeight) {
        this.expectedSuspensionRideHeight = expectedSuspensionRideHeight;
        return this;
    }

    List<Integer> getExpectedAirPressure() {
        return expectedAirPressure;
    }

    CarPhysicsPacketBuilder setExpectedAirPressure(final List<Integer> expectedAirPressure) {
        this.expectedAirPressure = expectedAirPressure;
        return this;
    }

    float getExpectedEngineSpeed() {
        return expectedEngineSpeed;
    }

    CarPhysicsPacketBuilder setExpectedEngineSpeed(final float expectedEngineSpeed) {
        this.expectedEngineSpeed = expectedEngineSpeed;
        return this;
    }

    float getExpectedEngineTorque() {
        return expectedEngineTorque;
    }

    CarPhysicsPacketBuilder setExpectedEngineTorque(final float expectedEngineTorque) {
        this.expectedEngineTorque = expectedEngineTorque;
        return this;
    }

    List<Short> getExpectedWings() {
        return expectedWings;
    }

    CarPhysicsPacketBuilder setExpectedWings(final List<Short> expectedWings) {
        this.expectedWings = expectedWings;
        return this;
    }

    short getExpectedHandbrake() {
        return expectedHandbrake;
    }

    CarPhysicsPacketBuilder setExpectedHandbrake(final short expectedHandbrake) {
        this.expectedHandbrake = expectedHandbrake;
        return this;
    }

    short getExpectedAeroDamage() {
        return expectedAeroDamage;
    }

    CarPhysicsPacketBuilder setExpectedAeroDamage(final short expectedAeroDamage) {
        this.expectedAeroDamage = expectedAeroDamage;
        return this;
    }

    short getExpectedEngineDamage() {
        return expectedEngineDamage;
    }

    CarPhysicsPacketBuilder setExpectedEngineDamage(final short expectedEngineDamage) {
        this.expectedEngineDamage = expectedEngineDamage;
        return this;
    }

    long getExpectedJoypad0() {
        return expectedJoypad0;
    }

    CarPhysicsPacketBuilder setExpectedJoypad0(final long expectedJoypad0) {
        this.expectedJoypad0 = expectedJoypad0;
        return this;
    }

    short getExpectedDPad() {
        return expectedDPad;
    }

    CarPhysicsPacketBuilder setExpectedDPad(final short expectedDPad) {
        this.expectedDPad = expectedDPad;
        return this;
    }

    List<String> getExpectedTyreCompound() {
        return expectedTyreCompound;
    }

    CarPhysicsPacketBuilder setExpectedTyreCompound(final List<String> expectedTyreCompound) {
        this.expectedTyreCompound = expectedTyreCompound;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(538).order(LITTLE_ENDIAN));

        data.put(expectedViewedParticipantIndex);

        writeUnsignedByte(expectedUnfilteredThrottle, data);
        writeUnsignedByte(expectedUnfilteredBrake, data);
        data.put(expectedUnfilteredSteering);
        writeUnsignedByte(expectedUnfilteredClutch, data);

        writeUnsignedByte(expectedCarFlags, data);
        data.putShort(expectedOilTemp);
        writeUnsignedShort(expectedOilPressure, data);
        data.putShort(expectedWaterTemp);
        writeUnsignedShort(expectedWaterPressure, data);
        writeUnsignedShort(expectedFuelPressure, data);
        writeUnsignedByte(expectedFuelCapacity, data);
        writeUnsignedByte(expectedBrake, data);
        writeUnsignedByte(expectedThrottle, data);
        writeUnsignedByte(expectedClutch, data);
        data.putFloat(expectedFuelLevel);
        data.putFloat(expectedSpeed);
        writeUnsignedShort(expectedRpm, data);
        writeUnsignedShort(expectedMaxRpm, data);
        data.put(expectedSteering);
        writeUnsignedByte(expectedGearNumGears, data);
        writeUnsignedByte(expectedBoostAmount, data);
        writeUnsignedByte(expectedCrashState, data);
        data.putFloat(expectedOdometer);
        expectedOrientation.forEach(data::putFloat);
        expectedLocalVelocity.forEach(data::putFloat);
        expectedWorldVelocity.forEach(data::putFloat);
        expectedAngularVelocity.forEach(data::putFloat);
        expectedLocalAcceleration.forEach(data::putFloat);
        expectedWorldAcceleration.forEach(data::putFloat);
        expectedExtentsCentre.forEach(data::putFloat);
        expectedTyreFlags.forEach(v -> writeUnsignedByte(v, data));
        expectedTerrain.forEach(v -> writeUnsignedByte(v, data));
        expectedTyreY.forEach(data::putFloat);
        expectedTyreRps.forEach(data::putFloat);
        expectedTyreTemp.forEach(v -> writeUnsignedByte(v, data));
        expectedTyreHeightAboveGround.forEach(data::putFloat);
        expectedTyreWear.forEach(v -> writeUnsignedByte(v, data));
        expectedBrakeDamage.forEach(v -> writeUnsignedByte(v, data));
        expectedSuspensionDamage.forEach(v -> writeUnsignedByte(v, data));
        expectedBrakeTemp.forEach(data::putShort);
        expectedTyreTreadTemp.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreLayerTemp.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreCarcassTemp.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreRimTemp.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreInternalAirTemp.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreTempLeft.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreTempCenter.forEach(v -> writeUnsignedShort(v, data));
        expectedTyreTempRight.forEach(v -> writeUnsignedShort(v, data));
        expectedWheelLocalPositionY.forEach(data::putFloat);
        expectedRideHeight.forEach(data::putFloat);
        expectedSuspensionTravel.forEach(data::putFloat);
        expectedSuspensionVelocity.forEach(data::putFloat);
        expectedSuspensionRideHeight.forEach(v -> writeUnsignedShort(v, data));
        expectedAirPressure.forEach(v -> writeUnsignedShort(v, data));
        data.putFloat(expectedEngineSpeed);
        data.putFloat(expectedEngineTorque);
        expectedWings.forEach(v -> writeUnsignedByte(v, data));
        writeUnsignedByte(expectedHandbrake, data);

        writeUnsignedByte(expectedAeroDamage, data);
        writeUnsignedByte(expectedEngineDamage, data);

        writeUnsignedInt(expectedJoypad0, data);
        writeUnsignedByte(expectedDPad, data);
        expectedTyreCompound.forEach(v -> writeString(v, TYRE_NAME_LENGTH_MAX, data));

        data.rewind();
        return data;
    }
}
