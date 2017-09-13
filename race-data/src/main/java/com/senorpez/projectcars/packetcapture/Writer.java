package com.senorpez.projectcars.packetcapture;

import java.net.DatagramPacket;

interface Writer {
    void writePacket(DatagramPacket packet);
}
