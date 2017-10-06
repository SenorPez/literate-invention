package com.senorpez.projectcars.packetcapture;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimplePCAPNGWriterTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final static byte[] expectedBlockTypeBytes = new byte[]{0x0A, 0x0D, 0x0D, 0x0A};
    private final static byte[] expectedByteOrderMagic = new byte[]{0x1A, 0x2B, 0x3C, 0x4D};
    private final static short expectedMajorVersion = 1;
    private final static short expectedMinorVersion = 0;
    private final static long expectedSectionLength = 0;
    private final static int expectedSectionHeaderBlockLength = 28 + (int) expectedSectionLength;

    private final static int expectedInterfaceDescriptionBlockType = 1;
    private final static int expectedInterfaceDescriptionBlockLength = 20;
    private final static short expectedLinkType = 0;
    private final static short expectedReserved = 0;
    private final static int expectedSnapLen = 0;

    private final static byte[] packetData = new byte[]{8, 6, 7, 5, 3, 0, 9};
    private final static int paddedPacketLength = 8;
    private final static byte[] paddedPacketData = Arrays.copyOf(packetData, paddedPacketLength);

    private final static int expectedSimplePacketBlockType = 3;
    private final static int expectedSimplePacketBlockLength = 16 + paddedPacketLength;
    private final static int expectedPacketLength = 7;

    private SimplePCAPNGWriter writer;


    @Before
    public void setUp() throws Exception {
        writer = new SimplePCAPNGWriter(outputStream);
    }

    @Test
    public void writeSectionHeaderBlock() throws Exception {
        final byte[] outputBytes = outputStream.toByteArray();

        final int blockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, 4, 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        assertThat(blockLength, is(expectedSectionHeaderBlockLength));

        final ByteBuffer sectionHeaderBlock = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, 0, blockLength))
                .order(LITTLE_ENDIAN);

        final byte[] blockTypeBytes = new byte[4];
        sectionHeaderBlock.get(blockTypeBytes);
        assertThat(blockTypeBytes, is(expectedBlockTypeBytes));

        assertThat(sectionHeaderBlock.getInt(), is(expectedSectionHeaderBlockLength));

        final byte[] byteOrderMagic = new byte[4];
        sectionHeaderBlock.get(byteOrderMagic);
        assertThat(byteOrderMagic, is(expectedByteOrderMagic));

        assertThat(sectionHeaderBlock.getShort(), is(expectedMajorVersion));
        assertThat(sectionHeaderBlock.getShort(), is(expectedMinorVersion));
        assertThat(sectionHeaderBlock.getLong(), is(expectedSectionLength));
        assertThat(sectionHeaderBlock.getInt(), is(expectedSectionHeaderBlockLength));

        assertThat(sectionHeaderBlock.remaining(), is(0));
    }

    @Test
    public void writeInterfaceDescriptionBlock() throws Exception {
        final byte[] outputBytes = outputStream.toByteArray();

        final int sectionHeaderBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, 4, 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        final int blockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + 4, sectionHeaderBlockLength + 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        assertThat(blockLength, is(expectedInterfaceDescriptionBlockLength));

        final ByteBuffer interfaceDescriptionBlock = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength, sectionHeaderBlockLength + blockLength))
                .order(LITTLE_ENDIAN);

        assertThat(interfaceDescriptionBlock.getInt(), is(expectedInterfaceDescriptionBlockType));
        assertThat(interfaceDescriptionBlock.getInt(), is(expectedInterfaceDescriptionBlockLength));
        assertThat(interfaceDescriptionBlock.getShort(), is(expectedLinkType));
        assertThat(interfaceDescriptionBlock.getShort(), is(expectedReserved));
        assertThat(interfaceDescriptionBlock.getInt(), is(expectedSnapLen));
        assertThat(interfaceDescriptionBlock.getInt(), is(expectedInterfaceDescriptionBlockLength));

        assertThat(interfaceDescriptionBlock.remaining(), is(0));
    }

    @Test
    public void writeSimplePacketBlock() throws Exception {
        final ByteBuffer buf = ByteBuffer.allocate(Short.MAX_VALUE);
        buf.clear();
        buf.put(packetData);
        buf.flip();
        final DatagramPacket packet = new DatagramPacket(buf.array(), buf.limit());

        writer.writeSimplePacketBlock(packet);

        final byte[] outputBytes = outputStream.toByteArray();

        final int sectionHeaderBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, 4, 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        final int interfaceDescriptionBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + 4, sectionHeaderBlockLength + 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        final int simplePacketBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + interfaceDescriptionBlockLength + 4, sectionHeaderBlockLength + interfaceDescriptionBlockLength + 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        assertThat(simplePacketBlockLength, is(expectedSimplePacketBlockLength));

        final ByteBuffer simplePacketBlock = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + interfaceDescriptionBlockLength, sectionHeaderBlockLength + interfaceDescriptionBlockLength + simplePacketBlockLength))
                .order(LITTLE_ENDIAN);

        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockType));
        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockLength));
        assertThat(simplePacketBlock.getInt(), is(expectedPacketLength));

        final byte retrievedData[] = new byte[paddedPacketLength];

        simplePacketBlock.get(retrievedData);
        assertThat(retrievedData, is(paddedPacketData));
        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockLength));
        assertThat(simplePacketBlock.remaining(), is(0));
    }

    @Test
    public void writePacket() throws Exception {
        final ByteBuffer buf = ByteBuffer.allocate(Short.MAX_VALUE);
        buf.clear();
        buf.put(packetData);
        buf.flip();
        final DatagramPacket packet = new DatagramPacket(buf.array(), buf.limit());

        writer.writePacket(packet);

        final byte[] outputBytes = outputStream.toByteArray();

        final int sectionHeaderBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, 4, 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        final int interfaceDescriptionBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + 4, sectionHeaderBlockLength + 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        final int simplePacketBlockLength = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + interfaceDescriptionBlockLength + 4, sectionHeaderBlockLength + interfaceDescriptionBlockLength + 8))
                .order(LITTLE_ENDIAN)
                .getInt();
        assertThat(simplePacketBlockLength, is(expectedSimplePacketBlockLength));

        final ByteBuffer simplePacketBlock = ByteBuffer
                .wrap(Arrays.copyOfRange(outputBytes, sectionHeaderBlockLength + interfaceDescriptionBlockLength, sectionHeaderBlockLength + interfaceDescriptionBlockLength + simplePacketBlockLength))
                .order(LITTLE_ENDIAN);

        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockType));
        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockLength));
        assertThat(simplePacketBlock.getInt(), is(expectedPacketLength));

        final byte retrievedData[] = new byte[paddedPacketLength];

        simplePacketBlock.get(retrievedData);
        assertThat(retrievedData, is(paddedPacketData));
        assertThat(simplePacketBlock.getInt(), is(expectedSimplePacketBlockLength));
        assertThat(simplePacketBlock.remaining(), is(0));
    }
}