package view.Controller;

import Model.Result;
import Model.User.User;
import controller.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import Enum.Menus;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.AppView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterViewController extends ViewController implements Initializable {
    public static volatile Result result;
    private final double alertAnimDuration = 3;
    public Button generatePassword;
    public Button nextB;
    public Button doneB;
    public TextField secQ;
    public TextField secA;
    public Label label;
    @FXML
    PasswordField passwordRepeatField;

    @FXML
    Label createAccount;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField nickNameField;

    @FXML
    TextField emailField;

    @FXML
    Button registerButton;

    @FXML
    Button registerCheat;

    @FXML
    Pane pane;

    @FXML
    public void registerAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nickName = nickNameField.getText();
        String email = emailField.getText();
        if (!password.equals(passwordRepeatField.getText())) {
            throwAlert("password fields do not match", "repeat the exact password");
            return;
        }
        Client.client.sendRegisterRequest(username, password, nickName, email);
        waitForResponse();
        if (result.isSuccessful()) {
            throwAlert("Registered Successfully!", "Dear \"" + nickName + "\" welcome to the Gwent World!");
            toggle();
            registerButton.setVisible(false);
        } else {
            if (result.getTitle() != null && result.getTitle().equals("that username taken try:")) {
                String name = result.getMessage();
                usernameField.setText(name);
                throwAlert(result.getTitle(), name + "do you confirm? tab register");
            } else
                throwAlert("operation failed", result.getMessage());
        }
        result = null;
    }

    private void waitForResponse() {
        while (result == null) {
            Thread.onSpinWait();
        }
    }

    @FXML
    public void registerCheatAction() {
        Client.client.sendUsernameChange("", "Admin");
        User admin = new User("22", "22", "Geralt of Karaj", "Email", true);
        admin.getGameInfo().addScore(22);
        throwAlert("Admin Created", "Welcome Omid|Javad|Saleh!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setPane(this.pane);
        setScaleOnInitialization();
    }

    public void generatePasswordAction() {
        String password = AppView.getServer().getRegisterController().generateStrongPassword();
        throwAlert("your new password", password);
        passwordRepeatField.setText(password);
        passwordField.setText(password);
    }

    public void back() {
        changeMenuTo(Menus.Login.getValue());
    }

    public void done(ActionEvent actionEvent) {
        String username = usernameField.getText();
        Client.client.sendUpdateSecurityRequest(username, secQ.getText(), secA.getText());
        secQ.clear();
        secA.clear();
        toggle();
        throwAlert("done", "securities saved, dont change anything unless this message is gone");
        usernameField.setText("");
        passwordField.setText("");
        passwordRepeatField.setText("");
        emailField.setText("");
        nickNameField.setText("");
        registerButton.setVisible(true);
    }

    public void next(ActionEvent actionEvent) {
        String username = usernameField.getText();
        Client.client.sendUpdateSecurityRequest(username, secQ.getText(), secA.getText());
        secQ.clear();
        secA.clear();
        throwAlert("saving", "securities being saved, dont change anything unless this message is gone");
    }

    public void toggle() {
        if (secQ.isVisible()) {
            secQ.setVisible(false);
            secA.setVisible(false);
            doneB.setVisible(false);
            nextB.setVisible(false);
        } else {
            secQ.setVisible(true);
            secA.setVisible(true);
            doneB.setVisible(true);
            nextB.setVisible(true);
        }
    }
}
