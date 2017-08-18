package com.senorpez.projectcars.racedata;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardOpenOption.*;

public class PacketCapture {
    private final static AtomicInteger packetNumber = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true).socket().bind(new InetSocketAddress(5606));
        UDPCapture capture = new UDPCapture(datagramChannel, pipe.sink());

        FileChannel fileChannel = FileChannel.open(Paths.get("output.pcars"), CREATE, TRUNCATE_EXISTING, WRITE);
        FileOutput output = new FileOutput(fileChannel, pipe.source());

        new Thread(capture).start();
        System.out.println("GO!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(output).start();
    }
}
