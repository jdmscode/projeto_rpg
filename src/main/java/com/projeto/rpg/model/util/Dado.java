package com.projeto.rpg.model.util;

import java.util.Random;

public class Dado {
    private static Random rng = new Random();

    public static void setSeed(long seed) {
        rng = new Random(seed);
    }

    public static int rolar(int faces) {
        if (faces <= 0) throw new IllegalArgumentException("faces deve ser > 0");
        return rng.nextInt(faces) + 1; // 1..faces
    }
}
