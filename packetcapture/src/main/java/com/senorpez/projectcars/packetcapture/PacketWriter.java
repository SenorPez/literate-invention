package com.senorpez.projectcars.packetcapture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

class PacketWriter implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PacketWriter.class);

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
                    logger.info(String.format("Writing %d remaining queue items", queue.size()));
                    final DatagramPacket packet = queue.remove();
                    try {
                        writer.writePacket(packet);
                    } catch (final IOException ignored) {}
                }
            } catch (final IOException e) {
                logger.warn("IOException");
            }
        }
    }

    void cancel() {
        if (!cancelled) logger.info("Shutting Down");
        cancelled = true;
    }
}
