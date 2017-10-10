package com.senorpez.projectcars.packetcapture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class PacketReader implements Runnable {
    private static final ByteBuffer buf = ByteBuffer.allocate(Short.MAX_VALUE);
    private static final Logger logger = LoggerFactory.getLogger(PacketReader.class);

    private final AtomicInteger packetsCaptured = new AtomicInteger(0);

    private final DatagramChannel channel;
    private final BlockingQueue<DatagramPacket> queue;

    private boolean cancelled = false;

    PacketReader(final BlockingQueue<DatagramPacket> queue) throws IOException {
        this.queue = queue;

        final DatagramChannel channel;
        channel = DatagramChannel.open();
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.setOption(StandardSocketOptions.SO_BROADCAST, true);
        channel.bind(new InetSocketAddress(5606));
        this.channel = channel;
    }

    @Override
    public void run() {
        while (!cancelled) {
            try {
                buf.clear();
                channel.receive(buf);
                buf.flip();
                final DatagramPacket packet = new DatagramPacket(buf.array(), buf.limit());
                queue.add(packet);

                if (packetsCaptured.incrementAndGet() % 100 == 0) {
                    logger.info(String.format("%d packets read", packetsCaptured.get()));
                }

                if (queue.remainingCapacity() < queue.size() / 50)
                    logger.warn("Queue at half capacity. Is a writer running?");
            } catch (final IOException e) {
                logger.warn("IOException");
                cancel();
                try {
                    channel.close();
                } catch (final IOException ignored) {}
            } catch (final IllegalStateException ignored) {
                logger.error("Queue full. Is a writer running?");
            }
        }
    }

    void cancel() {
        if (!cancelled) logger.info("Shutting Down");
        cancelled = true;
    }
}
