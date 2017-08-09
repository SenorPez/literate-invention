package com.senorpez.projectcars.racedata;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class AdditionalParticipantPacketBuilder extends PacketBuilder {
    private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private Short expectedOffset = (short) random.nextInt(MAX_UNSIGNED_BYTE + 1);
    private List<String> expectedNames = Collections.unmodifiableList(
            IntStream.range(0, 16)
                    .mapToObj(value -> {
                        int nameLength = random.nextInt(62);
                        StringBuilder nameBuilder = new StringBuilder();
                        IntStream.rangeClosed(0, nameLength)
                                .forEach(value1 -> nameBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
                        return nameBuilder.toString();
                    })
                    .collect(Collectors.toList()));

    AdditionalParticipantPacketBuilder() {
        super();
        this.setExpectedPacketType((short) 2);
    }

    Short getExpectedOffset() {
        return expectedOffset;
    }

    AdditionalParticipantPacketBuilder setExpectedOffset(final Short expectedOffset) {
        this.expectedOffset = expectedOffset;
        return this;
    }

    List<String> getExpectedNames() {
        return expectedNames;
    }

    AdditionalParticipantPacketBuilder setExpectedNames(final List<String> expectedNames) {
        this.expectedNames = expectedNames;
        return this;
    }

    @Override
    ByteBuffer build() throws Exception {
        final ByteBuffer data = build(ByteBuffer.allocate(1028).order(LITTLE_ENDIAN));

        writeUnsignedByte(expectedOffset, data);
        expectedNames.forEach(name -> writeString(name, data));

        data.rewind();
        return data;
    }
}
