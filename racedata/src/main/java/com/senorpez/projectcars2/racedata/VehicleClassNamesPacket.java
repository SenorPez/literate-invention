package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class VehicleClassNamesPacket extends Packet {
    private final static int CLASS_NAME_LENGTH_MAX = 20;
    private final static int CLASSES_SUPPORTED_PER_PACKET = 60;

    private final List<ClassInfo> classInfo = new ArrayList<>();
    
    VehicleClassNamesPacket(final ByteBuffer data) throws InvalidPacketTypeException, InvalidPacketDataException {
        super(data);

        if (PacketType.valueOf(this.getPacketType()) != PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES) {
            throw new InvalidPacketTypeException();
        }
        
        for (int i = 0; i < CLASSES_SUPPORTED_PER_PACKET; i++) {
            classInfo.add(new ClassInfo(data));
        }

        if (data.hasRemaining()) {
            throw new InvalidPacketDataException();
        }
    }

    List<ClassInfo> getClassInfo() {
        return classInfo;
    }

    class ClassInfo {
        private final long classIndex;
        private final String name;
        
        private ClassInfo(final ByteBuffer data) {
            classIndex = readUnsignedInt(data);
            name = readString(data, CLASS_NAME_LENGTH_MAX);
        }

        long getClassIndex() {
            return classIndex;
        }

        String getName() {
            return name;
        }
    }

}
