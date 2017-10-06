package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class SimplePCAPNGWriter implements PCAPNGWriter {
    private final OutputStream outputStream;

    SimplePCAPNGWriter(final OutputStream outputStream) throws IOException {
        this.outputStream = outputStream;
        writeSectionHeaderBlock();
        writeInterfaceDescriptionBlock();
    }

    @Override
    public void writeSectionHeaderBlock() throws IOException {
        final ByteBuffer blockType = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put(new byte[]{0x0A, 0x0D, 0x0D, 0x0A});
        final ByteBuffer blockLength = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put((byte) 28);
        final ByteBuffer byteOrderMagic = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put(new byte[]{0x1A, 0x2B, 0x3C, 0x4D});
        final ByteBuffer majorVersion = ByteBuffer.allocate(2).order(LITTLE_ENDIAN).put((byte) 1);
        final ByteBuffer minorVersion = ByteBuffer.allocate(2).order(LITTLE_ENDIAN).put((byte) 0);
        final ByteBuffer sectionLength = ByteBuffer.allocate(8).order(LITTLE_ENDIAN).put((byte) 0);

        outputStream.write(blockType.array());
        outputStream.write(blockLength.array());
        outputStream.write(byteOrderMagic.array());
        outputStream.write(majorVersion.array());
        outputStream.write(minorVersion.array());
        outputStream.write(sectionLength.array());
        outputStream.write(blockLength.array());
        blockLength.rewind();
    }

    @Override
    public void writeInterfaceDescriptionBlock() throws IOException {
        final ByteBuffer blockType = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put((byte) 1);
        final ByteBuffer blockLength = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put((byte) 20);
        final ByteBuffer linkType = ByteBuffer.allocate(2).order(LITTLE_ENDIAN).put((byte) 0);
        final ByteBuffer reserved = ByteBuffer.allocate(2).order(LITTLE_ENDIAN).put((byte) 0);
        final ByteBuffer snapLen = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put((byte) 0);

        outputStream.write(blockType.array());
        outputStream.write(blockLength.array());
        outputStream.write(linkType.array());
        outputStream.write(reserved.array());
        outputStream.write(snapLen.array());
        outputStream.write(blockLength.array());
        blockLength.rewind();
    }

    @Override
    public void writeSimplePacketBlock(final DatagramPacket packet) throws IOException {
        final ByteBuffer blockType = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).put((byte) 3);
        final ByteBuffer blockLength = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).putInt(getPacketPaddedLength(packet.getLength()) + 16);
        final ByteBuffer originalPacketLength = ByteBuffer.allocate(4).order(LITTLE_ENDIAN).putInt(packet.getLength());

        outputStream.write(blockType.array());
        outputStream.write(blockLength.array());
        outputStream.write(originalPacketLength.array());
        outputStream.write(packet.getData(), 0, getPacketPaddedLength(packet.getLength()));

        outputStream.write(blockLength.array());
        blockLength.rewind();
    }

    @Override
    public void writePacket(final DatagramPacket packet) throws IOException {
        writeSimplePacketBlock(packet);
    }

    private int getPacketPaddedLength(final int length) {
        return length + 4 - length % 4;
    }
}
