package com.senorpez.projectcars.packetcapture;

import java.net.DatagramPacket;

public interface PCAPNGWriter extends Writer {
    void writeSectionHeaderBlock();
    void writeInterfaceDescriptionBlock();
    void writeSimplePacketBlock(DatagramPacket packet);
}
