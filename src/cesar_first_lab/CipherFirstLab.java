package cesar_first_lab;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CipherFirstLab {
    private int key = 0;
    private String text = null;
    private static final String RU_ALPHABET_UPPER = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final String RU_ALPHABET_LOWER = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final String DIGITS = "0123456789";

    private static final String EN_ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String EN_ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";

    public CipherFirstLab() {
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
            System.out.println("IOException!");;
        }
    }

    public String cesarCipher(boolean encode) {
        int key = encode ? this.key : this.key * -1;
        char[] charArray = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (char chr : charArray) {
            String alphabet = getAlphabet(chr);

            if (alphabet != null) {
                int index = alphabet.indexOf(chr);
                int alphabetLength = alphabet.length();
                int newIndex = (index + key) % alphabetLength;
                if (newIndex < 0) newIndex += alphabetLength;

                sb.append(alphabet.charAt(newIndex));
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }


    private String getAlphabet(char chr) {
        if (RU_ALPHABET_UPPER.indexOf(chr) != -1) return RU_ALPHABET_UPPER;
        else if (RU_ALPHABET_LOWER.indexOf(chr) != -1) return RU_ALPHABET_LOWER;
        else if (EN_ALPHABET_LOWER.indexOf(chr) != -1) return EN_ALPHABET_LOWER;
        else if (EN_ALPHABET_UPPER.indexOf(chr) != -1) return EN_ALPHABET_UPPER;
        else return null;
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
