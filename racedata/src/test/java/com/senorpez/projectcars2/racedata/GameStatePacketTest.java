package com.senorpez.projectcars2.racedata;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static com.senorpez.projectcars2.racedata.PacketBuilder.*;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Enclosed.class)
public class GameStatePacketTest {
    @RunWith(Parameterized.class)
    public static class GameSessionStateTests {
        private GameStatePacket packet;

        @Parameter()
        public int gameStateValue;
        
        @Parameter(1)
        public int sessionStateValue; 
        
        @Parameters(name = "Game State: {0}, Session State: {1}")
        public static Collection<Object[]> data() {
            final List<Object[]> output = new ArrayList<>();
            IntStream
                    .range(0, GameState.GAME_MAX.ordinal())
                    .forEach(gameVal -> IntStream
                            .range(0, SessionState.SESSION_MAX.ordinal())
                            .forEach(sessionVal -> output.add(new Object[]{gameVal, sessionVal})));
            return output;
        }
        
        @Test
        public void getGameState() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedGameState(gameStateValue)
                    .setExpectedSessionState(sessionStateValue);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getGameState(), is(GameState.valueOf(gameStateValue)));
        }
        
        @Test
        public void getSessionState() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedGameState(gameStateValue)
                    .setExpectedSessionState(sessionStateValue);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSessionState(), is(SessionState.valueOf(sessionStateValue)));
        }
    }
    
    public static class SingleTests {
        private GameStatePacket packet;
        
        @Test(expected = InvalidPacketDataException.class)
        public void throwInvalidPacketData() throws Exception {
            final ByteBuffer data = ByteBuffer.allocate(1500).order(LITTLE_ENDIAN);
            data.put(10, (byte) PacketType.PACKET_GAME_STATE.ordinal());
            packet = new GameStatePacket(data);
        }
        
        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketType_WrongPacketType() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedPacketType((short) 0);
            packet = new GameStatePacket(builder.build());
        }
        
        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketTypeException_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedPacketType((short) PacketType.PACKET_MAX.ordinal());
            packet = new GameStatePacket(builder.build());
        }
        
        @Test(expected = InvalidPacketTypeException.class)
        public void throwInvalidPacketTypeException_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedPacketType((short) -1);
            packet = new GameStatePacket(builder.build());
        }
        
        @Test
        public void getBuildVersionNumber() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getBuildVersionNumber(), is(builder.getExpectedBuildVersionNumber()));
        }
        
        @Test
        public void getBuildVersionNumber_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedBuildVersionNumber(MAX_UNSIGNED_SHORT);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getBuildVersionNumber(), is(MAX_UNSIGNED_SHORT));

            builder.setExpectedBuildVersionNumber(MAX_UNSIGNED_SHORT + 1);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getBuildVersionNumber(), is(not(MAX_UNSIGNED_SHORT + 1)));
        }
        
        @Test
        public void getBuildVersionNumber_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedBuildVersionNumber(MIN_UNSIGNED_SHORT);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getBuildVersionNumber(), is(MIN_UNSIGNED_SHORT));

            builder.setExpectedBuildVersionNumber(MIN_UNSIGNED_SHORT - 1);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getBuildVersionNumber(), is(not(MIN_UNSIGNED_SHORT - 1)));
        }

        @Test(expected = InvalidSessionStateException.class)
        public void getSessionState_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedSessionState(SessionState.SESSION_MAX.ordinal());
            packet = new GameStatePacket(builder.build());
        }

        @Test
        public void getAmbientTemperature() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(builder.getExpectedAmbientTemperature()));
        }

        @Test
        public void getAmbientTemperature_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedAmbientTemperature(Byte.MAX_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(Byte.MAX_VALUE));

            builder.setExpectedAmbientTemperature((byte) (Byte.MAX_VALUE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getAmbientTemperature_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedAmbientTemperature(Byte.MIN_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(Byte.MIN_VALUE));

            builder.setExpectedAmbientTemperature((byte) (Byte.MIN_VALUE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getAmbientTemperature(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getTrackTemperature() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(builder.getExpectedTrackTemperature()));
        }

        @Test
        public void getTrackTemperature_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedTrackTemperature(Byte.MAX_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(Byte.MAX_VALUE));

            builder.setExpectedTrackTemperature((byte) (Byte.MAX_VALUE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getTrackTemperature_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedTrackTemperature(Byte.MIN_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(Byte.MIN_VALUE));

            builder.setExpectedTrackTemperature((byte) (Byte.MIN_VALUE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getTrackTemperature(), is(not(Byte.MIN_VALUE - 1)));
        }        
        
        @Test
        public void getRainDensity() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getRainDensity(), is(builder.getExpectedRainDensity()));
        }

        @Test
        public void getRainDensity_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedRainDensity(MAX_UNSIGNED_BYTE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getRainDensity(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedRainDensity((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getRainDensity(), is(not((byte) MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getRainDensity_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedRainDensity(MIN_UNSIGNED_BYTE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getRainDensity(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedRainDensity((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getRainDensity(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }
        
        @Test
        public void getSnowDensity() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSnowDensity(), is(builder.getExpectedSnowDensity()));
        }

        @Test
        public void getSnowDensity_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedSnowDensity(MAX_UNSIGNED_BYTE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSnowDensity(), is(MAX_UNSIGNED_BYTE));

            builder.setExpectedSnowDensity((byte) (MAX_UNSIGNED_BYTE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSnowDensity(), is(not((byte) MAX_UNSIGNED_BYTE + 1)));
        }

        @Test
        public void getSnowDensity_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedSnowDensity(MIN_UNSIGNED_BYTE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSnowDensity(), is(MIN_UNSIGNED_BYTE));

            builder.setExpectedSnowDensity((byte) (MIN_UNSIGNED_BYTE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getSnowDensity(), is(not(MIN_UNSIGNED_BYTE - 1)));
        }
        
        @Test
        public void getWindSpeed() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindSpeed(), is(builder.getExpectedWindSpeed()));
        }

        @Test
        public void getWindSpeed_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindSpeed(Byte.MAX_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindSpeed(), is(Byte.MAX_VALUE));

            builder.setExpectedWindSpeed((byte) (Byte.MAX_VALUE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindSpeed(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getWindSpeed_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindSpeed(Byte.MIN_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindSpeed(), is(Byte.MIN_VALUE));

            builder.setExpectedWindSpeed((byte) (Byte.MIN_VALUE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindSpeed(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getWindDirectionX() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(builder.getExpectedWindDirectionX()));
        }

        @Test
        public void getWindDirectionX_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindDirectionX(Byte.MAX_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(Byte.MAX_VALUE));

            builder.setExpectedWindDirectionX((byte) (Byte.MAX_VALUE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getWindDirectionX_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindDirectionX(Byte.MIN_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(Byte.MIN_VALUE));

            builder.setExpectedWindDirectionX((byte) (Byte.MIN_VALUE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionX(), is(not(Byte.MIN_VALUE - 1)));
        }

        @Test
        public void getWindDirectionY() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder();
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(builder.getExpectedWindDirectionY()));
        }

        @Test
        public void getWindDirectionY_MaxValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindDirectionY(Byte.MAX_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(Byte.MAX_VALUE));

            builder.setExpectedWindDirectionY((byte) (Byte.MAX_VALUE + 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(not(Byte.MAX_VALUE + 1)));
        }

        @Test
        public void getWindDirectionY_MinValue() throws Exception {
            final GameStatePacketBuilder builder = new GameStatePacketBuilder()
                    .setExpectedWindDirectionY(Byte.MIN_VALUE);
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(Byte.MIN_VALUE));

            builder.setExpectedWindDirectionY((byte) (Byte.MIN_VALUE - 1));
            packet = new GameStatePacket(builder.build());
            assertThat(packet.getWindDirectionY(), is(not(Byte.MIN_VALUE - 1)));
        }
    }
}
