package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

class PacketWriter implements Runnable {
    private final BlockingQueue<DatagramPacket> queue;
    private final Writer writer;

    PacketWriter(final BlockingQueue<DatagramPacket> queue, final Writer writer) throws IOException {
        this.queue = queue;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final DatagramPacket packet = queue.take();
                writer.writePacket(packet);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
