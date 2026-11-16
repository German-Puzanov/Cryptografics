package kursach;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (users.stream().anyMatch(u -> u.getName().equals(user.getName()))) {
            throw new IllegalArgumentException("User with such name had already exist!");
        } else {
            users.add(user);
            System.out.println("User " + user.getName() + "successfully added!");
        }
    }

    public void removeUser(User user) {
        if (!users.contains(user)) throw new IllegalArgumentException("No such user!");

        users.remove(user);
        System.out.println("User " + user.getName() + "successfully removed!");
    }


    public void showUsers() {
        users.forEach(System.out::println);
    }

    public void authorisation(String name, String password) {
        User current = users.stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
        if (current == null) throw new IllegalArgumentException("No such user!");
        if (!password.equals(current.getPassword())) throw new IllegalArgumentException("Wrong password!");
        current.setAuthorised(true);
        System.out.println("Вы вошли в профиль:" + current.getName());
    }

    public void printMenu(){
        System.out.println("""
                1. Зарегистрировать пользователя
                2. Удалить пользователя
                3. Авторизоваться
                4. Показать пользователей""");
    }

    public User createUser(Scanner scanner){
        System.out.print("Введите имя: ");
        String name = scanner.nextLine().trim();
        var keys = Main.keyGenerator();
        List<Long> openKeys = keys.get(Main.OPEN_KEY_NAME);
        List<Long> secretKeys = keys.get(Main.SECRET_KEY_NAME);
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();
        return new User(name, openKeys, secretKeys, password);
    }


}
