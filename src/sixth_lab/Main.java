package sixth_lab;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int BLOCK_CONST = 2;
    private static final String PADDING_CHAR = "`";

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
        if (key == null || notValidKey(key)) {
            System.out.println("Ошибка: Недопустимые индексы!");
            return null;
        }

        if (text.length() % (key.size() * BLOCK_CONST) != 0 && isCrypt) {
            System.out.println("Ошибка: длина шифротекста должна быть кратна " + key.size() * BLOCK_CONST);
            return null;
        }

        Character[][] blocks = blocksGetter(text, key);
        Character[][] block;

        for (Character[] characters : blocks) {
            if (!isCrypt) {
                block = blockToTable(key, characters);
                for (Integer colIndex : key) {
                    for (int row = 0; row < BLOCK_CONST; row++) {
                        result.append(block[row][colIndex]);
                    }
                }
            } else {
                result.append(decryptBlock(key, characters));
            }
        }

        return !isCrypt ? result.toString() : result.toString().replaceAll(PADDING_CHAR, "");
    }

    public static List<Integer> toIntegerList(String[] lst) {
        try {
            return Arrays.stream(lst).map(Integer::parseInt).toList();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Character[][] blocksGetter(String text, List<Integer> key) {

        int keyLen = key.size();
        int blockSize = keyLen * BLOCK_CONST;
        StringBuilder textBuilder = new StringBuilder(text);

        while (textBuilder.length() % (blockSize) != 0) {
            textBuilder.append(PADDING_CHAR);
        }
        text = textBuilder.toString();
        int blocksCount = text.length() / (blockSize);
        Character[][] result = new Character[blocksCount][blockSize];
        for (int i = 0; i < blocksCount; i++) {
            for (int j = 0; j < blockSize; j++) {
                result[i][j] = text.charAt(i * blockSize + j);
            }
        }
        return result;
    }

    public static boolean notValidKey(List<Integer> key) {
        return key == null || key.isEmpty() || key.stream().anyMatch(i -> i >= key.size() || i < 0) || key.stream().distinct().count() != key.size();
    }

    public static Character[][] blockToTable(List<Integer> key, Character[] block) {
        Character[][] result = new Character[BLOCK_CONST][key.size()];
        int count = 0;
        for (int i = 0; i < BLOCK_CONST; i++) {
            for (int j = 0; j < key.size(); j++) {
                result[i][j] = block[count];
                count++;
            }
        }
        return result;
    }

    public static String decryptBlock(List<Integer> key, Character[] block) {
        int keyLen = key.size();
        Character[][] table = new Character[BLOCK_CONST][keyLen];
        int pos = 0;
        for (int colIndex : key) {
            for (int row = 0; row < BLOCK_CONST; row++) {
                table[row][colIndex] = block[pos];
                pos++;
            }
        }
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < BLOCK_CONST; row++) {
            for (int col = 0; col < keyLen; col++) {
                result.append(table[row][col]);
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
