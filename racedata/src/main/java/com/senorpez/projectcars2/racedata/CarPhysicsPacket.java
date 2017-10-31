package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketType.PACKET_CAR_PHYSICS;
import static java.nio.charset.StandardCharsets.UTF_8;

class CarPhysicsPacket extends Packet {
    private final byte viewedParticipantIndex;

    private final short unfilteredThrottle;
    private final short unfilteredBrake;
    private final byte unfilteredSteering;
    private final short unfilteredClutch;

    private final short carFlags;
    private final short oilTemp;
    private final int oilPressure;
    private final short waterTemp;
    private final int waterPressure;
    private final int fuelPressure;
    private final short fuelCapacity;
    private final short brake;
    private final short throttle;
    private final short clutch;
    private final float fuelLevel;
    private final float speed;
    private final int rpm;
    private final int maxRpm;
    private final byte steering;
    private final short gearNumGears;
    private final short boostAmount;
    private final short crashState;
    private final float odometer;
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
    private final List<Short> tyreTemp;
    private final List<Float> tyreHeightAboveGround;
    private final List<Short> tyreWear;
    private final List<Short> brakeDamage;
    private final List<Short> suspensionDamage;
    private final List<Short> brakeTemp;
    private final List<Integer> tyreTreadTemp;
    private final List<Integer> tyreLayerTemp;
    private final List<Integer> tyreCarcassTemp;
    private final List<Integer> tyreRimTemp;
    private final List<Integer> tyreInternalAirTemp;
    private final List<Integer> tyreTempLeft;
    private final List<Integer> tyreTempCenter;
    private final List<Integer> tyreTempRight;
    private final List<Float> wheelLocalPositionY;
    private final List<Float> rideHeight;
    private final List<Float> suspensionTravel;
    private final List<Float> suspensionVelocity;
    private final List<Integer> suspensionRideHeight;
    private final List<Integer> airPressure;
    private final float engineSpeed;
    private final float engineTorque;
    private final List<Short> wings;
    private final short handbrake;

    private final short aeroDamage;
    private final short engineDamage;

    private final long joypad0;
    private final short dPad;
    private final List<String> tyreCompound;

