package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class PacketReader implements Runnable {
    private static DatagramSocket socket;
    private static final byte[] buf = new byte[Short.MAX_VALUE];
    private final BlockingQueue<DatagramPacket> queue;

    static {
        try {
            socket = new DatagramSocket(5606);
            socket.setReuseAddress(true);
        } catch (final SocketException e) {
            e.printStackTrace();
        }
    }

    PacketReader(final BlockingQueue<DatagramPacket> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                queue.add(packet);
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
