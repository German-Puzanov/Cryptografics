package second_lab;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String baseAlphabet = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_";

    private static final List<String> alphabets =
            List.of(
                    "ЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_АБВГДЕЖ",
                    "ПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_АБВГДЕЖЗИЙКЛМНО",
                    "ЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_АБВГДЕЖЗИЙКЛМНОПРСТУФХЦ"
            );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            printMenu();
            command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> {
                    String text = getText(scanner);
                    try {
                        System.out.printf("Зашифрованный текст: %s\n", cipher(text, false));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid params!");
                    }
                }
                case "2" -> {
                    String text = getText(scanner);
                    try {
                        System.out.printf("Расшифрованный текст: %s\n", cipher(text, true));
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

    public static String cipher(String text, boolean isCrypt) {
        if (text == null) return null;
        String[] splitText = text.split("");
        StringBuilder result = new StringBuilder();
        int index;
        if (!isCrypt) {
            for (int i = 0; i < splitText.length; i++) {
                String currentAlphabet = alphabets.get(i % alphabets.size());
                index = baseAlphabet.indexOf(splitText[i]);
                if (index == -1) {
                    result.append(splitText[i]);
                    continue;
                }
                result.append(currentAlphabet.charAt(index));
            }
        } else {
            for (int i = 0; i < splitText.length; i++) {
                String currentAlphabet = alphabets.get(i % alphabets.size());
                index = currentAlphabet.indexOf(splitText[i]);
                if (index == -1) {
                    result.append(splitText[i]);
                    continue;
                }
                result.append(baseAlphabet.charAt(index));
            }
        }
        return result.toString();

    }

    public static String getText(Scanner scanner) {
        System.out.print("Введите текст: ");
        return scanner.nextLine();
    }

    public static void printMenu() {
        System.out.println("""
                1. Зашифровать
                2. Расшифровать
                3. Выйти из программы
                """);
    }
}
