package view.Controller;

import Model.Result;
import controller.Client;
import controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import view.AppView;
import Enum.Menus;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginViewController extends ViewController implements Initializable {

    public static volatile Result result;
    public Button next;
    public Label question;
    public TextField answer;
    public TextField number;
    public Button show;
    public CheckBox stayLoggedIn;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    @FXML
    Button forgotPassword;

    @FXML
    Button registerButton;

    @FXML
    Pane pane;

    private void waitForResponse() {
        while (result == null) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                // Handle interruption (e.g., log it)
            }
        }
    }

    @FXML
    public void loginAction() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        Client.client.sendLoginRequest(username, password);
        waitForResponse();
        if (!result.isSuccessful()) {
            throwAlert(result.getTitle(), result.getMessage());
            result = null;
            return;
        }

        throwAlert(result.getTitle(), result.getMessage());
        result = null;
        if (stayLoggedIn.isSelected()) {
            try {
                Path path = Paths.get("username.txt");
                String content = usernameField.getText();
                Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Path path = Paths.get("password.txt");
                String content = passwordField.getText();
                Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        usernameField.clear();
        passwordField.clear();
        changeMenuTo(Menus.Main.getValue());
    }

    @FXML
    public void questionAction() {

        if (usernameField.getText().isBlank()) {
            throwAlert("warning", "enter username first");
            return;
        }
        if (passwordField.getText().isBlank()) {
            throwAlert("warning", "enter new password");
            return;
        }
        result = null;
        toggle();
    }

    @FXML
    public void registerAction() {
        usernameField.clear();
        passwordField.clear();

        changeMenuTo(Menus.Register.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Path path = Paths.get("username.txt");
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                usernameField.setText(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Path path = Paths.get("password.txt");
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                passwordField.setText(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setPane(this.pane);
        setScaleOnInitialization();
        result = null;
    }


    public void next(ActionEvent actionEvent) {
        result = null;
        Client.client.sendSecurityAnswers(usernameField.getText(), number.getText(), answer.getText(), passwordField.getText());
        waitForResponse();
        if (result.isSuccessful()) {
            throwAlert(result.getTitle(), result.getMessage());
            changeMenuTo(Menus.Main.getValue());
        } else {
            throwAlert(result.getTitle(), result.getMessage());
        }
    }

    public void toggle() {
        if (next.isVisible()) {
            next.setVisible(false);
            question.setVisible(false);
            number.setVisible(false);
            answer.setVisible(false);
            show.setVisible(false);
        } else {
            next.setVisible(true);
            question.setVisible(true);
            number.setVisible(true);
            answer.setVisible(true);
            show.setVisible(true);
        }
    }

    public void showQ(ActionEvent actionEvent) {
        result = null;
        if (number.getText().isBlank() || usernameField.getText().isBlank()) {
            throwAlert("oops", "invalid number or username");
            return;
        }
        if (!Pattern.compile("\\d+").matcher(number.getText()).matches()) {
            throwAlert("enter number lad", "what are u doing");
            return;
        }
        Client.client.sendQuestionSecurityRequest(number.getText(), usernameField.getText());
        waitForResponse();
        String q = result.getMessage();
        question.setText(q);
    }
}
