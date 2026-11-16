package kursach;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (users.stream().anyMatch(u -> u.getName().equals(user.getName()))) {
            throw new IllegalArgumentException("User with such name had already exist!");
        } else {
            users.add(user);
            System.out.println("User " + user.getName() + " successfully added!");
        }
    }

    public void removeUser(User user, String password) {
        if (user == null || !users.contains(user)) throw new IllegalArgumentException("No such user!");
        if (!password.equals(user.getPassword())) throw new IllegalArgumentException("Wrong password!");
        users.remove(user);
        System.out.println("User " + user.getName() + " successfully removed!");
    }

    public User getUserByName(String name) {
        return users.stream().filter(u -> name.equals(u.getName())).findFirst().orElse(null);
    }


    public void showUsers() {
        users.forEach(u -> System.out.println(u.toString()));
    }

    public void authorisation(String name, String password) {
        User current = users.stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
        if (current == null) throw new IllegalArgumentException("No such user!");
        if (!password.equals(current.getPassword())) throw new IllegalArgumentException("Wrong password!");
        users.forEach(u -> u.setAuthorised(false));
        current.setAuthorised(true);
        System.out.println("Вы вошли в профиль:" + current.getName());
    }

    public void printMenu() {
        System.out.println("""
                
                1. Зарегистрировать пользователя
                2. Удалить пользователя
                3. Авторизоваться
                4. Подписать документ
                5. Проверить подлинность документа
                6. Подписать сообщение
                7. Проверить подпись сообщения
                8. Показать пользователей
                0. Выход из программы
                """);
    }

    public User createUser(Scanner scanner) {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine().trim();
        var keys = KeyGenerator.generateKey();
        List<BigInteger> openKeys = keys.get(KeyGenerator.OPEN_KEY_NAME);
        List<BigInteger> secretKeys = keys.get(KeyGenerator.SECRET_KEY_NAME);
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();
        return new User(name, openKeys, secretKeys, password);
    }

    public void saveUsers(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(users);
            System.out.println("Данные сохранены!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void loadUsers(Path path) throws IOException {

        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return;
        }
        if (Files.size(path) == 0) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
