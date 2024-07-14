package controller;

import Model.DataSaver;
import Model.Result;
import Model.User.User;

import Enum.LoginRegex;

import java.security.SecureRandom;
import java.util.Random;

public class RegisterController {
    private static String username;
    private static String password;
    private static String nickname;
    private static String email;

    public String generateStrongPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+";

        String allCharacters = uppercaseLetters + lowercaseLetters + numbers + specialCharacters;

        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        password.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        password.append(lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 0; i < 4; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        StringBuilder shuffledPassword = new StringBuilder();
        while (!password.isEmpty()) {
            int index = random.nextInt(password.length());
            shuffledPassword.append(password.charAt(index));
            password.deleteCharAt(index);
        }

        return shuffledPassword.toString();
    }

    public Result register(String username, String password, String nickname, String email) {
        if (User.doesUserExist(username)) return new Result("that username taken try:", random(username), false);
        if (!LoginRegex.VALID_USERNAME.matches(username)) return new Result("username invalid", false);
        if (!LoginRegex.VALID_PASSWORD.matches(password)) return new Result("password invalid", false);
        if (!LoginRegex.STRONG_PASSWORD.matches(password)) return new Result("password weak", false);
        if (!LoginRegex.VALID_EMAIL.matches(email)) return new Result("email invalid", false);
        new User(username, password, nickname, email, true);
        DataSaver.saveUsers(User.getAllUsers());
        return new Result("registered", true);
    }

    public Result confirmRegister() {
        // baraye terminal bood
        return new Result("successful", true);
    }

    public Result passwordConfirmCheck(String pass, String confirm) {
        if (pass.equals(confirm))
            return new Result("password Checked", true);
        return new Result("wrong password Confirm", false);
    }

    public Result register(String username, String nickname, String email) {

        return null;
    }

    public static String random(String input) {
        StringBuilder result = new StringBuilder(input);

        Random random = new Random();
        int length = random.nextInt(5) + 1; // Generate a random length for additional characters

        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3); // Randomly choose between adding numbers or a dash
            if (choice == 0) {
                result.append(random.nextInt(10)); // Add a random number
            } else {
                result.append('-'); // Add a dash
            }
        }

        return result.toString();
    }

    public Result questionShow() {
        return null;
    }

    public void saveAnswer(String answer, String username) {
        User.getUserByUsername(username).getPasswordAnswers().add(answer);
        DataSaver.saveUsers(User.getAllUsers());
    }

    public void saveQuestion(String question, String username) {
        User.getUserByUsername(username).getPasswordQuestions().add(question);
        DataSaver.saveUsers(User.getAllUsers());
    }
}
