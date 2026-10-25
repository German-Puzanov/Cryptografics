package seventh_lab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Scanner;

public class Main {

    private static final Path OUTPUT_PATH = Path.of("src/output/output.txt");
    private static final Path INPUT_PATH = Path.of("src/input/input.txt");

    public static void main(String[] args) {
        try {
            Files.createDirectories(INPUT_PATH.getParent());
            Files.createDirectories(OUTPUT_PATH.getParent());
            if (!Files.exists(INPUT_PATH)) Files.createFile(INPUT_PATH);
            if (!Files.exists(OUTPUT_PATH)) Files.createFile(OUTPUT_PATH);
        } catch (IOException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> {
                    System.out.println("1. Шифровать\n2. Расшифровать");
                    String mode = scanner.nextLine().trim();
                    boolean isEncrypt = mode.equals("1");

                    System.out.print("Введите текст: ");
                    String text = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    String key = scanner.nextLine();

                    String output = processConsole(text, getByteKey(key), isEncrypt);
                    System.out.printf("Результат: %s\n", output);
                }
                case "2" -> {
                    System.out.println("1. Шифровать\n2. Расшифровать");
                    String mode = scanner.nextLine().trim();
                    boolean isEncrypt = mode.equals("1");

                    System.out.print("Введите путь к файлу: ");
                    String pathIn = scanner.nextLine().trim();
                    System.out.print("Введите путь к файлу результату: ");
                    String pathOut = scanner.nextLine().trim();
                    System.out.print("Введите ключ: ");
                    String key = scanner.nextLine();

                    processFile(pathIn, pathOut, getByteKey(key), isEncrypt);
                }
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверная команда.");
            }
        }
    }

    public static void processFile(String pathIn, String pathOut, byte[] key, boolean isEncrypt) {
        Path inPath = (pathIn == null || pathIn.isEmpty()) ? INPUT_PATH : Path.of(pathIn);
        Path outPath = (pathOut == null || pathOut.isEmpty()) ? OUTPUT_PATH : Path.of(pathOut);

        if (inPath.toAbsolutePath().equals(outPath.toAbsolutePath())) {
            System.out.println("Ошибка: Входной и выходной файлы не могут быть одинаковыми!");
            return;
        }

        if (!Files.exists(inPath)) {
            System.out.println("Ошибка: Входной файл не найден: " + inPath.toAbsolutePath());
            return;
        }

        try {
            if (isEncrypt) {
                byte[] fileBytes = Files.readAllBytes(inPath);
                for (int i = 0; i < fileBytes.length; i++) {
                    fileBytes[i] = (byte) (fileBytes[i] ^ key[i % key.length]);
                }
                String base64Result = Base64.getEncoder().encodeToString(fileBytes);
                Files.writeString(outPath, base64Result, StandardCharsets.UTF_8);
            } else {
                String base64Content = Files.readString(inPath, StandardCharsets.UTF_8).trim();
                byte[] fileBytes = Base64.getDecoder().decode(base64Content);
                for (int i = 0; i < fileBytes.length; i++) {
                    fileBytes[i] = (byte) (fileBytes[i] ^ key[i % key.length]);
                }
                Files.write(outPath, fileBytes);
            }
            System.out.println("Результат готов!");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Ошибка обработки файла: " + e.getMessage());
        }
    }

    public static String processConsole(String text, byte[] key, boolean isEncrypt) {
        byte[] data;
        if (!isEncrypt) {
            try {
                data = Base64.getDecoder().decode(text.trim());
            } catch (IllegalArgumentException e) {
                return "Ошибка: Неверный формат Base64";
            }
        } else {
            data = text.getBytes(StandardCharsets.UTF_8);
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ key[i % key.length]);
        }

        return isEncrypt ? Base64.getEncoder().encodeToString(data) : new String(data, StandardCharsets.UTF_8);
    }

    public static byte[] getByteKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return new byte[]{0};
        }
        return key.trim().getBytes(StandardCharsets.UTF_8);
    }

    public static void printMenu() {
        System.out.println("\n1. Работа с консолью\n2. Работа с файлом\n3. Выход");
        System.out.print("Выберите действие: ");
    }
}