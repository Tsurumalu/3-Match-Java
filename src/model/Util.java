package model;

import java.util.Random;

public class Util {
    static Random random = new Random(0);

    public static void init(int seed) {
        random = new Random(seed);
    }

    public static <T> T RandomPick(T[] arr) {
        int randomIndex = (int) (random.nextInt(0, arr.length));
        return arr[randomIndex];
    }
}
