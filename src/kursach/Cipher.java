package kursach;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cipher {

    public static BigInteger cipher(BigInteger message, BigInteger exponent, BigInteger num) {
        return message.modPow(exponent, num);
    }

    public static BigInteger hashAlgorithmSHA256(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм SHA-256 не найден", e);
        }
    }

    public static BigInteger hashAlgorithmSHA256(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = in.read(buf)) != -1) {
                digest.update(buf, 0, n);
            }
            return new BigInteger(1, digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при чтении файла или хэшировании", e);
        }
    }
}
