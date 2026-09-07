import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CipherFirstLab {
    private Integer key = null;
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
        if (key <= 0) throw new IllegalArgumentException("Invalid key");
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
                char base = Character.isUpperCase(chr) ? 'A' : 'a';
                char newChar = (char) (base + (chr - base + key) % 26);
                sb.append(newChar);
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
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
