package cesar_first_lab;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CipherFirstLab {
    private int key = 0;
    private String text = null;

    public CipherFirstLab() {
    }

    public CipherFirstLab(int key, String text) {
        setKey(key);
        setText(text);
    }

    public CipherFirstLab(int key, Path path) throws Exception {
        setKey(key);
        setText(path);
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Invalid text!");
        this.text = text;
    }


    public void setText(Path path) throws Exception {
        try (FileReader reader = new FileReader(path.toFile())) {
            int chr;
            StringBuilder sb = new StringBuilder();
            while ((chr = reader.read()) != -1) {
                sb.append((char) chr);
            }
            if (sb.isEmpty()) throw new IllegalArgumentException("Invalid text!");
            text = sb.toString();

        } catch (IOException e) {
            throw new Exception("IOException!");
        }
    }

    public String cesarCipher(boolean encode) {
        int key = encode ? this.key : this.key * -1;
        char[] charArray = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char chr : charArray) {
            if (Character.isAlphabetic(chr)) {
                int pos = getPos(chr, key);
                char newChar = (char) pos;
                sb.append(newChar);
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    private static int getPos(char chr, int key) {
        char base;
        int alphabetLength;
        if (chr >= 'А' && chr <= 'Я') {
            base = 'А';
            alphabetLength = 32;
        } else if (chr >= 'а' && chr <= 'я') {
            base = 'а';
            alphabetLength = 32;
        } else if (chr >= 'A' && chr <= 'Z') {
            base = 'A';
            alphabetLength = 26;
        } else {
            base = 'a';
            alphabetLength = 26;
        }

        int pos = base + (chr - base + key) % alphabetLength;
        if (pos < base) pos += alphabetLength;
        return pos;
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
                ", text='" + text + '\'' +
                '}';
    }
}
