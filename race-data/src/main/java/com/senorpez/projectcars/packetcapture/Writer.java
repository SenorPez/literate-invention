package com.senorpez.projectcars.packetcapture;

import java.net.DatagramPacket;

public interface Writer {
    void writePacket(DatagramPacket packet);
}
