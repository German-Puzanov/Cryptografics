package seventh_lab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class Main {

    private static final Path OUTPUT_PATH = Path.of("src/output/output.txt");
    private static final Path INPUT_PATH = Path.of("src/input/input.txt");

    public static void main(String[] args) throws IOException {
        if (!Files.exists(INPUT_PATH) || !Files.exists(OUTPUT_PATH)) {
            Files.createFile(OUTPUT_PATH);
            Files.createFile(INPUT_PATH);
        }

        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            printMenu();
            command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> {
                    System.out.println("""
                            1. Текстовый ввод
                            2. Ввод битов""");
                    command = scanner.nextLine();
                    switch (command) {
                        case "1" -> {
                            System.out.print("Введите текст: ");
                            String text = scanner.nextLine();
                            System.out.print("Введите ключ: ");
                            String key = scanner.nextLine();
                            String output = cipherFromConsole(text, getByteKey(key));
                            System.out.printf("Результат шифрования:%s\n", output);
                        }
                        case "2" -> {

                        }
                    }

                }
                case "2" -> {
                    System.out.print("Введите путь к файлу: ");
                    String pathIn = scanner.nextLine();
                    System.out.print("Введите путь к файлу результату: ");
                    String pathOut = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    String key = scanner.nextLine();
                    cipherFromFile(pathIn, pathOut, getByteKey(key));
                    System.out.println("Результат готов!");
                }
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
            }
        }

    }

    public static void cipherFromFile(String pathIn, String pathOut, byte[] key) {
        byte[] buffer = new byte[4096];
        pathIn = (pathIn == null || pathIn.isEmpty())
                ? INPUT_PATH.toString()
                : pathIn;

        pathOut = (pathOut == null || pathOut.isEmpty())
                ? OUTPUT_PATH.toString()
                : pathOut;

        try (InputStream in = new FileInputStream(pathIn);
             OutputStream os = new BufferedOutputStream(new FileOutputStream(pathOut))) {
            int b;
            int count = 0;
            while ((b = in.read()) != -1) {
                for (int i = 0; i < b; i++) {
                    buffer[i] = (byte) (buffer[i] ^ key[count % key.length]);
                    count++;
                }
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static String cipherFromConsole(String text, byte[] key) {
        StringBuilder result = new StringBuilder();
        byte[] byteCode = text.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[byteCode.length];
        for (int i = 0; i < byteCode.length; i++) {
            buffer[i % buffer.length] = (byte) (byteCode[i] ^ key[i % key.length]);
        }
        result.append(bytesToString(buffer));
        return result.toString();
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            if (b == 0) break;
            result.append((char) b);
        }
        return result.toString();
    }

    public static byte[] getByteKey(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }


    public static void printMenu() {
        System.out.println("""
                1. Зашифровать/расшифровать сообщение из консоли
                2. Зашифровать/расшифровать сообщение из файла
                3. выход
                """);
    }
}
