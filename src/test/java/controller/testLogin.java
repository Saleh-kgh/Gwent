package controller;

import Model.Result;
import Model.User.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class testLogin {
    private String username, password, nickname, email;
    private LoginController loginController;
    private User user;

    @Before
    public void init() {
        loginController = new LoginController();
        username = "user";
        password = "abAB12$$";
        nickname = "test";
        email = "tt@t.com";
        user = new User(username, password, nickname, email, true);
        ArrayList<String> q = new ArrayList<>();
        q.add("q");
        ArrayList<String> a = new ArrayList<>();
        a.add("a");
        user.setPasswordQuestions(q);
        user.setPasswordAnswers(a);
    }

    @Test
    public void testLoginEmptyUsername() {
        username = "";
        Result result = loginController.login(username, password);
        assert !result.isSuccessful();
        assert result.getTitle().equals("Empty Username!");
    }

    @Test
    public void testLoginNoSuchUser() {
        username = "f";
        Result result = loginController.login(username, password);
        assert !result.isSuccessful();
        assert result.getTitle().equals("Wrong Username!");
    }

    @Test
    public void testLoginEmptyPass() {
        password = "";
        Result result = loginController.login(username, password);
        assert !result.isSuccessful();
        assert result.getTitle().equals("Empty password!");
    }

    @Test
    public void testLoginWrongPass() {
        password = "a";
        Result result = loginController.login(username, password);
        assert !result.isSuccessful();
        assert result.getTitle().equals("Wrong Password!");
    }

    @Test
    public void testLoginSuccess() {
        Result result = loginController.login(username, password);
        assert result.isSuccessful();
    }

    @Test
    public void testLoginSecQOutOfBound() {
        Result result = loginController.getQuestion(2, username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("invalid bound");
    }

    @Test
    public void testLoginSecQWrongUsername() {
        Result result = loginController.getQuestion(0, "h");
        assert !result.isSuccessful();
        assert result.getMessage().equals("no such user");
    }

    @Test
    public void testLoginSecQSuccessGettingQuestion() {
        Result result = loginController.getQuestion(0, username);
        assert result.isSuccessful();
        assert result.getMessage().equals("q");
    }

    @Test
    public void testLoginSecCheckAnswer() {
        Result result = loginController.checkQuestionSecurity("0", "a", username);
        assert result.isSuccessful();
        assert result.getMessage().equals("Correct answer");
    }

    @Test
    public void testLoginSecCheckAnswerWrong() {
        Result result = loginController.checkQuestionSecurity("0", "b", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("Wrong answer");
    }

    @Test
    public void testLoginSecNoSuchUser() {
        Result result = loginController.checkQuestionSecurity("0", "a", "f");
        assert !result.isSuccessful();
        assert result.getMessage().equals("Wrong answer");
    }

    @Test
    public void testLoginSecInvalidFormat() {
        Result result = loginController.checkQuestionSecurity("0dr", "a", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("Wrong number format");
    }

    @Test
    public void testLoginSecInvalidBound() {
        Result result = loginController.checkQuestionSecurity("10", "a", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("invalid bound");
    }
}
