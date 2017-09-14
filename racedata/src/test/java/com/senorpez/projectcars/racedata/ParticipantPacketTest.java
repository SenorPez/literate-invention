package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.senorpez.projectcars.racedata.PacketType.PARTICIPANT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ParticipantPacketTest {
    private ParticipantPacket packet;

    @Test
    public void getPacketType() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getPacketType(), is(PARTICIPANT));
    }

    @Test(expected = InvalidPacketException.class)
    public void getPacketType_WrongType() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder()
                .setExpectedPacketType((short) 0);
        packet = new ParticipantPacket(builder.build());
    }

    @Test
    public void getExpectedCarName() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getCarName(), is(builder.getExpectedCarName()));
    }

    @Test
    public void getExpectedCarName_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedCarName = builder.getExpectedCarName();
        builder.setExpectedCarName(StringGenerator.GetStringWithGarbage(expectedCarName));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedCarName, is(not(builder.getExpectedCarName())));
        assertThat(packet.getCarName(), is(expectedCarName));
    }

    @Test
    public void getExpectedCarName_MaxLength_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedCarName = builder.getExpectedCarName();
        builder.setExpectedCarName(StringGenerator.GetStringWithGarbage(expectedCarName));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedCarName, is(not(builder.getExpectedCarName())));
        assertThat(packet.getCarName(), is(expectedCarName));
    }

    @Test
    public void getExpectedCarClass() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getCarClass(), is(builder.getExpectedCarClass()));
    }

    @Test
    public void getExpectedCarClass_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedCarClass = builder.getExpectedCarClass();
        builder.setExpectedCarClass(StringGenerator.GetStringWithGarbage(expectedCarClass));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedCarClass, is(not(builder.getExpectedCarClass())));
        assertThat(packet.getCarClass(), is(expectedCarClass));
    }

    @Test
    public void getExpectedCarClass_MaxLength_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedCarClass = builder.getExpectedCarClass();
        builder.setExpectedCarClass(StringGenerator.GetStringWithGarbage(expectedCarClass));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedCarClass, is(not(builder.getExpectedCarClass())));
        assertThat(packet.getCarClass(), is(expectedCarClass));
    }

    @Test
    public void getExpectedTrackLocation() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getTrackLocation(), is(builder.getExpectedTrackLocation()));
    }

    @Test
    public void getExpectedTrackLocation_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedTrackLocation = builder.getExpectedTrackLocation();
        builder.setExpectedTrackLocation(StringGenerator.GetStringWithGarbage(expectedTrackLocation));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedTrackLocation, is(not(builder.getExpectedTrackLocation())));
        assertThat(packet.getTrackLocation(), is(expectedTrackLocation));
    }

    @Test
    public void getExpectedTrackLocation_MaxLength_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedTrackLocation = builder.getExpectedTrackLocation();
        builder.setExpectedTrackLocation(StringGenerator.GetStringWithGarbage(expectedTrackLocation));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedTrackLocation, is(not(builder.getExpectedTrackLocation())));
        assertThat(packet.getTrackLocation(), is(expectedTrackLocation));
    }

    @Test
    public void getExpectedTrackVariation() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getTrackVariation(), is(builder.getExpectedTrackVariation()));
    }

    @Test
    public void getExpectedTrackVariation_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedTrackVariation = builder.getExpectedTrackVariation();
        builder.setExpectedTrackVariation(StringGenerator.GetStringWithGarbage(expectedTrackVariation));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedTrackVariation, is(not(builder.getExpectedTrackVariation())));
        assertThat(packet.getTrackVariation(), is(expectedTrackVariation));
    }

    @Test
    public void getExpectedTrackVariation_MaxLength_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final String expectedTrackVariation = builder.getExpectedTrackVariation();
        builder.setExpectedTrackVariation(StringGenerator.GetStringWithGarbage(expectedTrackVariation));
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedTrackVariation, is(not(builder.getExpectedTrackVariation())));
        assertThat(packet.getTrackVariation(), is(expectedTrackVariation));
    }

    @Test
    public void getNames() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getNames(), contains(builder.getExpectedNames().toArray()));
    }

    @Test
    public void getNames_F1Names() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder()
                .setExpectedNames(Collections.unmodifiableList(Arrays.asList(
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
                        "Kevin Magnussen")));
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getNames(), contains(builder.getExpectedNames().toArray()));
    }

    @Test
    public void getNames_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final List<String> expectedNames = builder.getExpectedNames();
        final List<String> namesWithGarbage = builder.getExpectedNames().stream()
                .map(StringGenerator::GetStringWithGarbage)
                .collect(Collectors.toList());
        builder.setExpectedNames(namesWithGarbage);
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedNames, not(contains(namesWithGarbage.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    @Test
    public void getNames_MaxLength_GarbageAtEnd() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        final List<String> expectedNames = builder.getExpectedNames();
        final List<String> namesWithGarbage = builder.getExpectedNames().stream()
                .map(StringGenerator::GetStringWithGarbage)
                .collect(Collectors.toList());
        builder.setExpectedNames(namesWithGarbage);
        packet = new ParticipantPacket(builder.build());

        assertThat(expectedNames, not(contains(namesWithGarbage.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    @Test
    public void getFastestLapTimes() throws Exception {
        final ParticipantPacketBuilder builder = new ParticipantPacketBuilder();
        packet = new ParticipantPacket(builder.build());
        assertThat(packet.getFastestLapTimes(), contains(builder.getExpectedFastestLapTimes().toArray()));
    }
}
