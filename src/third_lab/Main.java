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

        int circuitCount = alphabets.size();
        int[] counters = new int[circuitCount];
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            String s = String.valueOf(ch);

            if (!baseAlphabet.contains(s)) {
                result.append(s);
                continue;
            }
            if (!isCrypt) {
                for (int i = 0; i < circuitCount; i++) {
                    String alphabet = alphabets.get(i)[counters[i]];
                    int idx = baseAlphabet.indexOf(s);
                    s = String.valueOf(alphabet.charAt(idx));
                }
            } else {
                for (int i = circuitCount - 1; i >= 0; i--) {
                    String alphabet = alphabets.get(i)[counters[i]];
                    int idx = alphabet.indexOf(s);
                    s = String.valueOf(baseAlphabet.charAt(idx));
                }
            }

            result.append(s);
            alphabetIndexGetter(counters);
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
        alphabets.put(0, new String[]{"ЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙК", "3456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя012", "абвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"});
        alphabets.put(1, new String[]{"юя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэ", "ГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВ", ".,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789"});
        alphabets.put(2, new String[]{"вгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯаб", "89.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя01234567", "ПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789.,!?;:-()' АБВГДЕЁЖЗИЙКЛМНО"});
    }

    private static void alphabetIndexGetter(int[] counters) {
        counters[0]++;
        if (counters[0] == alphabets.get(0).length) counters[0] = 0;

        if (counters[0] == 0) {
            counters[1]++;
            if (counters[1] == alphabets.get(1).length) counters[1] = 0;
        }

        if (counters[0] == 0 && counters[1] == 0) {
            counters[2]++;
            if (counters[2] == alphabets.get(2).length) counters[2] = 0;
        }
    }
}

