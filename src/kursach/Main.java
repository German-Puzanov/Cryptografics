package kursach;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static final Path SAVES = Path.of("src/kursach/saves.dat");

    public static void main(String[] args) {
        UserManager manager = new UserManager();
        Scanner scanner = new Scanner(System.in);
        String command;

        try {
            manager.loadUsers(SAVES);
        } catch (Exception e) {
            System.out.println("Данные не загружены!");
        }


        while (true) {
            manager.printMenu();
            command = scanner.nextLine();
            try {
                switch (command) {
                    case "1" -> manager.addUser(manager.createUser(scanner));
                    case "2" -> {
                        System.out.print("Введите имя пользователя: ");
                        String name = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        manager.removeUser(manager.getUserByName(name), password);
                    }
                    case "3" -> {
                        System.out.print("Введите имя пользователя: ");
                        String name = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();

                        manager.authorisation(name, password);
                    }
                    case "4" -> {
                        System.out.print("Введите путь к документу: ");
                        Path path = Path.of(scanner.nextLine().trim());

                        if (!Files.exists(path)) throw new IllegalArgumentException("Invalid path!");

                        System.out.print("Введите имя подписывающего человека: ");
                        String name = scanner.nextLine().trim();

                        User user = manager.getUserByName(name);
                        if (user == null) throw new IllegalArgumentException("No such user!");
                        if (user.notAuthorised())
                            throw new IllegalArgumentException("You have no secret key of this user, try authorise at first!");

                        BigInteger hash = Cipher.hashAlgorithmFNV(path);
                        BigInteger signature = Cipher.cipher(hash, user.getSecretKey().get(0), user.getSecretKey().get(1));

                        System.out.println("Хэш документа (FNV-256): " + hash.toString(16));
                        System.out.println("Подпись пользователя " + user.getName() + ": " + signature.toString(16));
                    }
                    case "5" -> {
                        System.out.print("Введите путь к проверяемому документу: ");
                        Path path = Path.of(scanner.nextLine().trim());

                        if (!Files.exists(path)) throw new IllegalArgumentException("Invalid path!");

                        System.out.print("Введите имя человека, подписавшего документ: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Введите подпись (в HEX-формате): ");
                        String signatureHex = scanner.nextLine().trim();

                        BigInteger signature;
                        try {
                            signature = new BigInteger(signatureHex, 16);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Ошибка: подпись должна быть в HEX-формате");
                        }

                        User user = manager.getUserByName(name);
                        if (user == null) throw new IllegalArgumentException("No such user!");

                        BigInteger hash = Cipher.hashAlgorithmFNV(path);
                        BigInteger checkHash = Cipher.cipher(signature, user.getOpenKey().get(0), user.getOpenKey().get(1));
                        boolean valid = (hash.equals(checkHash));

                        System.out.println("Хэш документа: " + hash.toString(16));
                        System.out.println("Расшифрованная подпись: " + checkHash.toString(16));
                        System.out.println("Подпись пользователя " + user.getName() + ": "
                                + (valid ? "Действительна!" : "Недействительна!")
                                + " (" + checkHash.toString(16) + (valid ? " == " : " != ") + hash.toString(16) + ")");
                    }
                    case "6" -> {
                        System.out.print("Введите сообщение: ");
                        String message = scanner.nextLine();

                        System.out.print("Введите имя подписывающего: ");
                        String name = scanner.nextLine().trim();
                        User user = manager.getUserByName(name);
                        if (user == null) throw new IllegalArgumentException("No such user!");
                        if (user.notAuthorised())
                            throw new IllegalArgumentException("You have no secret key of this user, try authorise at first!");

                        BigInteger hash = Cipher.hashAlgorithmFNV(message);
                        BigInteger signature = Cipher.cipher(hash, user.getSecretKey().get(0), user.getSecretKey().get(1));

                        System.out.println("Хэш документа (FNV-256): " + hash.toString(16));
                        System.out.println("Подпись пользователя " + user.getName() + ": " + signature.toString(16));
                    }
                    case "7" -> {
                        System.out.print("Введите сообщение: ");
                        String message = scanner.nextLine();

                        System.out.print("Введите имя человека, подписавшего документ: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Введите подпись (в HEX-формате): ");
                        String signatureHex = scanner.nextLine().trim();

                        BigInteger signature;
                        try {
                            signature = new BigInteger(signatureHex, 16);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Ошибка: подпись должна быть в HEX-формате");
                        }

                        User user = manager.getUserByName(name);
                        if (user == null) throw new IllegalArgumentException("No such user!");

                        BigInteger hash = Cipher.hashAlgorithmFNV(message);
                        BigInteger checkHash = Cipher.cipher(signature, user.getOpenKey().get(0), user.getOpenKey().get(1));
                        boolean valid = (hash.equals(checkHash));

                        System.out.println("Хэш сообщения: " + hash.toString(16));
                        System.out.println("Расшифрованная подпись: " + checkHash.toString(16));
                        System.out.println("Подпись пользователя " + user.getName() + ": "
                                + (valid ? "Действительна!" : "Недействительна!")
                                + " (" + checkHash.toString(16) + (valid ? " == " : " != ") + hash.toString(16) + ")");
                    }
                    case "8" -> manager.showUsers();
                    case "0" -> {
                        manager.saveUsers(SAVES);
                        System.out.println("До новых встреч!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid command!");
                }
            } catch (IllegalArgumentException | IOException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

        }
    }
}