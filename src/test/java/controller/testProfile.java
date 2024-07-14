package controller;

import Model.Result;
import Model.User.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class testProfile {
    private String username, password, nickname, email;
    private ProfileController profileController;
    private User user;

    @Before
    public void init() {
        profileController = new ProfileController();
        username = "user";
        password = "abAB12$$";
        nickname = "test";
        email = "tt@t.com";
        user = new User(username, password, nickname, email, true);
        new User("aa", "1234", "aa", "asdas@sd.com", true);
    }

    @Test
    public void testChangeUsernameRepeatedName() {
        Result result = profileController.changeUsername(username, username);
        assert !result.isSuccessful();
        assert result.getTitle().equals("username problem");
    }

    @Test
    public void testChangeUsernameAlreadyTaken() {
        Result result = profileController.changeUsername("aa", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("username taken");
    }

    @Test
    public void testChangeUsernameInvalid() {
        Result result = profileController.changeUsername("rr>", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("username invalid");
    }

    @Test
    public void testChangeNickname() {
        Result result = profileController.changeNickname(nickname, username);
        assert !result.isSuccessful();
        assert result.getTitle().equals("Nickname");
    }

    @Test
    public void testChangePasswordInvalid() {
        Result result = profileController.changePassword("<password>", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password invalid");
    }

    @Test
    public void testChangePasswordStrong() {
        Result result = profileController.changePassword("asad", username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password weak");
    }

    @Test
    public void testChangeEmailRepeated() {
        Result result = profileController.changeEmail(email, username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("new email must be different from old one");
    }

    @Test
    public void testShowGameHistory() {
        Result result = profileController.showGameHistory(-4, username);
        assert !result.isSuccessful();
        assert result.getMessage().equals("enter a number greater than 1");
    }
}
