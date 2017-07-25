package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.racedata.PacketType.ADDITIONAL_PARTICIPANT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AdditionalParticipantPacketTest {
    private final static Short maxUnsignedByte = Short.MAX_VALUE >>> 8;
    private final static Short minUnsignedByte = 0;

    private final static Integer expectedBuildVersionNumber = 1000;
    private final static Short expectedPacketType = 2;
    private final static Short expectedCount = 0;
    private final static Short expectedOffset = 16;
    private final static List<String> expectedNames = Collections.unmodifiableList(Arrays.asList(
            "Felipe Nasr",
            "Jolyon Palmer",
            "Pascal Wehrlein",
            "Stoffel Vandoorne",
            "Esteban Gutiérrez",
            "Marcus Ericsson",
            "Esteban Ocon",
            "Rio Haryanto",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""));
    private AdditionalParticipantPacket packet;

    @Test
    public void getPacketType() throws Exception {
        packet = new Builder()
                .setPacketType(expectedPacketType)
                .build();
        assertThat(packet.getPacketType(), is(ADDITIONAL_PARTICIPANT));
    }

    @Test(expected = InvalidPacketException.class)
    public void getPacketType_WrongType() throws Exception {
        packet = new Builder()
                .setPacketType((short) 1)
                .build();
    }

    @Test
    public void getOffset() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getOffset(), is(expectedOffset));
    }

    @Test
    public void getOffset_MaxValue() throws Exception {
        packet = new Builder()
                .setOffset(maxUnsignedByte)
                .build();
        assertThat(packet.getOffset(), is(maxUnsignedByte));
    }

    @Test
    public void getOffset_MinValue() throws Exception {
        packet = new Builder()
                .setOffset(minUnsignedByte)
                .build();
        assertThat(packet.getOffset(), is(minUnsignedByte));
    }

    @Test
    public void getNames() throws Exception {
        packet = new Builder()
                .setNames(expectedNames)
                .build();
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    @Test
    public void getNames_GarbageAtEnd() throws Exception {
        final List<String> namesWithGarbage = expectedNames.stream()
                .map(name -> name + "\u0000" + GarbageGenerator.generate(10))
                .collect(Collectors.toList());
        packet = new Builder()
                .setNames(namesWithGarbage)
                .build();
        assertThat(namesWithGarbage, not(contains(expectedNames.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    @Test
    public void getNames_MaxLengthGarbageAtEnd() throws Exception {
        final List<String> namesWithGarbage = expectedNames.stream()
                .map(name -> name + "\u0000" + GarbageGenerator.generate(63 - name.getBytes(UTF_8).length))
                .collect(Collectors.toList());
        packet = new Builder()
                .setNames(namesWithGarbage)
                .build();
        assertThat(namesWithGarbage, not(contains(expectedNames.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    private class Builder {
        private final Integer expectedBuildVersionNumber = AdditionalParticipantPacketTest.expectedBuildVersionNumber;
        private Short expectedPacketType = AdditionalParticipantPacketTest.expectedPacketType;
        private final Short expectedCount = AdditionalParticipantPacketTest.expectedCount;
        private Short expectedOffset = AdditionalParticipantPacketTest.expectedOffset;
        private List<String> expectedNames = AdditionalParticipantPacketTest.expectedNames;

        private Builder() {
        }

        private Builder setPacketType(final Short packetType) {
            this.expectedPacketType = packetType;
            return this;
        }

        private Builder setOffset(final Short offset) {
            this.expectedOffset = offset;
            return this;
        }

        private Builder setNames(final List<String> names) {
            this.expectedNames = names;
            return this;
        }

        private AdditionalParticipantPacket build() throws Exception {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1028);
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            final Integer packetTypeMask = 3; /* 0000 0011 */

            dataOutputStream.writeShort(expectedBuildVersionNumber);
            dataOutputStream.writeByte((expectedCount << 2) | (packetTypeMask & expectedPacketType));
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

            final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            return new AdditionalParticipantPacket(dataInputStream);
        }
    }

    private static class GarbageGenerator {
        private final static String letters = "abcdef";
        private final static Random generator = new Random();

        private static String generate(final int count) {
            return IntStream.range(0, count)
                    .mapToObj(value -> String.valueOf(letters.charAt(generator.nextInt(letters.length()))))
                    .collect(Collectors.joining());
        }
    }
}