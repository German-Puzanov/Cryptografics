package kursach;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Cipher {

    public static long cipher(long message, long exponent, long num) {
        BigInteger messageBig = BigInteger.valueOf(message);
        BigInteger openExponentBig = BigInteger.valueOf(exponent);
        BigInteger numBig = BigInteger.valueOf(num);

        return messageBig.modPow(openExponentBig, numBig).longValue();
    }

    public static long hashAlgorithmFNV(String s) {
        int basis = 0x811C9DC5;
        int prime = 0x01000193;
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

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
