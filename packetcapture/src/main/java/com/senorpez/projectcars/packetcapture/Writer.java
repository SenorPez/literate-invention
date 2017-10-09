package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;

interface Writer {
    void writePacket(DatagramPacket packet) throws IOException;
}
