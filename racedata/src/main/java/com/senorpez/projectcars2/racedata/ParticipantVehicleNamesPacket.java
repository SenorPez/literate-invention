package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class ParticipantVehicleNamesPacket extends Packet {
    private final static int VEHICLES_PER_PACKET = 16;
    private final static int VEHICLE_NAME_LENGTH_MAX = 64;

    private final List<VehicleInfo> vehicleInfo = new ArrayList<>();
    
    ParticipantVehicleNamesPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES) {
            throw new InvalidPacketTypeException();
        }
        
        for (int i = 0; i < VEHICLES_PER_PACKET; i++) {
            vehicleInfo.add(new VehicleInfo(data));
        }

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    List<VehicleInfo> getVehicleInfo() {
        return vehicleInfo;
    }

    class VehicleInfo {
        private final int index;
        private final long carClass;
        private final String name;
        
        private VehicleInfo(final ByteBuffer data) {
            index = readUnsignedShort(data);
            carClass = readUnsignedInt(data);
            name = readString(data, VEHICLE_NAME_LENGTH_MAX);

            // Get rid of 2 padding bytes
            final byte[] garbage = new byte[2];
            data.get(garbage);
        }

        int getIndex() {
            return index;
        }

        long getCarClass() {
            return carClass;
        }

        String getName() {
            return name;
        }
    }
}
