package third_lab;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String baseAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' ";

    private static final Map<Integer, String[]> alphabets = new HashMap<>();


    public static void main(String[] args) {
        updateMap();
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
                String alphabet = getAlphabet(i);
                index = baseAlphabet.indexOf(splitText[i]);
                updateResult(splitText[i], index, result, alphabet);
            }
        } else {
            for (int i = 0; i < splitText.length; i++) {
                String alphabet = getAlphabet(i);
                index = alphabet.indexOf(splitText[i]);
                updateResult(splitText[i], index, result, baseAlphabet);
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


    public static void updateMap() {
        alphabets.put(0, new String[]{
                "ЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙК",
                "3456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя012",
                "абвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        });

        alphabets.put(1, new String[]{
                "юя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэ",
                "ГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВ",
                ".,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789"
        });

        alphabets.put(2, new String[]{
                "вгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯаб",
                "89.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя01234567",
                "ПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНО"
        });
    }

    private static String getAlphabet(int index) {
        int circuit = index % alphabets.size();
        int currentAlphabetIndex = index % alphabets.get(circuit).length;
        return alphabets.get(circuit)[currentAlphabetIndex];
    }

    private static void updateResult(String word, int index, StringBuilder result, String alphabet){
        if (index == -1) {
            result.append(word);
            return;
        }
        result.append(alphabet.charAt(index));
    }
}
