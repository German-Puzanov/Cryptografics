package kursach;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Cipher {

    private static final BigInteger FNV_OFFSET_BASIS =
            new BigInteger("dd268dbcaac550362d98c384c4e576ccc8b1536847b6bbb31023b4c8caee0535", 16);

    private static final BigInteger FNV_PRIME =
            BigInteger.ONE.shiftLeft(168)
                    .add(BigInteger.ONE.shiftLeft(8))
                    .add(BigInteger.valueOf(0x63));

    private static final BigInteger MASK_256 =
            BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);

    public static BigInteger cipher(BigInteger message, BigInteger exponent, BigInteger num) {
        return message.modPow(exponent, num);
    }

    public static BigInteger hashAlgorithmFNV(String s) {
        BigInteger hash = FNV_OFFSET_BASIS;
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            hash = hash.xor(BigInteger.valueOf(b & 0xFF));
            hash = hash.multiply(FNV_PRIME).and(MASK_256);
        }
        return hash;
    }

    public static BigInteger hashAlgorithmFNV(Path path) {
        BigInteger hash = FNV_OFFSET_BASIS;
        byte[] buf = new byte[32 * 1024];
        try (InputStream in = Files.newInputStream(path)) {
            int n;
            while ((n = in.read(buf)) != -1) {
                for (int i = 0; i < n; i++) {
                    hash = hash.xor(BigInteger.valueOf(buf[i] & 0xFF));
                    hash = hash.multiply(FNV_PRIME).and(MASK_256); // mod 2^256
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return hash;


    }
}
