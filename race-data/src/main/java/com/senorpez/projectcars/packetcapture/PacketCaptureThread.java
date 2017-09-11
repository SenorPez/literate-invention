package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

class PacketCaptureThread extends Thread {
    PacketCaptureThread(final BlockingQueue<DatagramPacket> queue) {
        super(new PacketReader(queue));
    }

    PacketCaptureThread(final BlockingQueue<DatagramPacket> queue, final Writer writer) throws IOException {
        super(new PacketWriter(queue, writer));
    }
}
