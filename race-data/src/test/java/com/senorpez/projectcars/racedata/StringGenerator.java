package com.senorpez.projectcars.racedata;

import java.util.Random;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

class StringGenerator {
    private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static Random random = new Random();

    static String GetString() {
        return GetString(random.nextInt(64));
    }

    static String GetString(final int length) {
        final StringBuilder nameBuilder = new StringBuilder();
        IntStream.range(0, length)
                .forEach(value -> nameBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
        return nameBuilder.toString();
    }

    static String GetStringWithGarbage(final String string) {
        assert string.getBytes(UTF_8).length < 63;
        final StringBuilder garbageBuilder = new StringBuilder();
        IntStream.range(0, 64 - string.getBytes(UTF_8).length - 1)
                .forEach(value -> garbageBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
        return string + "\u0000" + garbageBuilder.toString();
    }
}
