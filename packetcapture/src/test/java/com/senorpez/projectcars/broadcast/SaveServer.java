package com.senorpez.projectcars.broadcast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class SaveServer {
    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket();
            socket.setReuseAddress(true);
        } catch (final SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final PushbackInputStream file = new PushbackInputStream(new FileInputStream(new File("C:\\Users\\502625185\\Downloads\\ginetta2sprint.pcars")), Short.MAX_VALUE);
        getSectionHeaderBlock(file);
        getInterfaceDescriptionBlock(file);

        int packetLength;

        while (file.available() > 0) {
            final byte[] simplePacketBlock = getSimplePacketBlock(file);
            packetLength = getOriginalPacketLength(simplePacketBlock);

            final byte[] packetData = Arrays.copyOfRange(simplePacketBlock, 12, 12 + packetLength);
            final DatagramPacket packet = new DatagramPacket(packetData, packetData.length, InetAddress.getByName("255.255.255.255"), 5606);
            socket.send(packet);
            System.out.println("Broadcasted packet.");
        }
    }

    private static void getSectionHeaderBlock(final PushbackInputStream file) throws IOException {
        final byte[] blockType = new byte[]{0x0A, 0x0D, 0x0D, 0x0A};

        final byte[] blockTypeBuffer = new byte[4];
        assert file.read(blockTypeBuffer) == 4;
        assert Arrays.equals(blockTypeBuffer, blockType);

        final byte[] blockLengthBuffer = new byte[4];
        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] sectionHeaderBlock = new byte[blockLength];
        assert file.read(sectionHeaderBlock) == blockLength;
    }

    private static void getInterfaceDescriptionBlock(final PushbackInputStream file) throws IOException {
        final int blockType = 1;

        final byte[] blockTypeBuffer = new byte[4];
        assert file.read(blockTypeBuffer) == 4;
        assert ByteBuffer.wrap(blockTypeBuffer).order(LITTLE_ENDIAN).getInt() == blockType;

        final byte[] blockLengthBuffer = new byte[4];
        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] interfaceDescriptionBlock = new byte[blockLength];
        assert file.read(interfaceDescriptionBlock) == blockLength;
    }

    private static byte[] getSimplePacketBlock(final PushbackInputStream file) throws IOException {
        final int blockType = 3;

        final byte[] blockTypeBuffer = new byte[4];
        assert file.read(blockTypeBuffer) == 4;
        assert ByteBuffer.wrap(blockTypeBuffer).order(LITTLE_ENDIAN).getInt() == blockType;

        final byte[] blockLengthBuffer = new byte[4];
        assert file.read(blockLengthBuffer) == 4;
        final int blockLength = ByteBuffer.wrap(blockLengthBuffer).order(LITTLE_ENDIAN).getInt();

        file.unread(blockLengthBuffer);
        file.unread(blockTypeBuffer);

        final byte[] simplePacketBlock = new byte[blockLength];
        assert file.read(simplePacketBlock) == blockLength;
        return simplePacketBlock;
    }

    private static int getOriginalPacketLength(final byte[] simplePacketBlock) throws IOException {
        final byte[] originalPacketLengthBuffer = Arrays.copyOfRange(simplePacketBlock, 8, 12);
        assert originalPacketLengthBuffer.length == 4;
        return ByteBuffer.wrap(originalPacketLengthBuffer).order(LITTLE_ENDIAN).getInt();
    }

}
