package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

class PacketWriter implements Runnable {
    private final BlockingQueue<DatagramPacket> queue;
    private final Writer writer;
    private boolean cancelled = false;

    PacketWriter(final BlockingQueue<DatagramPacket> queue, final Writer writer) {
        this.queue = queue;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (!cancelled) {
            try {
                final DatagramPacket packet = queue.take();
                writer.writePacket(packet);
            } catch (final InterruptedException e) {
                cancel();
                while (queue.size() > 0) {
                    final DatagramPacket packet = queue.remove();
                    try {
                        writer.writePacket(packet);
                    } catch (final IOException ignored) {}
                }
            } catch (final IOException e) {
                // TODO: 10/03/17 Actual logging
                System.out.println("Writer error.");
            }
        }
    }

    void cancel() {
        cancelled = true;
    }
}
