package com.senorpez.projectcars.racedata;

import java.util.Random;
import java.util.stream.IntStream;

class GarbageGenerator {
    private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static Random random = new Random();

    static String trash(final String string, final int count) {
        final StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, count)
                .forEach(value -> stringBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
        return string + "\u0000" + stringBuilder.toString();
    }
}
