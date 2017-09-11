package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

class PacketCaptureThread extends Thread {
    PacketCaptureThread(final BlockingQueue<DatagramPacket> queue) {
        super(new PacketReader(queue));
    }

    PacketCaptureThread(final BlockingQueue<DatagramPacket> queue, final Path outputFile) throws IOException {
        super(new FileWriter(queue, outputFile));
    }
}
