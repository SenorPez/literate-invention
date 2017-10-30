package com.senorpez.projectcars.packetcapture;

import java.io.IOException;

interface PCAPNGWriter extends Writer {
    void writeSectionHeaderBlock() throws IOException;
    void writeInterfaceDescriptionBlock() throws IOException;
    void writeSimplePacketBlock(byte[] packet) throws IOException;
}
