package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;

class PacketReader implements Runnable {
    private static final ByteBuffer buf = ByteBuffer.allocate(Short.MAX_VALUE);

    private final DatagramChannel channel;
    private final BlockingQueue<DatagramPacket> queue;

    private boolean cancelled = false;

    PacketReader(final BlockingQueue<DatagramPacket> queue) throws IOException {
        this.queue = queue;

        DatagramChannel newChannel;
        newChannel = DatagramChannel.open();
        newChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        newChannel.setOption(StandardSocketOptions.SO_BROADCAST, true);
        newChannel.bind(new InetSocketAddress(5606));
        channel = newChannel;
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
            } catch (final IOException e) {
                cancel();
                try {
                    channel.close();
                } catch (final IOException ignored) {}
            } catch (final IllegalStateException ignored) {
                // TODO: 10/03/17 Actual Logging
                System.out.println("Error: Queue full. Is a writer running?");
            }
        }
    }

    void cancel() {
        cancelled = true;
    }
}
