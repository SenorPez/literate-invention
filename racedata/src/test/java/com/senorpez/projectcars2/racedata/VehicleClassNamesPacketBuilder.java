package com.senorpez.projectcars2.racedata;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

class VehicleClassNamesPacketBuilder extends PacketBuilder {
    private final static int CLASS_NAME_LENGTH_MAX = 20;
    private final static int CLASSES_SUPPORTED_PER_PACKET = 60;

    private ClassInfoBuilder classInfoBuilder = new ClassInfoBuilder();
    
    VehicleClassNamesPacketBuilder() {
        super();
        this.setExpectedPacketType((short) PacketType.PACKET_PARTICIPANT_VEHICLE_NAMES.ordinal());
    }

    ClassInfoBuilder getClassInfoBuilder() {
        return classInfoBuilder;
    }

    VehicleClassNamesPacketBuilder setClassInfoBuilder(final ClassInfoBuilder classInfoBuilder) {
        this.classInfoBuilder = classInfoBuilder;
        return this;
    }

    @Override
    ByteBuffer build() {
        final ByteBuffer data = build(ByteBuffer.allocate(1452));

        IntStream
                .range(0, CLASSES_SUPPORTED_PER_PACKET)
                .forEach(v -> data.put(classInfoBuilder.build()));

        data.rewind();
        return data;
    }
    
    static class ClassInfoBuilder extends PacketBuilder {
        private long expectedClassIndex = getBoundedLong();
        private String expectedName = generateString(CLASS_NAME_LENGTH_MAX);

        long getExpectedClassIndex() {
            return expectedClassIndex;
        }

        ClassInfoBuilder setExpectedClassIndex(final long expectedClassIndex) {
            this.expectedClassIndex = expectedClassIndex;
            return this;
        }

        String getExpectedName() {
            return expectedName;
        }

        ClassInfoBuilder setExpectedName(final String expectedName) {
            this.expectedName = expectedName;
            return this;
        }

        @Override
        ByteBuffer build() {
            final ByteBuffer data = ByteBuffer.allocate(24);

            writeUnsignedInt(expectedClassIndex, data);
            writeString(expectedName, CLASS_NAME_LENGTH_MAX, data);
            
            data.rewind();
            return data;
        }
    }
}
