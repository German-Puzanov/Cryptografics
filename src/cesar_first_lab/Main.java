package cesar_first_lab;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CipherFirstLab cipher = new CipherFirstLab();
        String command;

        while (true) {
            try {
                showMenu();
                command = scanner.nextLine().trim();
                switch (command) {

                    case "1" -> {
                        System.out.println("""
                                1. Текст из консоли
                                2. Текст из файла input/input.txt""");
                        command = scanner.nextLine().trim();
                        switch (command) {
                            case "1" -> {
                                setCipher(cipher, scanner, true);
                                String output = cipher.cesarCipher(true);

                                resultOutput(cipher, output, scanner);
                            }
                            case "2" -> {
                                setCipher(cipher, scanner, false);
                                String output = cipher.cesarCipher(true);

                                resultOutput(cipher, output, scanner);
                            }
                        }
                    }
                    case "2" -> {
                        System.out.println("""
                                1. Текст из консоли
                                2. Текст из файла input/input.txt""");
                        command = scanner.nextLine().trim();
                        switch (command) {
                            case "1" -> {
                                setCipher(cipher, scanner, true);
                                String output = cipher.cesarCipher(false);

                                resultOutput(cipher, output, scanner);
                            }
                            case "2" -> {
                                setCipher(cipher, scanner, false);
                                String output = cipher.cesarCipher(false);

                                resultOutput(cipher, output, scanner);
                            }
                        }
                    }
                    case "3" -> System.out.println(cipher);
                    case "0" -> {
                        clearFiles(scanner);
                        return;
                    }
                    default -> System.out.println("Invalid command!");
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

    }

    public static void resultOutput(CipherFirstLab cipher, String output, Scanner scanner) {
        System.out.println("""
                1. Вывести текст в консоли
                2. Записать результат в файл src/output/output.txt""");
        String command = scanner.nextLine().trim();
        switch (command) {
            case "1" -> System.out.println(output);
            case "2" -> {
                cipher.outputWriter(output, "src/output/output.txt");
                System.out.println("Результат записан в файл src/output/output.txt!");
            }
        }
    }

    public static void setCipher(CipherFirstLab cipher, Scanner scanner, boolean hasText) {
        if (hasText) {
            System.out.print("Введите текст: ");
            String text = scanner.nextLine();
            System.out.print("Введите шаг сдвига: ");
            int key = Integer.parseInt(scanner.nextLine());
            cipher.setKey(key);
            cipher.setText(text);
        } else {
            System.out.print("Введите шаг сдвига: ");
            int key = Integer.parseInt(scanner.nextLine());
            cipher.setKey(key);
            cipher.setText(Path.of("src/input/input.txt"));
        }
    }

    public static void showMenu() {
        System.out.println("""
                1. Зашифровать текст\s
                2. Расшифровать текст\s
                3. Показать текущие настройки\s
                0. Выход\s""");
    }

    public static void clearFiles(Scanner scanner) throws IOException {
        System.out.print("Очистить файлы? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            try (FileWriter writer = new FileWriter("src/output/output.txt")) {
                writer.write("");
            }
            try (FileWriter writer = new FileWriter("src/input/input.txt")) {
                writer.write("");
            }
        }
    }
}