package com.senorpez.projectcars.packetcapture;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class PacketReaderTest {
    private BlockingQueue<byte[]> queue;
    private Thread readerThread;

    @Before
    public void setUp() throws Exception {
        queue = (BlockingQueue<byte[]>) mock(BlockingQueue.class);
        final PacketReader reader = new PacketReader(queue);
        readerThread = new Thread(reader);
        readerThread.start();
        Thread.sleep(500);
    }

    @Test
    public void run() throws Exception {
        when(queue.add(any())).thenReturn(true);

        packetSender(10);
        Thread.sleep(500);

        readerThread.interrupt();
        readerThread.join();
        verify(queue, times(10)).add(any());
    }

    @Test
    public void run_InsufficientCapacity() throws Exception {
        when(queue.add(any())).thenThrow(new IllegalStateException());
        packetSender(10);
        Thread.sleep(500);

        readerThread.interrupt();
        readerThread.join();
        verify(queue, times(10)).add(any());
    }

    private void packetSender(final int count) throws SocketException {
        final DatagramSocket socket = new DatagramSocket();

        IntStream.range(0, count)
                .forEach(value -> {
                    final byte[] packetData = new byte[]{8, 6, 7, 5, 3, 0, 9};
                    final DatagramPacket packet;
                    try {
                        packet = new DatagramPacket(packetData, packetData.length, InetAddress.getByName("255.255.255.255"), 5606);
                        socket.send(packet);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Packet: " + value);
                });
        socket.close();
    }
}