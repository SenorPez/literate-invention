package com.senorpez.projectcars.packetcapture;

import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

class PacketWriter implements Runnable {
    private final BlockingQueue<DatagramPacket> queue;
    private final Writer writer;
    private boolean cancelled;

    PacketWriter(final BlockingQueue<DatagramPacket> queue, final Writer writer) {
        this.queue = queue;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (!cancelled || queue.size() > 0) {
            try {
                final DatagramPacket packet = queue.take();
                writer.writePacket(packet);
            } catch (final InterruptedException e) {
                while (queue.size() > 0) {
                    final DatagramPacket packet = queue.remove();
                    writer.writePacket(packet);
                    cancelled = true;
                }
            }
        }
    }

    void cancel() {
        cancelled = true;
    }
}
