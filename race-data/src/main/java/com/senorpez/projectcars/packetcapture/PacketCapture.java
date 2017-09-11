package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PacketCapture {
    public static void main(final String[] args) throws IOException {
        final BlockingQueue<DatagramPacket> queue = new ArrayBlockingQueue<>(10000);

        new PacketCaptureThread(queue).start();
        new PacketCaptureThread(queue, Paths.get(args[0])).start();
    }
}
