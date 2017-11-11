package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;

import static com.senorpez.projectcars2.racedata.PacketType.PACKET_GAME_STATE;

class GameStatePacket extends Packet {
    private final int buildVersionNumber;
    private final int gameState;
    private final int sessionState;
    private final byte ambientTemperature;
    private final byte trackTemperature;
    private final short rainDensity;
    private final short snowDensity;
    private final byte windSpeed;
    private final byte windDirectionX;
    private final byte windDirectionY;

    GameStatePacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidSessionStateException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PACKET_GAME_STATE) {
            throw new InvalidPacketTypeException();
        }

        this.buildVersionNumber = readUnsignedShort(data);

        final byte gameSessionState = data.get();
        final int gameStateMask = 7; /* 0000 0111 */
        final int sessionStateMask = 56; /* 0011 1000 */

        this.gameState = (gameSessionState & gameStateMask);

        if (((gameSessionState & sessionStateMask) >>> 3) >= SessionState.SESSION_MAX.ordinal()) {
            throw new InvalidSessionStateException();
        } else {
            this.sessionState = (gameSessionState & sessionStateMask) >>> 3;
        }

        this.ambientTemperature = data.get();
        this.trackTemperature = data.get();
        this.rainDensity = readUnsignedByte(data);
        this.snowDensity = readUnsignedByte(data);
        this.windSpeed = data.get();
        this.windDirectionX = data.get();
        this.windDirectionY = data.get();

        // Get rid of 2 padding bytes
        final byte[] garbage = new byte[2];
        data.get(garbage);

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    int getBuildVersionNumber() {
        return buildVersionNumber;
    }

    GameState getGameState() {
        return GameState.valueOf(gameState);
    }

    SessionState getSessionState() {
        return SessionState.valueOf(sessionState);
    }

    byte getAmbientTemperature() {
        return ambientTemperature;
    }

    byte getTrackTemperature() {
        return trackTemperature;
    }

    short getRainDensity() {
        return rainDensity;
    }

    short getSnowDensity() {
        return snowDensity;
    }

    byte getWindSpeed() {
        return windSpeed;
    }

    byte getWindDirectionX() {
        return windDirectionX;
    }

    byte getWindDirectionY() {
        return windDirectionY;
    }
}
