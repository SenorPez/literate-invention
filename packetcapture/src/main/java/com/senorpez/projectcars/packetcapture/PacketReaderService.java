package com.senorpez.projectcars.packetcapture;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.net.StandardSocketOptions.SO_BROADCAST;
import static java.net.StandardSocketOptions.SO_REUSEADDR;

public class PacketReaderService extends Service<Void> {
    private static final ByteBuffer buf = ByteBuffer.allocate(Short.MAX_VALUE);

    private final BlockingQueue<DatagramPacket> queue;
    private final SimpleIntegerProperty queueSize;
    private final AtomicInteger packetsCaptured = new AtomicInteger(0);
    private final Logger logger = LoggerFactory.getLogger(PacketReaderService.class);

    PacketReaderService(final BlockingQueue<DatagramPacket> queue, final SimpleIntegerProperty queueSize) {
        this.queue = queue;
        this.queueSize = queueSize;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            private DatagramChannel channel;

            @Override
            protected Void call() throws Exception {
                channel = DatagramChannel.open();
                channel.setOption(SO_REUSEADDR, true);
                channel.setOption(SO_BROADCAST, true);
                channel.bind(new InetSocketAddress(5606));
                queueSize.setValue(queue.size());

                while (!isCancelled()) {
                    try {
                        buf.clear();
                        channel.receive(buf);
                        buf.flip();

                        final DatagramPacket packet = new DatagramPacket(buf.array(), buf.limit());
                        queue.add(packet);
                        queueSize.setValue(queue.size());

                        if (packetsCaptured.incrementAndGet() % 100 == 0) {
                            logger.info(String.format("%d packets read", packetsCaptured.get()));
                        }

                        if (queue.remainingCapacity() < queue.size() / 50f)
                            logger.warn("Queue at half capacity. Is a writer running?");
                    } catch (final IOException e) {
                        logger.warn("IOException");
                        super.cancel();
                    } catch (final IllegalStateException ignored) {
                        logger.error("Queue full. Is a writer running?");
                    }
                }
                return null;
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                logger.info("Shutting Down");
                try {
                    channel.close();
                } catch (final IOException ignored) {}
            }
        };
    }
}
