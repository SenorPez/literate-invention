package com.senorpez.projectcars.packetcapture;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketWriterService extends Service<Void> {
    private final BlockingQueue<byte[]> queue;
    private final SimpleIntegerProperty queueSize;
    private Writer writer;
    private final AtomicInteger packetsWritten = new AtomicInteger(0);
    private final Logger logger = LoggerFactory.getLogger(PacketWriterService.class);

    PacketWriterService(final BlockingQueue<byte[]> queue, final SimpleIntegerProperty queueSize) {
        this.queue = queue;
        this.queueSize = queueSize;
    }

    void start(final Writer writer) throws InterruptedException {
        this.writer = writer;
        super.start();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            class InvalidPacketException extends Throwable {
            }

            @Override
            protected Void call() throws Exception {
                while (!isCancelled()) {
                    try {
                        final byte[] packetData = queue.take();

                        writer.writePacket(packetData);
                        queueSize.setValue(queue.size());

                        if (packetsWritten.incrementAndGet() % 100 == 0) {
                            logger.info(String.format("%d packets written", packetsWritten.get()));
                        }
                    } catch (final InterruptedException e) {
                        super.cancel();
                    } catch (final IOException e) {
                        logger.warn("IOException");
                    }
                }
                return null;
            }

            @Override
            protected void cancelled() {
                while (queue.size() > 0) {
                    logger.info(String.format("Writing %d remaining queue items", queue.size()));
                    final byte[] packetData = queue.remove();

                    try {
                        writer.writePacket(packetData);
                        queueSize.setValue(queue.size());
                    } catch (final IOException ignored) {}
                }
                super.cancelled();
                logger.info("Shutting Down");
            }
        };
    }
}