    CarPhysicsPacket(final ByteBuffer data) throws InvalidPacketDataException, InvalidCrashDamageStateException, InvalidPacketTypeException, InvalidTerrainException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PACKET_CAR_PHYSICS) {
            throw new InvalidPacketDataException();
        }

        this.viewedParticipantIndex = data.get();

        this.unfilteredThrottle = readUnsignedByte(data);
        this.unfilteredBrake = readUnsignedByte(data);
        this.unfilteredSteering = data.get();
        this.unfilteredClutch = readUnsignedByte(data);

        this.carFlags = readUnsignedByte(data);
        this.oilTemp = data.getShort();
        this.oilPressure = readUnsignedShort(data);
        this.waterTemp = data.getShort();
        this.waterPressure = readUnsignedShort(data);
        this.fuelPressure = readUnsignedShort(data);
        this.fuelCapacity = readUnsignedByte(data);
        this.brake = readUnsignedByte(data);
        this.throttle = readUnsignedByte(data);
        this.clutch = readUnsignedByte(data);
        this.fuelLevel = data.getFloat();
        this.speed = data.getFloat();
        this.rpm = readUnsignedShort(data);
        this.maxRpm = readUnsignedShort(data);
        this.steering = data.get();
        this.gearNumGears = readUnsignedByte(data);
        this.boostAmount = readUnsignedByte(data);

        final short crashStateValue = readUnsignedByte(data);
        if (crashStateValue >= CrashDamageState.CRASH_DAMAGE_MAX.ordinal()
                || crashStateValue < 0) {
            throw new InvalidCrashDamageStateException();
        } else {
            this.crashState = crashStateValue;
        }

        this.odometer = data.getFloat();
        this.orientation = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.localVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.worldVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.angularVelocity = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.localAcceleration = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.worldAcceleration = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.extentsCentre = Collections.unmodifiableList(IntStream.range(0, 3).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.tyreFlags = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));

        final List<Short> terrainValue = IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList());
        if (terrainValue.stream().anyMatch(v -> v >= Terrain.TERRAIN_MAX.ordinal()
                || v < 0)) {
            throw new InvalidTerrainException();
        } else {
            this.terrain = Collections.unmodifiableList(terrainValue);
        }

        this.tyreY = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.tyreRps = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.tyreTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));
        this.tyreHeightAboveGround = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.tyreWear = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));
        this.brakeDamage = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));
        this.suspensionDamage = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));
        this.brakeTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getShort()).collect(Collectors.toList()));
        this.tyreTreadTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreLayerTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreCarcassTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreRimTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreInternalAirTemp = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreTempLeft = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreTempCenter = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.tyreTempRight = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.wheelLocalPositionY = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.rideHeight = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.suspensionTravel = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.suspensionVelocity = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> data.getFloat()).collect(Collectors.toList()));
        this.suspensionRideHeight = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.airPressure = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readUnsignedShort(data)).collect(Collectors.toList()));
        this.engineSpeed = data.getFloat();
        this.engineTorque = data.getFloat();
        this.wings = Collections.unmodifiableList(IntStream.range(0, 2).mapToObj(value -> readUnsignedByte(data)).collect(Collectors.toList()));
        this.handbrake = readUnsignedByte(data);

        this.aeroDamage = readUnsignedByte(data);
        this.engineDamage = readUnsignedByte(data);

        this.joypad0 = readUnsignedInt(data);
        this.dPad = readUnsignedByte(data);
        this.tyreCompound = Collections.unmodifiableList(IntStream.range(0, 4).mapToObj(value -> readString(data)).collect(Collectors.toList()));

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    byte getViewedParticipantIndex() {
        return viewedParticipantIndex;
    }

    short getUnfilteredThrottle() {
        return unfilteredThrottle;
    }

    short getUnfilteredBrake() {
        return unfilteredBrake;
    }

    byte getUnfilteredSteering() {
        return unfilteredSteering;
    }

    short getUnfilteredClutch() {
        return unfilteredClutch;
    }

    boolean isHeadlight() {
        return CarFlags.CAR_HEADLIGHT.isSet(carFlags);
    }

    boolean isEngineActive() {
        return CarFlags.CAR_ENGINE_ACTIVE.isSet(carFlags);
    }

    boolean isEngineWarning() {
        return CarFlags.CAR_ENGINE_WARNING.isSet(carFlags);
    }

    boolean isSpeedLimiter() {
        return CarFlags.CAR_SPEED_LIMITER.isSet(carFlags);
    }

    boolean isAbs() {
        return CarFlags.CAR_ABS.isSet(carFlags);
    }

    boolean isHandbrake() {
        return CarFlags.CAR_HANDBRAKE.isSet(carFlags);
    }

    short getOilTemp() {
        return oilTemp;
    }

    int getOilPressure() {
        return oilPressure;
    }

    short getWaterTemp() {
        return waterTemp;
    }

    int getWaterPressure() {
        return waterPressure;
    }

    int getFuelPressure() {
        return fuelPressure;
    }

    short getFuelCapacity() {
        return fuelCapacity;
    }

    short getBrake() {
        return brake;
    }

    short getThrottle() {
        return throttle;
    }

    short getClutch() {
        return clutch;
    }

    float getFuelLevel() {
        return fuelLevel;
    }

    float getSpeed() {
        return speed;
    }

    int getRpm() {
        return rpm;
    }

    int getMaxRpm() {
        return maxRpm;
    }

    byte getSteering() {
        return steering;
    }

    short getGearNumGears() {
        return gearNumGears;
    }

    short getBoostAmount() {
        return boostAmount;
    }

    CrashDamageState getCrashDamageState() {
        return CrashDamageState.valueOf(crashState);
    }

    float getOdometer() {
        return odometer;
    }

    List<Float> getOrientation() {
        return orientation;
    }

    List<Float> getLocalVelocity() {
        return localVelocity;
    }

    List<Float> getWorldVelocity() {
        return worldVelocity;
    }

    List<Float> getAngularVelocity() {
        return angularVelocity;
    }

    List<Float> getLocalAcceleration() {
        return localAcceleration;
    }

    List<Float> getWorldAcceleration() {
        return worldAcceleration;
    }

    List<Float> getExtentsCentre() {
        return extentsCentre;
    }

    List<Boolean> isTyreAttached() {
        return tyreFlags.stream().map(TyreFlags.TYRE_ATTACHED::isSet).collect(Collectors.toList());
    }

    List<Boolean> isTyreInflated() {
        return tyreFlags.stream().map(TyreFlags.TYRE_INFLATED::isSet).collect(Collectors.toList());
    }

    List<Boolean> isTyreIsOnGround() {
        return tyreFlags.stream().map(TyreFlags.TYRE_IS_ON_GROUND::isSet).collect(Collectors.toList());
    }

    List<Terrain> getTerrain() {
        return terrain.stream().map(Terrain::valueOf).collect(Collectors.toList());
    }

    List<Float> getTyreY() {
        return tyreY;
    }

    List<Float> getTyreRps() {
        return tyreRps;
    }

    List<Short> getTyreTemp() {
        return tyreTemp;
    }

    List<Float> getTyreHeightAboveGround() {
        return tyreHeightAboveGround;
    }

    List<Short> getTyreWear() {
        return tyreWear;
    }

    List<Short> getBrakeDamage() {
        return brakeDamage;
    }

    List<Short> getSuspensionDamage() {
        return suspensionDamage;
    }

    List<Short> getBrakeTemp() {
        return brakeTemp;
    }

    List<Integer> getTyreTreadTemp() {
        return tyreTreadTemp;
    }

    List<Integer> getTyreLayerTemp() {
        return tyreLayerTemp;
    }

    List<Integer> getTyreCarcassTemp() {
        return tyreCarcassTemp;
    }

    List<Integer> getTyreRimTemp() {
        return tyreRimTemp;
    }

    List<Integer> getTyreInternalAirTemp() {
        return tyreInternalAirTemp;
    }

    List<Integer> getTyreTempLeft() {
        return tyreTempLeft;
    }

    List<Integer> getTyreTempCenter() {
        return tyreTempCenter;
    }

    List<Integer> getTyreTempRight() {
        return tyreTempRight;
    }

    List<Float> getWheelLocalPositionY() {
        return wheelLocalPositionY;
    }

    List<Float> getRideHeight() {
        return rideHeight;
    }

    List<Float> getSuspensionTravel() {
        return suspensionTravel;
    }

    List<Float> getSuspensionVelocity() {
        return suspensionVelocity;
    }

    List<Integer> getSuspensionRideHeight() {
        return suspensionRideHeight;
    }

    List<Integer> getAirPressure() {
        return airPressure;
    }

    float getEngineSpeed() {
        return engineSpeed;
    }

    float getEngineTorque() {
        return engineTorque;
    }

    List<Short> getWings() {
        return wings;
    }

    short getHandbrake() {
        return handbrake;
    }

    short getAeroDamage() {
        return aeroDamage;
    }

    short getEngineDamage() {
        return engineDamage;
    }

    long getJoypad0() {
        return joypad0;
    }

    short getdPad() {
        return dPad;
    }

    List<String> getTyreCompound() {
        return tyreCompound;
    }

    private static int readUnsignedShort(final ByteBuffer data) {
        final byte[] bytes = new byte[2];
        data.get(bytes);
        return (bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8);
    }
    
    private static String readString(final ByteBuffer data) {
        final byte[] bytes = new byte[40];
        data.get(bytes);
        return new String(bytes, UTF_8).split("\u0000", 2)[0];
    }
}
