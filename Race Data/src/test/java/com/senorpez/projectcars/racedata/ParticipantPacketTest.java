package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.senorpez.projectcars.racedata.PacketType.PARTICIPANT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ParticipantPacketTest {
    private final static Short maxUnsignedByte = Short.MAX_VALUE >>> 8;
    private final static Short minUnsignedByte = 0;

    private final static Integer expectedBuildVersionNumber = 1000;
    private final static Short expectedPacketType = 1;
    private final static Short expectedCount = 0;
    private final static String expectedCarName = "Mercedes F1 W07 Hybrid";
    private final static String expectedCarClass = "2016 F1";
    private final static String expectedTrackLocation = "Abu Dhabi";
    private final static String expectedTrackVariation = "Grand Prix";
    private final static List<String> expectedNames = Collections.unmodifiableList(Arrays.asList(
            "Nico Rosberg",
            "Lewis Hamilton",
            "Daniel Ricciardo",
            "Sebastian Vettel",
            "Max Verstappen",
            "Kimi Räikkönen",
            "Sergio Pérez",
            "Valtteri Bottas",
            "Nico Hülkenberg",
            "Fernando Alonso",
            "Felipe Massa",
            "Carlos Sainz Jr.",
            "Romain Grosjean",
            "Daniil Kvyat",
            "Jenson Button",
            "Kevin Magnussen"));
    private final static List<Float> expectedFastestLapTimes = Collections.unmodifiableList(Arrays.asList(
            103.729f,
            104.495f,
            104.889f,
            104.970f,
            105.137f,
            105.163f,
            105.187f,
            105.249f,
            105.261f,
            105.675f,
            105.715f,
            105.928f,
            105.949f,
            106.145f,
            106.189f,
            106.216f));
    private ParticipantPacket packet;

    @Test
    public void getPacketType() throws Exception {
        packet = new Builder()
                .setPacketType(expectedPacketType)
                .build();
        assertThat(packet.getPacketType(), is(PARTICIPANT));
    }

    @Test(expected = InvalidPacketException.class)
    public void getPacketType_WrongType() throws Exception {
        packet = new Builder()
                .setPacketType((short) 2)
                .build();
    }

    @Test
    public void getCarName() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getCarName(), is(expectedCarName));
    }

    @Test
    public void getCarName_GarbageAtEnd() throws Exception {
        packet = new Builder()
                .setCarName(expectedCarName + "\u0000" + GarbageGenerator.generate(10))
                .build();
        assertThat(packet.getCarName(), is(expectedCarName));
    }

    @Test
    public void getCarName_MaxLengthGarbageAtEnd() throws Exception {
        packet = new Builder()
                .setCarName(expectedCarName + "\u0000" + GarbageGenerator.generate(63 - expectedCarName.getBytes(UTF_8).length))
                .build();
        assertThat(packet.getCarName(), is(expectedCarName));
    }

    @Test
    public void getCarClass() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getCarClass(), is(expectedCarClass));
    }

    @Test
    public void getCarClass_GarbageAtEnd() throws Exception {
        packet = new Builder()
                .setCarClass(expectedCarClass + "\u0000" + GarbageGenerator.generate(10))
                .build();
        assertThat(packet.getCarClass(), is(expectedCarClass));
    }

    @Test
    public void getCarClass_MaxLengthGarbageAtEnd() throws Exception {
        packet = new Builder()
                .setCarClass(expectedCarClass + "\u0000" + GarbageGenerator.generate(63 - expectedCarClass.getBytes(UTF_8).length))
                .build();
        assertThat(packet.getCarClass(), is(expectedCarClass));
    }

    @Test
    public void getTrackLocation() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getTrackLocation(), is(expectedTrackLocation));
    }

    @Test
    public void getTrackLocation_GarbageAtEnd() throws Exception {
        packet = new Builder()
                .setTrackLocation(expectedTrackLocation + "\u0000" + GarbageGenerator.generate(10))
                .build();
        assertThat(packet.getTrackLocation(), is(expectedTrackLocation));
    }

    @Test
    public void getTrackLocation_MaxLengthGarbageAtEnd() throws Exception {
        packet = new Builder()
                .setTrackLocation(expectedTrackLocation + "\u0000" + GarbageGenerator.generate(63 - expectedTrackLocation.getBytes(UTF_8).length))
                .build();
        assertThat(packet.getTrackLocation(), is(expectedTrackLocation));
    }

    @Test
    public void getTrackVariation() throws Exception {
        packet = new Builder().build();
        assertThat(packet.getTrackVariation(), is(expectedTrackVariation));
    }

    @Test
    public void getTrackVariation_GarbageAtEnd() throws Exception {
        packet = new Builder()
                .setTrackVariation(expectedTrackVariation + "\u0000" + GarbageGenerator.generate(10))
                .build();
        assertThat(packet.getTrackVariation(), is(expectedTrackVariation));
    }

    @Test
    public void getTrackVariation_MaxLengthGarbageAtEnd() throws Exception {
        packet = new Builder()
                .setTrackVariation(expectedTrackVariation + "\u0000" + GarbageGenerator.generate(63 - expectedTrackVariation.getBytes(UTF_8).length))
                .build();
        assertThat(packet.getTrackVariation(), is(expectedTrackVariation));
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

    @Test
    public void getFastestLapTimes() throws Exception {
        packet = new Builder().setFastestLapTimes(expectedFastestLapTimes).build();
        assertThat(packet.getFastestLapTimes(), contains(expectedFastestLapTimes.toArray()));
    }

    private class Builder {
        private final Integer expectedBuildVersionNumber = ParticipantPacketTest.expectedBuildVersionNumber;
        private Short expectedPacketType = ParticipantPacketTest.expectedPacketType;
        private final Short expectedCount = ParticipantPacketTest.expectedCount;
        private String expectedCarName = ParticipantPacketTest.expectedCarName;
        private String expectedCarClass = ParticipantPacketTest.expectedCarClass;
        private String expectedTrackLocation = ParticipantPacketTest.expectedTrackLocation;
        private String expectedTrackVariation = ParticipantPacketTest.expectedTrackVariation;
        private List<String> expectedNames = ParticipantPacketTest.expectedNames;
        private List<Float> expectedFastestLapTimes = ParticipantPacketTest.expectedFastestLapTimes;

        private Builder() {
        }

        private Builder setPacketType(final Short packetType) {
            this.expectedPacketType = packetType;
            return this;
        }

        private Builder setCarName(final String carName) {
            this.expectedCarName = carName;
            return this;
        }

        private Builder setCarClass(final String carClass) {
            this.expectedCarClass = carClass;
            return this;
        }

        private Builder setTrackLocation(final String trackLocation) {
            this.expectedTrackLocation = trackLocation;
            return this;
        }

        private Builder setTrackVariation(final String trackVariation) {
            this.expectedTrackVariation = trackVariation;
            return this;
        }

        private Builder setNames(final List<String> names) {
            this.expectedNames = names;
            return this;
        }

        private Builder setFastestLapTimes(final List<Float> fastestLapTimes) {
            this.expectedFastestLapTimes = fastestLapTimes;
            return this;
        }

        private ParticipantPacket build() throws Exception {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1028);
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            final Integer packetTypeMask = 3; /* 0000 0011 */

            dataOutputStream.writeShort(expectedBuildVersionNumber);
            dataOutputStream.writeByte((expectedCount << 2) | (packetTypeMask & expectedPacketType));
            dataOutputStream.write(toByte(expectedCarName));
            dataOutputStream.write(toByte(expectedCarClass));
            dataOutputStream.write(toByte(expectedTrackLocation));
            dataOutputStream.write(toByte(expectedTrackVariation));
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
            expectedFastestLapTimes.forEach(lapTime -> {
                try {
                    dataOutputStream.writeFloat(lapTime);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });

            final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            return new ParticipantPacket(dataInputStream);
        }

        private byte[] toByte(final String input) {
            final byte[] nameBuffer = new byte[64];
            final byte[] nameBytes = input.getBytes(UTF_8);
            System.arraycopy(nameBytes, 0, nameBuffer, 0, nameBytes.length);
            return nameBuffer;
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
