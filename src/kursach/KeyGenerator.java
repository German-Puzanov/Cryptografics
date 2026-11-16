package kursach;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KeyGenerator {
    private static final long OPEN_EXPONENT = 65537;
    static final String OPEN_KEY_NAME = "Open key";
    static final String SECRET_KEY_NAME = "Secret key";

    public static Map<String, List<Long>> generateKey() {
        Map<String, List<Long>> personalKeys = new HashMap<>();

        Random random = new SecureRandom();
        long firstPrime, secondPrime, eulerFunc;
        do {
            firstPrime = randomPrime(random);
            secondPrime = randomPrime(random);
            eulerFunc = (firstPrime - 1) * (secondPrime - 1);
        } while (firstPrime == secondPrime || BigInteger.valueOf(eulerFunc).gcd(BigInteger.valueOf(OPEN_EXPONENT)).intValue() != 1);

        long k = 1;
        while ((k * eulerFunc + 1) % OPEN_EXPONENT != 0) k++;

        long secretExponent = (k * eulerFunc + 1) / OPEN_EXPONENT;

        long n = firstPrime * secondPrime;

        personalKeys.put(OPEN_KEY_NAME, List.of(OPEN_EXPONENT, n));
        personalKeys.put(SECRET_KEY_NAME, List.of(secretExponent, n));

        return personalKeys;
    }

    private static long randomPrime(Random random) {
        return random.longs(OPEN_EXPONENT, 100_000_00).filter(KeyGenerator::isPrime).findFirst().orElseThrow(() -> new IllegalStateException("No prime in range [" + OPEN_EXPONENT + ", 100000)"));
    }

    public static boolean isPrime(long n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
