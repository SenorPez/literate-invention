package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

class PacketReader implements Runnable {
    private static final DatagramSocket SOCKET;
    private static final byte[] buf = new byte[Short.MAX_VALUE];

    private final BlockingQueue<DatagramPacket> queue;
    private boolean cancelled = false;

    static {
        DatagramSocket newSocket = null;
        try {
            newSocket = new DatagramSocket(5606);
            newSocket.setReuseAddress(true);
        } catch (final SocketException e) {
            e.printStackTrace();
        }
        SOCKET = newSocket;
    }

    PacketReader(final BlockingQueue<DatagramPacket> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!cancelled) {
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                SOCKET.receive(packet);
                queue.add(packet);
            } catch (final IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    void cancel() {
        cancelled = true;
    }
}
