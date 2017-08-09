package com.senorpez.projectcars.racedata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Iterator;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.file.StandardOpenOption.READ;

public class Telemetry implements Iterator<Packet> {
    private final FileChannel telemetryData;

    public Telemetry (final Path telemetryFile) throws IOException {
        this.telemetryData = FileChannel.open(telemetryFile, READ);
    }

    @Override
    public boolean hasNext() {
        try {
            return telemetryData.position() < telemetryData.size();
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    public Packet next() {
        try {
            final ByteBuffer sizeBuffer = ByteBuffer.allocate(2).order(LITTLE_ENDIAN);
            telemetryData.read(sizeBuffer);
            sizeBuffer.flip();
            final Short length = sizeBuffer.getShort();

            final ByteBuffer packetBuffer = ByteBuffer.allocate(length).order(LITTLE_ENDIAN);
            telemetryData.read(packetBuffer);
            packetBuffer.flip();
            final PacketType packetType = PacketType.fromLength(length);
            return packetType.getPacket(packetBuffer);
        } catch (final IOException e) {
            return null;
        }
    }
}
