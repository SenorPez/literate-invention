package com.senorpez.projectcars.racedata;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

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
    DataInputStream build() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = build(new ByteArrayOutputStream(1028));

        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeByte(expectedOffset);
        expectedNames.forEach(name -> {
            final byte[] nameBuffer = new byte[64];
            final byte[] nameBytes = name.getBytes(UTF_8);
            System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
            try {
                dataOutputStream.write(nameBuffer);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        return new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
}
