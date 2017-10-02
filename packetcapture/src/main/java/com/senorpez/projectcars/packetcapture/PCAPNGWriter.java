package com.senorpez.projectcars.packetcapture;

import java.net.DatagramPacket;

interface PCAPNGWriter extends Writer {
    void writeSectionHeaderBlock();
    void writeInterfaceDescriptionBlock();
    void writeSimplePacketBlock(DatagramPacket packet);
}
