package com.senorpez.projectcars2.racedata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class Application {
    public static void main(String[] args) throws IOException, InvalidPacketDataException {
        final PushbackInputStream file = new PushbackInputStream(new FileInputStream(new File("C:\\Users\\senor\\Desktop\\pcars2.pcars")), Short.MAX_VALUE);
        getSectionHeaderBlock(file);
        getInterfaceDescriptionBlock(file);

        while (file.available() > 0) {
            final byte[] simplePacketBlock = getSimplePacketBlockData(getSimplePacketBlock(file));

            ByteBuffer data = ByteBuffer.wrap(simplePacketBlock).order(LITTLE_ENDIAN);
            Packet packet = PacketType.getPacket(data);

            if (packet instanceof ParticipantsPacket) {
                ParticipantsPacket participantsPacket = (ParticipantsPacket) packet;
                System.out.println(participantsPacket.getParticipantsChangedTimestamp());
            }

            if (packet instanceof TimingsPacket) {
                TimingsPacket timingsPacket = (TimingsPacket) packet;
                System.out.println(timingsPacket.getParticipantsChangedTimestamp());
            }

            if (packet instanceof TimeStatsPacket) {
                TimeStatsPacket timeStatsPacket = (TimeStatsPacket) packet;
                System.out.println(timeStatsPacket.getParticipantsChangedTimestamp());
            }
        }
    }

    private static void getSectionHeaderBlock(final PushbackInputStream file) throws IOException {
        final byte[] blockType = new byte[]{0x0A, 0x0D, 0x0D, 0x0A};

        final byte[] blockTypeBuffer = new byte[4];
        file.read(blockTypeBuffer);
//        assert file.read(blockTypeBuffer) == 4;
//        assert Arrays.equals(blockTypeBuffer, blockType);

        final byte[] blockLengthBuffer = new byte[4];
        file.read(blockLengthBuffer);
//        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] sectionHeaderBlock = new byte[blockLength];
        file.read(sectionHeaderBlock);
//        assert file.read(sectionHeaderBlock) == blockLength;
    }

    private static void getInterfaceDescriptionBlock(final PushbackInputStream file) throws IOException {
        final int blockType = 1;

        final byte[] blockTypeBuffer = new byte[4];
        file.read(blockTypeBuffer);
//        assert file.read(blockTypeBuffer) == 4;
//        assert ByteBuffer.wrap(blockTypeBuffer).order(LITTLE_ENDIAN).getInt() == blockType;

        final byte[] blockLengthBuffer = new byte[4];
        file.read(blockLengthBuffer);
//        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] interfaceDescriptionBlock = new byte[blockLength];
        file.read(interfaceDescriptionBlock);
//        assert file.read(interfaceDescriptionBlock) == blockLength;
    }

    private static byte[] getSimplePacketBlock(final PushbackInputStream file) throws IOException {
        final int blockType = 3;

        final byte[] blockTypeBuffer = new byte[4];
        file.read(blockTypeBuffer);
//        assert file.read(blockTypeBuffer) == 4;
//        assert ByteBuffer.wrap(blockTypeBuffer).order(LITTLE_ENDIAN).getInt() == blockType;

        final byte[] blockLengthBuffer = new byte[4];
        file.read(blockLengthBuffer);
//        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] simplePacketBlock = new byte[blockLength];
        file.read(simplePacketBlock);
//        assert file.read(simplePacketBlock) == blockLength;
        return simplePacketBlock;
    }

    private static int getOriginalPacketLength(final byte[] simplePacketBlock) throws IOException {
        final byte[] originalPacketLengthBuffer = Arrays.copyOfRange(simplePacketBlock, 8, 12);
//        assert originalPacketLengthBuffer.length == 4;
        return ByteBuffer.wrap(originalPacketLengthBuffer).order(LITTLE_ENDIAN).getInt();
    }

    private static byte[] getSimplePacketBlockData(final byte[] simplePacketBlock) throws IOException {
        final int originalPacketLength = getOriginalPacketLength(simplePacketBlock);
        return Arrays.copyOfRange(simplePacketBlock, 12, 12 + originalPacketLength);
    }
}
