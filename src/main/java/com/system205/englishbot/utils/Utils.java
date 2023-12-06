package com.system205.englishbot.utils;

import java.util.Collection;
import java.util.Random;

public final class Utils {
    private Utils() {}

    private static final Random random = new Random();
    public static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty())
            throw new IllegalArgumentException("The collection is empty");

        final int randomIndex = random.nextInt(collection.size());
        int i = 0;
        for (T element : collection) {
            if (i == randomIndex) {
                return element;
            }
            i++;
        }

        throw new IllegalStateException("The element has to be present. There might be something wrong with random.");
    }
}
