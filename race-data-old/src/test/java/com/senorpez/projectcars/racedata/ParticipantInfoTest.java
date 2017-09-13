package com.senorpez.projectcars.racedata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ParticipantInfoTest {
    private ParticipantInfo participantInfo;

    @Test
    public void GetWorldPosition() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getWorldPosition(), contains(builder.getExpectedWorldPosition().toArray()));
    }

    @Test
    public void GetCurrentLapDistance() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getCurrentLapDistance(), is(builder.getExpectedCurrentLapDistance()));
    }

    @Test
    public void getRacePosition() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getRacePosition(), is(builder.getExpectedRacePosition()));
    }

    @Test
    public void IsActive() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.isActive(), is(builder.getExpectedIsActive()));
    }

    @Test
    public void GetLapsCompleted() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getLapsCompleted(), is(builder.getExpectedLapsCompleted()));
    }

    @Test
    public void IsLapInvalidated() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.isLapInvalidated(), is(builder.getExpectedIsLapInvalidated()));
    }

    @Test
    public void GetCurrentLap() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getCurrentLap(), is(builder.getExpectedCurrentLap()));
    }

    @Test
    public void GetCurrentSector() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getCurrentSector(), is(builder.getExpectedCurrentSector()));
    }

    @Test
    public void IsSameClass() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.isSameClass(), is(builder.getExpectedIsSameClass()));
    }

    @Test
    public void GetLastSectorTime() throws Exception {
        final ParticipantInfoBuilder builder = new ParticipantInfoBuilder();
        participantInfo = new ParticipantInfo(builder.build());
        assertThat(participantInfo.getLastSectorTime(), is(builder.getExpectedLastSectorTime()));
    }
}
