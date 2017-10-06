package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;

interface PCAPNGWriter extends Writer {
    void writeSectionHeaderBlock() throws IOException;
    void writeInterfaceDescriptionBlock() throws IOException;
    void writeSimplePacketBlock(DatagramPacket packet) throws IOException;
}
