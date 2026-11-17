package seventh_lab;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

   public static String WIN1251_CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ЂЃ‚ѓ„…†‡€‰Љ‹ЊЌЋЏђ‘’“”•–—™љ›њќћџ\u00A0ЎўЈ¤Ґ¦§Ё©Є«¬\u00AD®Ї°±Ііґµ¶·ё№є»јЅѕїАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ШИФРОВАНИЕ ГАММИРОВАНИЕМ");
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1" -> {

                        System.out.print("Введите сообщение (байты): ");
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
                        System.out.println("Результат:      " + new String(resultBytes, java.nio.charset.Charset.forName("Windows-1251")));
                    }
                    case "2" -> {
                        System.out.print("Введите сообщение: ");
                        String message = scanner.nextLine();
                        if (Arrays.stream(message.split("")).anyMatch(s -> !WIN1251_CHARS.contains(s) )){
                            System.out.println("СООБЩЕНИЕ СОДЕРЖИТ СИМВОЛЫ КОТОРЫХ НЕТ В WINDOWS-1251!");
                            continue;
                        }
                        byte[] text = message.getBytes(java.nio.charset.Charset.forName("Windows-1251"));
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
                    case "3" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверная команда.");
                }
            } catch (
                    Exception e) {
                System.out.println(e.getMessage());
            }
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
            result.append(byteToCorrectFormat((byte) (text.get(i) ^ gamma.get(i)))).append(" ");
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

    public static List<Byte> correctGamma(int len, List<Byte> gamma) {
        int gammaSize = gamma.size();
        List<Byte> newGamma = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            newGamma.add(gamma.get(i % gammaSize));
        }
        return newGamma;
    }

    public static void printMenu() {
        System.out.println("1. Бинарный ввод \n2. Символьный ввод\n3. Выход");
        System.out.print("Выберите действие: ");
    }
}