package com.senorpez.projectcars.racedata;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.senorpez.projectcars.racedata.PacketType.ADDITIONAL_PARTICIPANT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class AdditionalParticipantPacketTest {
    private AdditionalParticipantPacket packet;

    @Test
    public void getPacketType() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder();
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getPacketType(), is(ADDITIONAL_PARTICIPANT));
    }

    @Test(expected = InvalidPacketException.class)
    public void getPacketType_WrongType() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder()
                .setExpectedPacketType((short) 1);
        packet = new AdditionalParticipantPacket(builder.build());
    }

    @Test
    public void getOffset() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder();
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getOffset(), is(builder.getExpectedOffset()));
    }

    @Test
    public void getOffset_MaxValue() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder()
                .setExpectedOffset(PacketBuilder.MAX_UNSIGNED_BYTE);
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getOffset(), is(PacketBuilder.MAX_UNSIGNED_BYTE));

        builder.setExpectedOffset((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1));
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getOffset(), is(not((short) (PacketBuilder.MAX_UNSIGNED_BYTE + 1))));
    }

    @Test
    public void getOffset_MinValue() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder()
                .setExpectedOffset(PacketBuilder.MIN_UNSIGNED_BYTE);
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getOffset(), is(PacketBuilder.MIN_UNSIGNED_BYTE));

        builder.setExpectedOffset((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1));
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getOffset(), is(not((short) (PacketBuilder.MIN_UNSIGNED_BYTE - 1))));
    }

    @Test
    public void getNames() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder();
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getNames(), contains(builder.getExpectedNames().toArray()));
    }

    @Test
    public void getNames_F1Names() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder()
                .setExpectedNames(Collections.unmodifiableList(Arrays.asList(
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
                        "")));
        packet = new AdditionalParticipantPacket(builder.build());
        assertThat(packet.getNames(), contains(builder.getExpectedNames().toArray()));
    }

    @Test
    public void getNames_GarbageAtEnd() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder();
        final List<String> expectedNames = builder.getExpectedNames();
        final List<String> namesWithGarbage = builder.getExpectedNames().stream()
                .map(StringGenerator::GetStringWithGarbage)
                .collect(Collectors.toList());
        builder.setExpectedNames(namesWithGarbage);
        packet = new AdditionalParticipantPacket(builder.build());

        assertThat(expectedNames, not(contains(namesWithGarbage.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }

    @Test
    public void getNames_MaxLength_GarbageAtEnd() throws Exception {
        final AdditionalParticipantPacketBuilder builder = new AdditionalParticipantPacketBuilder();
        final List<String> expectedNames = builder.getExpectedNames();
        final List<String> namesWithGarbage = builder.getExpectedNames().stream()
                .map(StringGenerator::GetStringWithGarbage)
                .collect(Collectors.toList());
        builder.setExpectedNames(namesWithGarbage);
        packet = new AdditionalParticipantPacket(builder.build());

        assertThat(expectedNames, not(contains(namesWithGarbage.toArray())));
        assertThat(packet.getNames(), contains(expectedNames.toArray()));
    }
}