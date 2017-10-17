package com.senorpez.projectcars.packetcapture;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PacketWriterTest {
    private BlockingQueue<byte[]> queue;
    private Writer writer;
    private Thread writerThread;

    @Before
    public void setUp() throws Exception {
        queue = (BlockingQueue<byte[]>) mock(BlockingQueue.class);
        writer = mock(SimplePCAPNGWriter.class);
    }

    @Test
    public void run() throws Exception {
        when(queue.take()).thenReturn(new byte[]{8, 6, 7, 5, 3, 0, 9});
        doNothing().when(writer).writePacket(any());

        final PacketWriter packetWriter = new PacketWriter(queue, writer);
        writerThread = new Thread(packetWriter);
        writerThread.start();
        Thread.sleep(500);

        packetWriter.cancel();
        writerThread.interrupt();
        writerThread.join();

        verify(queue, atLeast(1)).take();
        verifyNoMoreInteractions(queue);

        verify(writer, atLeast(1)).writePacket(any());
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void run_InterruptTake() throws Exception {
        when(queue.take()).thenThrow(new InterruptedException());
        when(queue.size()).thenReturn(0);
        doNothing().when(writer).writePacket(any());

        final PacketWriter packetWriter = new PacketWriter(queue, writer);
        writerThread = new Thread(packetWriter);
        writerThread.start();
        Thread.sleep(500);

        packetWriter.cancel();
        writerThread.interrupt();
        writerThread.join();

        verify(queue).take();
        verify(queue).size();
        verifyNoMoreInteractions(queue);

        verifyZeroInteractions(writer);
    }

    @Test
    public void run_InterruptTakeWithQueueRemaining() throws Exception {
        when(queue.take())
                .thenReturn(new byte[]{8, 6, 7, 5, 3, 0, 9})
                .thenThrow(new InterruptedException());
        when(queue.size()).thenReturn(3, 3, 2, 2, 1, 1, 0);
        when(queue.remove()).thenReturn(
                new byte[]{8, 6, 7, 5, 3, 0, 9},
                new byte[]{8, 6, 7, 5, 3, 0, 9},
                new byte[]{8, 6, 7, 5, 3, 0, 9});
        doNothing().when(writer).writePacket(any());

        final PacketWriter packetWriter = new PacketWriter(queue, writer);
        writerThread = new Thread(packetWriter);
        writerThread.start();
        Thread.sleep(500);

        packetWriter.cancel();
        writerThread.interrupt();
        writerThread.join();

        verify(queue, times(2)).take();
        verify(queue, times(3)).remove();
        verify(queue, times(7)).size();
        verifyNoMoreInteractions(queue);

        verify(writer, times(4)).writePacket(any());
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void run_WriterIOError() throws Exception {
        when(queue.take()).thenReturn(new byte[]{8, 6, 7, 5, 3, 0, 9});
        doThrow(new IOException()).when(writer).writePacket(any());

        final PacketWriter packetWriter = new PacketWriter(queue, writer);
        writerThread = new Thread(packetWriter);
        writerThread.start();
        Thread.sleep(500);

        packetWriter.cancel();
        writerThread.interrupt();
        writerThread.join();

        verify(queue, atLeast(1)).take();
        verifyNoMoreInteractions(queue);

        verify(writer, atLeast(1)).writePacket(any());
        verifyNoMoreInteractions(writer);
    }
}