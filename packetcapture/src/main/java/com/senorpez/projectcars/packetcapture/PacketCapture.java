package com.senorpez.projectcars.packetcapture;

import java.io.IOException;
import java.util.stream.Stream;

public class PacketCapture {
    public static void main(final String[] args) throws IOException {
        if (args.length > 0 && args[0].equalsIgnoreCase("headless")) {
            PacketCaptureHeadless.main(args);
        } else {
            try {
                PacketCaptureGUI.main(args);
            } catch (final UnsupportedOperationException e) {
                final String[] newArgs = new String[]{"headless"};
                PacketCaptureHeadless.main(
                        Stream.of(newArgs, args).flatMap(Stream::of).toArray(String[]::new)
                );
            }
        }
    }
}
