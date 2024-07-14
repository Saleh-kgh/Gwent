package controller;

import Model.Result;
import org.junit.*;

import java.util.regex.Pattern;

public class testRegister {
    private String username, password, nickname, email;
    private RegisterController registerController;

    @Before
    public void init() {
        registerController = new RegisterController();
        username = "user";
        password = "abAB12$$";
        nickname = "test";
        email = "tt@t.com";
    }

    @Test
    public void testGeneratePasswordValid() {
        password = registerController.generateStrongPassword();
        assert Pattern.compile("[!@#$%^&*()_=+0-9a-zA-Z-]+").matcher(password).matches();
    }

    @Test
    public void testGeneratePasswordStrong() {
        password = registerController.generateStrongPassword();
        assert Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_=+-]).{8,}$").matcher(password).matches();
    }

    @Test
    public void testRegisterSuccessful() {
        Result result = registerController.register(username, password, nickname, email);
        assert result.isSuccessful();
    }

    @Test
    public void testRegisterSuccessfulWithUsername() {
        username = "seda0";
        Result result = registerController.register(username, password, nickname, email);
        assert result.isSuccessful();
    }

    @Test
    public void testRegisterInvalidUsername() {
        username = ">";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("username invalid");
    }

    @Test
    public void testRegisterInvalidPassword() {
        password = ">";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password invalid");
    }

    @Test
    public void testRegisterStrongPasswordOne() {
        password = "aa";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password weak");
    }

    @Test
    public void testRegisterStrongPasswordTwo() {
        password = "adasd&f";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password weak");
    }

    @Test
    public void testRegisterStrongPasswordThree() {
        password = "sdfsdf99fg";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("password weak");
    }

    @Test
    public void testRegisterInvalidEmailOne() {
        email = ">";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("email invalid");
    }

    @Test
    public void testRegisterInvalidEmailTwo() {
        email = "asdasd.com";
        Result result = registerController.register(username, password, nickname, email);
        assert !result.isSuccessful();
        assert result.getMessage().equals("email invalid");
    }
}
