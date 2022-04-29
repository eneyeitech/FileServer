package util;

import java.util.Random;

public class StringGenerator {

    private static int leftLimit = 65;
    private static int rightLimit = 122;
    private static Random random = new Random(1000000000);

    public static String generate(int len) {
        return random.ints(leftLimit, rightLimit + 1)
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
