package view.Controller;

import Model.User.User;
import controller.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import view.AppView;
import Enum.Menus;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends ViewController implements Initializable {

    public Button roomButton;
    @FXML
    Button startGameButton;

    @FXML

    Button profileButton;
    @FXML
    Button ldBoardButton;

    @FXML
    Button cheatsButton;

    @FXML
    Button logoutButton;

    @FXML
    Button exitButton;

    @FXML
    Pane pane;

    @FXML
    public void startGameAction() {
        throwAlert("Starting Game!", "");
        changeMenuTo(Menus.PreGame.getValue());
    }

    @FXML
    public void profileAction() {
        changeMenuTo(Menus.Profile.getValue());
    }

    @FXML
    public void ldBoardAction() {
        throwAlert("time to see the champions", "din din dara!");
        changeMenuTo(Menus.LeaderBoard.getValue());
    }

    @FXML
    public void cheatsAction() {

    }

    @FXML
    public void logoutAction() {
        AppView.setLoggedInUser(null);
        Client.client.setUser(null);
        Client.client.sendEndClient();
        changeMenuTo(Menus.Login.getValue());
    }

    @FXML
    public void exitAction() {
        throwAlert("Exiting The Game", "So long then!");
        exitProgram();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setPane(pane);
        setScaleOnInitialization();
    }

    public void Rooms(MouseEvent mouseEvent) {
        throwAlert("sss", "quiet! people are playing");
        AppView.setRootForMenu("room");
    }
}
