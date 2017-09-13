package com.senorpez.projectcars.racedata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class UDPCapture implements Runnable {
    DatagramChannel inputChannel;
    Pipe.SinkChannel outputChannel;
    ByteBuffer buffer = ByteBuffer.allocate(2048);

    public UDPCapture(DatagramChannel inputChannel, Pipe.SinkChannel outputChannel) {
        this.inputChannel = inputChannel;
        this.outputChannel = outputChannel;
        buffer.clear();
    }

    @Override
    public void run() {
        while (true) {
            try {
                inputChannel.receive(buffer);

                int length = buffer.position();
                buffer.flip();

                if (length > 0) {
                    ByteBuffer lengthBuffer = ByteBuffer.allocate(2048).order(LITTLE_ENDIAN).putShort((short) length);
                    lengthBuffer.flip();
                    outputChannel.write(new ByteBuffer[]{lengthBuffer, buffer});
                }
                buffer.clear();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
