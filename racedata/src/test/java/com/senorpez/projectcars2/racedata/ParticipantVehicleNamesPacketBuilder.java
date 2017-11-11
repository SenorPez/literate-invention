package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.stream.IntStream;

class ParticipantVehicleNamesPacketBuilder extends PacketBuilder {
    private final static int VEHICLES_PER_PACKET = 16;
    private final static int VEHICLE_NAME_LENGTH_MAX = 64;

    private VehicleInfoBuilder vehicleInfoBuilder = new VehicleInfoBuilder();

    ParticipantVehicleNamesPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES.ordinal());
    }

    VehicleInfoBuilder getVehicleInfoBuilder() {
        return vehicleInfoBuilder;
    }

    ParticipantVehicleNamesPacketBuilder setVehicleInfoBuilder(final VehicleInfoBuilder vehicleInfoBuilder) {
        this.vehicleInfoBuilder = vehicleInfoBuilder;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(1164));

        IntStream
                .range(0, VEHICLES_PER_PACKET)
                .forEach(v -> data.put(vehicleInfoBuilder.build()));

        data.rewind();
        return data;
    }
    
    static class VehicleInfoBuilder extends PacketBuilder {
        private final static Random random = new Random();
        
        private int expectedIndex = random.nextInt(MAX_UNSIGNED_SHORT);
        private long expectedCarClass = getBoundedLong();
        private String expectedName = generateString(VEHICLE_NAME_LENGTH_MAX);

        int getExpectedIndex() {
            return expectedIndex;
        }

        VehicleInfoBuilder setExpectedIndex(final int expectedIndex) {
            this.expectedIndex = expectedIndex;
            return this;
        }

        long getExpectedCarClass() {
            return expectedCarClass;
        }

        VehicleInfoBuilder setExpectedCarClass(final long expectedCarClass) {
            this.expectedCarClass = expectedCarClass;
            return this;
        }

        String getExpectedName() {
            return expectedName;
        }

        VehicleInfoBuilder setExpectedName(final String expectedName) {
            this.expectedName = expectedName;
            return this;
        }

        @Override
        ByteBuffer build() {
            final ByteBuffer data = ByteBuffer.allocate(72);

            writeUnsignedShort(expectedIndex, data);
            writeUnsignedInt(expectedCarClass, data);
            writeString(expectedName, VEHICLE_NAME_LENGTH_MAX, data);
            
            data.rewind();
            return data;
        }
    }
}
