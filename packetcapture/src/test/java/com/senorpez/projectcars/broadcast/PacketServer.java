package com.senorpez.projectcars.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PacketServer {
    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket();
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
        } catch (final SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        Files.list(Paths.get(args[0]))
                .sorted((f1, f2) -> {
                    Integer n1 = Integer.valueOf(f1.toFile().getName().replaceAll("[^\\d]", ""));
                    Integer n2 = Integer.valueOf(f2.toFile().getName().replaceAll("[^\\d]", ""));
                    return n1.compareTo(n2);
                }).forEach(PacketServer::broadcastPacket);
    }

    private static void broadcastPacket(final Path packetFile) {
        try {
            final byte[] packetData = Files.readAllBytes(packetFile);
            final DatagramPacket packet = new DatagramPacket(packetData, packetData.length, InetAddress.getByName("255.255.255.255"), 5606);
            socket.send(packet);
            System.out.println("Broadcasted: " + packetFile.getFileName());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
