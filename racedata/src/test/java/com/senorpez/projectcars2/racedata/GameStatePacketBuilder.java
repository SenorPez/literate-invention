package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class GameStatePacketBuilder extends PacketBuilder {
    private final static Random random = new Random();

    private int expectedBuildVersionNumber = random.nextInt(MAX_UNSIGNED_SHORT);
    private int expectedGameState = random.nextInt(7);
    private int expectedSessionState = random.nextInt(7);
    private byte expectedAmbientTemperature = (byte) random.nextInt(Byte.MAX_VALUE);
    private byte expectedTrackTemperature = (byte) random.nextInt(Byte.MAX_VALUE);
    private short expectedRainDensity = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private short expectedSnowDensity = (short) random.nextInt(MAX_UNSIGNED_BYTE);
    private byte expectedWindSpeed = (byte) random.nextInt(Byte.MAX_VALUE);
    private byte expectedWindDirectionX = (byte) random.nextInt(Byte.MAX_VALUE);
    private byte expectedWindDirectionY = (byte) random.nextInt(Byte.MAX_VALUE);
    
    GameStatePacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_GAME_STATE.ordinal());
    }

    int getExpectedBuildVersionNumber() {
        return expectedBuildVersionNumber;
    }

    GameStatePacketBuilder setExpectedBuildVersionNumber(final int expectedBuildVersionNumber) {
        this.expectedBuildVersionNumber = expectedBuildVersionNumber;
        return this;
    }

    int getExpectedGameState() {
        return expectedGameState;
    }

    GameStatePacketBuilder setExpectedGameState(final int expectedGameState) {
        this.expectedGameState = expectedGameState;
        return this;
    }

    int getExpectedSessionState() {
        return expectedSessionState;
    }

    GameStatePacketBuilder setExpectedSessionState(final int expectedSessionState) {
        this.expectedSessionState = expectedSessionState;
        return this;
    }

    byte getExpectedAmbientTemperature() {
        return expectedAmbientTemperature;
    }

    GameStatePacketBuilder setExpectedAmbientTemperature(final byte expectedAmbientTemperature) {
        this.expectedAmbientTemperature = expectedAmbientTemperature;
        return this;
    }

    byte getExpectedTrackTemperature() {
        return expectedTrackTemperature;
    }

    GameStatePacketBuilder setExpectedTrackTemperature(final byte expectedTrackTemperature) {
        this.expectedTrackTemperature = expectedTrackTemperature;
        return this;
    }

    short getExpectedRainDensity() {
        return expectedRainDensity;
    }

    GameStatePacketBuilder setExpectedRainDensity(final short expectedRainDensity) {
        this.expectedRainDensity = expectedRainDensity;
        return this;
    }

    short getExpectedSnowDensity() {
        return expectedSnowDensity;
    }

    GameStatePacketBuilder setExpectedSnowDensity(final short expectedSnowDensity) {
        this.expectedSnowDensity = expectedSnowDensity;
        return this;
    }

    byte getExpectedWindSpeed() {
        return expectedWindSpeed;
    }

    GameStatePacketBuilder setExpectedWindSpeed(final byte expectedWindSpeed) {
        this.expectedWindSpeed = expectedWindSpeed;
        return this;
    }

    byte getExpectedWindDirectionX() {
        return expectedWindDirectionX;
    }

    GameStatePacketBuilder setExpectedWindDirectionX(final byte expectedWindDirectionX) {
        this.expectedWindDirectionX = expectedWindDirectionX;
        return this;
    }

    byte getExpectedWindDirectionY() {
        return expectedWindDirectionY;
    }

    GameStatePacketBuilder setExpectedWindDirectionY(final byte expectedWindDirectionY) {
        this.expectedWindDirectionY = expectedWindDirectionY;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(22).order(LITTLE_ENDIAN));

        writeUnsignedShort(expectedBuildVersionNumber, data);
        writeUnsignedByte((short) (expectedGameState | (expectedSessionState << 3)), data);
        data.put(expectedAmbientTemperature);
        data.put(expectedTrackTemperature);
        writeUnsignedByte(expectedRainDensity, data);
        writeUnsignedByte(expectedSnowDensity, data);
        data.put(expectedWindSpeed);
        data.put(expectedWindDirectionX);
        data.put(expectedWindDirectionY);

        data.rewind();
        return data;
    }
}
