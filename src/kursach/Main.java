package kursach;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    private static final long FIRST_PRIME = 104729;
    private static final long SECOND_PRIME = 99991;
    private static final long OPEN_EXPONENT = 65537;
    private static final String OPEN_KEY_NAME = "Open key";
    private static final String SECRET_KEY_NAME = "Secret key";

    private static final String FILE_PATH = "src/input/img.png";

    public static void main(String[] args) throws IOException {
        // Ключи отправителя (sender) и злоумышленника (attacker)
        var senderKeys = keyGenerator();
        var attackerKeys = keyGenerator();

        // Извлекаем только те ключи, которые реально используются
        long senderE = senderKeys.get(OPEN_KEY_NAME).get(0);
        long senderN = senderKeys.get(OPEN_KEY_NAME).get(1);
        long senderD = senderKeys.get(SECRET_KEY_NAME).get(0);

        long attackerD = attackerKeys.get(SECRET_KEY_NAME).get(0);
        long attackerN = attackerKeys.get(OPEN_KEY_NAME).get(1);

        long originalHash = hashAlgorithmFNV(Path.of(FILE_PATH));

        // =========================================================================
        System.out.println("=== [1] Корректная подпись ===");
        System.out.println("Смысл: Отправитель подписывает закрытым ключом, получатель проверяет открытым.");
        long signature = cipher(originalHash, senderD, senderN);
        long verifiedHash = cipher(signature, senderE, senderN);
        System.out.printf("Данные: Исходный хэш = %d, Подпись = %d, Результат проверки = %d%n",
                originalHash, signature, verifiedHash);
        System.out.println("Вердикт: " + (verifiedHash == originalHash) + "\n");

        // =========================================================================
        System.out.println("=== [2] Подделка подписи ===");
        System.out.println("Смысл: Злоумышленник подписывает своим закрытым ключом, но получатель проверяет открытым ключом отправителя.");
        long forgedSignature = cipher(originalHash, attackerD, attackerN);
        long forgedVerifiedHash = cipher(forgedSignature, senderE, senderN);
        System.out.printf("Данные: Исходный хэш = %d, Поддельная подпись = %d, Результат проверки = %d%n",
                originalHash, forgedSignature, forgedVerifiedHash);
        System.out.println("Вердикт (подделка обнаружена): " + (forgedVerifiedHash != originalHash) + "\n");

        // =========================================================================
        System.out.println("=== [3] Подмена файла ===");
        System.out.println("Смысл: Злоумышленник меняет файл, но не может обновить оригинальную подпись. Получатель видит несовпадение.");
        Path tempFile = Path.of("src/input/img_1.png");
        byte[] data = Files.readAllBytes(Path.of(FILE_PATH));
        data[data.length / 2] ^= 1;
        Files.write(tempFile, data);

        long tamperedHash = hashAlgorithmFNV(tempFile);
        Files.deleteIfExists(tempFile);

        System.out.printf("Данные: Старый хэш = %d, Хэш измененного файла = %d%n",
                originalHash, tamperedHash);
        System.out.println("Вердикт (подмена обнаружена): " + (verifiedHash != tamperedHash));
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

    public static long cipher(long message, long openExponent, long num) {
        BigInteger messageBig = BigInteger.valueOf(message);
        BigInteger openExponentBig = BigInteger.valueOf(openExponent);
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