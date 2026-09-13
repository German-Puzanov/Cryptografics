package cesar_first_lab;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CipherFirstLab {
    private int key = 0;
    private String text = null;
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ,.:;?!-()[]";

    public CipherFirstLab() {
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Invalid text!");
        this.text = text;
    }


    public void setText(Path path) {
        try (FileReader reader = new FileReader(path.toFile())) {
            int chr;
            StringBuilder sb = new StringBuilder();
            while ((chr = reader.read()) != -1) {
                sb.append((char) chr);
            }
            if (sb.isEmpty()) throw new IllegalArgumentException("Invalid text!");
            text = sb.toString();

        } catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    public String cesarCipher(boolean encode) {
        int key = encode ? this.key : this.key * -1;
        char[] charArray = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (Character chr : charArray) {
            String cipherChar;
            String lowerChr = chr.toString().toLowerCase();
            if (inAlphabet(lowerChr)) {
                int index = ALPHABET.indexOf(lowerChr);
                int newIndex = (index + key) % ALPHABET.length();
                if (newIndex < 0) newIndex += ALPHABET.length();
                if (chr.toString().equals(lowerChr)){
                    cipherChar = String.valueOf(ALPHABET.charAt(newIndex));
                } else {
                    cipherChar = String.valueOf(ALPHABET.charAt(newIndex)).toUpperCase();
                }
                sb.append(cipherChar);
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }


    private boolean inAlphabet(String chr) {
        return ALPHABET.contains(chr);
    }

    public void outputWriter(String text, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(text);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Cipher{" +
                "key=" + key +
                ", text='" + (text != null ? text.substring(0, Math.min(20, text.length())) + "..." : "null") + '\'' +
                '}';
    }
}
