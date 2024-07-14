package controller;

import Model.InGameObjects.Cards.CardFactory;
import Model.Result;
import Model.User.User;
import view.AppView;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class LoginController {
    private String onLoggingUsername;

    public Result login(String username, String password) {
        if (User.doesUserExist(username)) {
            if (User.getUserByUsername(username).getPassword().equals(password)) {
                AppView.setLoggedInUser(User.getUserByUsername(username));

                return new Result("Logged in!", "Welcome back dear " + User.getUserByUsername(username).getNickname() + "!", true);
            } else if (!password.equals("")) {
                return new Result("Wrong Password!", "Entered password is wrong.", false);
            } else {
                return new Result("Empty password!", "You haven't field the password box yet.", false);
            }
        } else if (username.equals("")) {
            return new Result("Empty Username!", "You haven't field the username box yet.", false);
        } else {
            return new Result("Wrong Username!", "Username \"" + username + "\" does not exist.", false);

        }
    }
    public Result getQuestion(int num, String username) {
        if (User.getUserByUsername(username) == null)
            return new Result("no such user", false);
        if (num < 0 || num > Objects.requireNonNull(User.getUserByUsername(username)).getPasswordQuestions().size() - 1) {
            return new Result("invalid bound", false);
        }
        return new Result(User.getUserByUsername(username).getPasswordQuestions().get(num), true);
    }
    public Result forgotPassword(String username) {
        if (User.doesUserExist(username)) {
            onLoggingUsername = username;
            return new Result("User found. Enter a security question", true);
        } else {
            return new Result("username not found", false);
        }
    }
    public Result checkQuestionSecurity(String questionNumber, String answer, String onLoggingUsername) {
        User user = User.getUserByUsername(onLoggingUsername);
        if (user == null) {
            return new Result("Wrong answer", false);
        }
        if (!Pattern.compile("\\d+").matcher(questionNumber.trim()).matches()) {
            return new Result("Wrong number format", false);
        }
        int num = Integer.parseInt(questionNumber.trim());
        if (num < 0 || num > Objects.requireNonNull(User.getUserByUsername(onLoggingUsername)).getPasswordQuestions().size() - 1) {
            return new Result("invalid bound", false);
        }
        assert user != null;
        if (user.getPasswordAnswers().get(Integer.parseInt(questionNumber.trim())).equals(answer.trim())) {
            return new Result("Correct answer", true);
        } else {
            return new Result("Wrong answer", false);
        }
    }
    public Result checkQuestionSecurity(String questionNumber, String answer) {
        int number = Integer.parseInt(questionNumber);
        User user = User.getUserByUsername(onLoggingUsername);

        if (user == null) {
            return new Result("Wrong answer", false);
        }
        if (!Pattern.compile("\\d+").matcher(questionNumber.trim()).matches()) {
            return new Result("Wrong number format", false);
        }
        int num = Integer.parseInt(questionNumber.trim());
        if (num < 0 || num > Objects.requireNonNull(User.getUserByUsername(onLoggingUsername)).getPasswordQuestions().size() - 1) {
            return new Result("invalid bound", false);
        }
        assert user != null;
        if (user.returnQuestion(number).equals(answer)) {
            return new Result("Correct answer", true);
        } else {
            return new Result("Wrong answer", false);
        }
    }

    public Result checkQuestionSecurity(int number, String answer) {
        User user = User.getUserByUsername(onLoggingUsername);
        if (user.returnQuestion(number).equals(answer)) {
            return new Result("Correct answer", true);
        } else {
            return new Result("Wrong answer", false);
        }
    }

    public String setPassword(String password) {
        User user = User.getUserByUsername(onLoggingUsername);
        assert user != null;
        user.setPassword(password);
        return "password changed successfully";
    }

    public String setPassword() {
        User user = User.getUserByUsername(onLoggingUsername);
        assert user != null;
        return "password changed successfully";
    }

    public void instantiateAllCards() {
        CardFactory.createAllCards();
    }
}
