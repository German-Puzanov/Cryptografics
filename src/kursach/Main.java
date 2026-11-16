package kursach;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static final long FIRST_PRIME = 104729;
    public static final long SECOND_PRIME = 99991;
    public static final long OPEN_EXPONENT = 65537;
    public static final String OPEN_KEY_NAME = "Open key";
    public static final String SECRET_KEY_NAME = "Secret key";
    public static final String FILE_PATH = "src/input/img.png";

    public static void main(String[] args) throws IOException {
        UserManager manager = new UserManager();
        Scanner scanner = new Scanner(System.in);
        String command;


        while (true) {
            manager.printMenu();
            command = scanner.nextLine();

            switch (command) {
                case "1" -> {

                }
                case "2" -> {

                }
                case "3" -> {

                }
                case "4" -> {

                }
                case "0" -> {
                    System.out.println("До новых встреч");
                    return;
                }
            }
        }
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

    public static long cipher(long message, long exponent, long num) {
        BigInteger messageBig = BigInteger.valueOf(message);
        BigInteger openExponentBig = BigInteger.valueOf(exponent);
        BigInteger numBig = BigInteger.valueOf(num);

        return messageBig.modPow(openExponentBig, numBig).longValue();

    }

    public static Map<String, List<Long>> keyGenerator() {

        Map<String, List<Long>> personalKeys = new HashMap<>();

        long firstPrime = new Random().longs(OPEN_EXPONENT, 100000).filter(Main::isPrime).findFirst().orElse(FIRST_PRIME);
        long secondPrime = new Random().longs(OPEN_EXPONENT, 100000).filter(n -> isPrime(n) && n != firstPrime).findFirst().orElse(SECOND_PRIME);

        long eulerFunc = (firstPrime - 1) * (secondPrime - 1);
        long openExponent = OPEN_EXPONENT;

        long k = 1;
        while ((k * eulerFunc + 1) % openExponent != 0) {
            k++;
        }
        long secretExponent = (k * eulerFunc + 1) / openExponent;

        long n = firstPrime * secondPrime;

        personalKeys.put(OPEN_KEY_NAME, List.of(openExponent, n));
        personalKeys.put(SECRET_KEY_NAME, List.of(secretExponent, n));

        return personalKeys;
    }

    public static long hashAlgorithmFNV(String s) {
        int basis = 0x811C9DC5;
        int prime = 0x01000193;
        byte[] bytes = s.getBytes();

        for (byte b : bytes) {
            basis ^= ((b & 0xFF));
            basis *= prime;
        }
        return basis & 0xFFFFFFFFL;
    }

    public static long hashAlgorithmFNV(Path path) {
        int basis = 0x811C9DC5;
        int prime = 0x01000193;

        byte[] buf = new byte[32 * 1024];

        try (InputStream in = Files.newInputStream(path)) {
            int n;
            while ((n = in.read(buf)) != -1) {
                for (int i = 0; i < n; i++) {
                    basis ^= (buf[i] & 0xFF);
                    basis *= prime;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basis & 0xFFFFFFFFL;
    }

}