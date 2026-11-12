package seventh_lab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
            System.out.println("Ошибка инициализации: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1" -> {
                        System.out.println("""
                                Режим ввода:
                                1. Байты
                                2. Символы""");
                        command = scanner.nextLine();
                        switch (command) {
                            case "1" -> {
                                System.out.println("Введите сообщение (байты): ");
                                List<Byte> text = Arrays.stream(scanner.nextLine().split(" ")).map(s -> (byte) Integer.parseInt(s, 2)).toList();
                                System.out.print("Введите гамму (байты): ");
                                List<Byte> gamma = Arrays.stream(scanner.nextLine().split(" ")).map(s -> (byte) Integer.parseInt(s, 2)).toList();

                                gamma = correctGamma(text.size(), gamma);

                                System.out.println("Вход:            " + text.stream().map(Main::byteToCorrectFormat).collect(Collectors.joining(" ")));
                                System.out.println("Гамма:           " + gamma.stream().map(Main::byteToCorrectFormat).collect(Collectors.joining(" ")));

                                String output = processConsole(text, gamma);
                                System.out.printf("Результат(bin): %s\n", output);

                                String[] binStrings = output.split(" ");
                                byte[] resultBytes = new byte[binStrings.length];
                                for (int i = 0; i < binStrings.length; i++) {
                                    resultBytes[i] = (byte) Integer.parseInt(binStrings[i], 2);
                                }
                                System.out.println("Результат:      " + new String(resultBytes, StandardCharsets.UTF_8));
                            }
                            case "2" -> {
                                System.out.println("Введите сообщение: ");
                                byte[] text = scanner.nextLine().getBytes(java.nio.charset.Charset.forName("Windows-1251"));
                                System.out.print("Введите гамму: ");
                                byte[] gamma = scanner.nextLine().getBytes(java.nio.charset.Charset.forName("Windows-1251"));

                                gamma = correctGamma(text.length, gamma);

                                System.out.println("Вход (байты):   " + IntStream.range(0, text.length).mapToObj(i -> byteToCorrectFormat(text[i])).collect(Collectors.joining(" ")));
                                byte[] finalGamma = gamma;
                                System.out.println("Гамма (байты):  " + IntStream.range(0, gamma.length).mapToObj(i -> byteToCorrectFormat(finalGamma[i])).collect(Collectors.joining(" ")));

                                byte[] output = processConsole(text, gamma);
                                System.out.println("Результат(bin): " + IntStream.range(0, output.length).mapToObj(i -> byteToCorrectFormat(output[i])).collect(Collectors.joining(" ")));
                                System.out.println("Результат:      " + new String(output, java.nio.charset.Charset.forName("Windows-1251")));
                            }
                        }
                    }
                    case "2" -> {
                        System.out.print("Введите путь к файлу: ");
                        String pathIn = scanner.nextLine().trim();
                        System.out.print("Введите путь к файлу результату: ");
                        String pathOut = scanner.nextLine().trim();
                        System.out.print("Введите гамму: ");
                        Byte[] gamma = (Byte[]) Arrays.stream(scanner.nextLine().split(" ")).map(String::getBytes).toArray();
                        processFile(pathIn, pathOut, gamma);
                    }
                    case "3" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверная команда.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void processFile(String pathIn, String pathOut, Byte[] key) {
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
            byte[] fileBytes = Files.readAllBytes(inPath);
            for (int i = 0; i < fileBytes.length; i++) {
                fileBytes[i] = (byte) (fileBytes[i] ^ key[i]);
            }
            Files.write(outPath, fileBytes);
            System.out.println("Результат готов!");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Ошибка обработки файла: " + e.getMessage());
        }
    }

    public static byte[] processConsole(byte[] text, byte[] gamma) {
        byte[] result = new byte[text.length];
        for (int i = 0; i < text.length; i++) {
            result[i] = (byte) (text[i] ^ gamma[i]);
        }
        return result;
    }

    public static String processConsole(List<Byte> text, List<Byte> gamma) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.size(); i++) {
            result.append(byteToCorrectFormat((byte) (text.get(i) ^ gamma.get(i % gamma.size())))).append(" ");
        }
        return result.toString().trim();
    }

    public static String byteToCorrectFormat(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    public static byte[] correctGamma(int len, byte[] gamma) {
        int gammaSize = gamma.length;
        byte[] newGamma = new byte[len];
        for (int i = 0; i < len; i++) {
            newGamma[i] = gamma[i % gammaSize];
        }
        return newGamma;
    }

    public static List<Byte> correctGamma(int len, List<Byte> gamma){
        int gammaSize = gamma.size();
        List<Byte> newGamma = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            newGamma.add(gamma.get(i % gammaSize));
        }
        return newGamma;

    }
    public static void printMenu() {
        System.out.println("\n1. Работа с консолью\n2. Работа с файлом\n3. Выход");
        System.out.print("Выберите действие: ");
    }
}