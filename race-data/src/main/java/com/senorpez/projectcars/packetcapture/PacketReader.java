package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;

class PacketReader implements Runnable {
    private static final DatagramChannel CHANNEL;
    private static final byte[] buf = new byte[Short.MAX_VALUE];

    private final BlockingQueue<DatagramPacket> queue;
    private boolean cancelled = false;

    static {
        DatagramChannel newChannel = null;
        try {
            newChannel = DatagramChannel.open();
            newChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            newChannel.setOption(StandardSocketOptions.SO_BROADCAST, true);
            newChannel.bind(new InetSocketAddress(5606));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        CHANNEL = newChannel;
    }

    PacketReader(final BlockingQueue<DatagramPacket> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!cancelled) {
            try {
                CHANNEL.receive(ByteBuffer.wrap(buf));
                final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                queue.add(packet);
            } catch (final IOException | IllegalStateException e) {
                try {
                    CHANNEL.close();
                } catch (final IOException ignored) {}
            }
        }
    }

    void cancel() {
        cancelled = true;
    }
}
