package kursach;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyGenerator {
    private static final BigInteger OPEN_EXPONENT = BigInteger.valueOf(65537);
    private static final int KEY_SIZE = 512;
    static final String OPEN_KEY_NAME = "Open key";
    static final String SECRET_KEY_NAME = "Secret key";

    public static Map<String, List<BigInteger>> generateKey() {
        Map<String, List<BigInteger>> personalKeys = new HashMap<>();

        SecureRandom random = new SecureRandom();
        BigInteger firstPrime, secondPrime, n, eulerFunc, secretExponent;
        do {
            firstPrime = BigInteger.probablePrime(KEY_SIZE / 2, random);
            secondPrime = BigInteger.probablePrime(KEY_SIZE / 2, random);
            eulerFunc = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));
        } while (firstPrime.equals(secondPrime) || !eulerFunc.gcd(OPEN_EXPONENT).equals(BigInteger.ONE));

        n = firstPrime.multiply(secondPrime);

        secretExponent = OPEN_EXPONENT.modInverse(eulerFunc);

        personalKeys.put(OPEN_KEY_NAME, List.of(OPEN_EXPONENT, n));
        personalKeys.put(SECRET_KEY_NAME, List.of(secretExponent, n));

        return personalKeys;
    }

}
