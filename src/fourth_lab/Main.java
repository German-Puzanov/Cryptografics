package fourth_lab;
import java.util.Scanner;

public class Main {
    private static final String baseAlphabet = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя0123456789 .,!?_";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String command;
        while (true) {
            printMenu();
            command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> {
                    System.out.print("Введите текст: ");
                    String text = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    String key = scanner.nextLine();
                    try {
                        System.out.printf("Зашифрованный текст: %s\n", cipher(text,key, false));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid params!");
                    }
                }
                case "2" -> {
                    System.out.print("Введите текст: ");
                    String text = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    String key = scanner.nextLine();
                    try {
                        System.out.printf("Расшифрованный текст: %s\n", cipher(text,key, true));
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

    public static String cipher(String text, String key, boolean isCrypt) {
        if (text == null || key == null) return null;
        StringBuilder result = new StringBuilder();
        int newIndex;
        int keyIndex;
        char[] charArr = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            char symbol = charArr[i];
            keyIndex = baseAlphabet.indexOf(key.charAt(i % key.length()));
            if (baseAlphabet.indexOf(symbol) == -1 || keyIndex == -1) {
                result.append(symbol);
                continue;
            }
            if (!isCrypt) {
                newIndex = baseAlphabet.indexOf(symbol) + keyIndex;
                if (newIndex >= baseAlphabet.length()) newIndex -= baseAlphabet.length();
                result.append(baseAlphabet.charAt(newIndex));
            } else {
                newIndex = baseAlphabet.indexOf(symbol) - keyIndex;
                if (newIndex < 0) newIndex += baseAlphabet.length();
                result.append(baseAlphabet.charAt(newIndex));
            }
        }
        return result.toString();

    }

    public static void printMenu() {
        System.out.println("""
                1. Зашифровать
                2. Расшифровать
                3. Выйти из программы
                """);
    }
}
