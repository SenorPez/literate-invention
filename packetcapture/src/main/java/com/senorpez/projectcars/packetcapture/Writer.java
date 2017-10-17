package com.senorpez.projectcars.packetcapture;

import java.io.IOException;

interface Writer {
    void writePacket(byte[] packet) throws IOException;
}
