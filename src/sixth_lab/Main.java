package sixth_lab;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
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
                    System.out.print("Введите ключ через пробел (пример: 2 0 1): ");
                    List<Integer> key = toIntegerList(scanner.nextLine().split("[,./ ]"));
                    String output = cipher(text, key, false);
                    if (output != null) {
                        System.out.printf("Результат шифрования:%s\n", output);
                    }
                }
                case "2" -> {
                    System.out.print("Введите текст: ");
                    String text = scanner.nextLine();
                    System.out.print("Введите ключ через пробел (пример: 2 0 1): ");
                    List<Integer> key = toIntegerList(scanner.nextLine().split("[,./ ]"));
                    String output = cipher(text, key, true);
                    if (output != null) {
                        System.out.printf("Результат расшифровки:%s\n", output);
                    }
                }
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
            }
        }
    }

    public static String cipher(String text, List<Integer> key, boolean isCrypt) {
        StringBuilder result = new StringBuilder();
        if (!isCrypt) {
            Character[][] table = tableGetter(text, key);
            if (table == null) return null;
            for (int i = 0; i < table.length; i++) {
                result.append(Arrays.stream(table[key.get(i)]).map(Object::toString).collect(Collectors.joining()));
            }
        } else {
            if (key == null){
                System.out.println("Ошибка: Недопустимые индексы!");
                return null;
            }
            int blocksCount = text.length() / key.size();
            for (int i = 0; i < blocksCount; i++) {
                for (int j = 0; j < key.size(); j++) {
                    result.append(text.charAt(i + blocksCount * key.indexOf(j)));
                }
            }
        } return !isCrypt ? result.toString() : result.toString().replaceAll("\0", "");
    }

    public static List<Integer> toIntegerList(String[] lst) {
        try {
            return Arrays.stream(lst).map(Integer::parseInt).toList();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Character[][] tableGetter(String text, List<Integer> key) {
        if (text == null || key == null) {
            System.out.println("Ошибка: null параметры!");
            return null;
        }

        if (!isValidKey(key)) {
            System.out.println("Ошибка: Недопустимые индексы!");
            return null;
        }

        int keyLen = key.size();
        StringBuilder textBuilder = new StringBuilder(text);
        while (textBuilder.length() % keyLen != 0) {
            textBuilder.append("\0");
        }
        text = textBuilder.toString();
        int blocksCount = text.length() / keyLen;
        Character[][] result = new Character[keyLen][blocksCount];
        for (int i = 0; i < blocksCount; i++) {
            for (int j = 0; j < keyLen; j++) {
                result[j][i] = text.charAt(i * keyLen + j);
            }
        }
        Arrays.stream(result).forEach(elem -> System.out.println(Arrays.toString(elem)));
        return result;
    }

    public static boolean isValidKey(List<Integer> key) {
        return !key.isEmpty() && key.stream().noneMatch(i -> i >= key.size() || i < 0) && key.stream().distinct().toArray().length == key.size();
    }

    public static void printMenu() {
        System.out.println("""
                1. Зашифровать
                2. Расшифровать
                3. Выйти из программы
                """);
    }
}
