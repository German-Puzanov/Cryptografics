package second_lab;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<String> alphabets =
            List.of("АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_",
                    "ЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_АБВГДЕЖ",
                    "ПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_АБВГДЕЖЗИЙКЛМНО");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            printMenu();
            command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> {
                    String[] params = getParams(scanner);
                    try {
                        System.out.printf("Зашифрованный текст: %s\n", cipher(params[1], Integer.parseInt(params[0]), false));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid params!");
                    }
                }
                case "2" -> {
                    String[] params = getParams(scanner);
                    try {
                        System.out.printf("Расшифрованный текст: %s\n", cipher(params[1], Integer.parseInt(params[0]), true));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid params!");
                    }
                }
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
            }
        }
    }

    public static String cipher(String text, int key, boolean isCrypt) {
        if (text == null) return null;
        key = isCrypt ? -1 * key : key;
        String[] splitText = text.split("");
        StringBuilder result = new StringBuilder();
        int index;
        int newIndex;
        for (int i = 0; i < splitText.length; i++) {
            String currentAlphabet = alphabets.get(i % alphabets.size());
            index = currentAlphabet.indexOf(splitText[i]);
            if (index == -1) {
                result.append(splitText[i]);
                continue;
            }
            newIndex = (index + key) % currentAlphabet.length();
            if (newIndex < 0) newIndex += currentAlphabet.length();
            result.append(currentAlphabet.charAt(newIndex));
        }
        return result.toString();
    }

    public static String[] getParams(Scanner scanner) {
        System.out.print("Введите ключ: ");
        String key = scanner.nextLine().trim();
        System.out.print("Введите текст: ");
        String text = scanner.nextLine();
        return new String[]{key, text};
    }

    public static void printMenu() {
        System.out.println("""
                1. Зашифровать
                2. Расшифровать
                3. Выйти из программы
                """);
    }
}
