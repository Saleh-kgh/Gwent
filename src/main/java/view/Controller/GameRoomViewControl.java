package view.Controller;

import Model.Result;
import controller.Client;
import controller.GameRoomControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import view.AppView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import Enum.Menus;

public class GameRoomViewControl extends ViewController implements Initializable {
    public Pane pane;
    public ListView gameRooms;
    public Button toggleButton;
    public TextField number;
    public static Result result;

    private ScheduledExecutorService executor;

    private void waitForResponse() {
        while (GameRoomControl.CurrentGames == null && GameRoomControl.AllGames == null && result == null) {
            Thread.onSpinWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameRoomControl.CurrentGames = null;
        GameRoomControl.AllGames = null;
        result = null;
        Client.client.sendGetCurrentGamesRequest();
        waitForResponse();
        super.setPane(pane);
        setScaleOnInitialization();
        ArrayList<String> rooms = GameRoomControl.CurrentGames;
        ObservableList<String> items = FXCollections.observableArrayList(rooms);
        gameRooms.setItems(items);
        initGraphic();

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::refreshTask, 3, 2, TimeUnit.SECONDS);
    }

    private void refreshTask() {
        refresh(null);
    }

    public void back(ActionEvent actionEvent) {
        throwAlert("returning", "hope you enjoyed");
        AppView.setRootForMenu(Menus.Main.getValue());
    }

    public void toggle(MouseEvent mouseEvent) {
        GameRoomControl.CurrentGames = null;
        GameRoomControl.AllGames = null;
        result = null;
        if (toggleButton.getText().equals("current Rooms")) {

            Client.client.sendGetAllGamesRequest();
            waitForResponse();
            ArrayList<String> rooms = GameRoomControl.AllGames;
            ObservableList<String> items = FXCollections.observableArrayList(rooms);
            gameRooms.setItems(items);
            toggleButton.setText("all Rooms");
        } else {
            Client.client.sendGetCurrentGamesRequest();
            waitForResponse();
            ArrayList<String> rooms = GameRoomControl.CurrentGames;
            ObservableList<String> items = FXCollections.observableArrayList(rooms);
            gameRooms.setItems(items);
            toggleButton.setText("current Rooms");
        }
        initGraphic();
    }

    public void refresh(ActionEvent actionEvent) {
        GameRoomControl.CurrentGames = null;
        GameRoomControl.AllGames = null;
        result = null;
        if (toggleButton.getText().equals("current Rooms")) {
            Client.client.sendGetCurrentGamesRequest();
            waitForResponse();
            ArrayList<String> rooms = GameRoomControl.CurrentGames;
            ObservableList<String> items = FXCollections.observableArrayList(rooms);
            gameRooms.setItems(items);
        } else {
            Client.client.sendGetAllGamesRequest();
            waitForResponse();
            ArrayList<String> rooms = GameRoomControl.AllGames;
            ObservableList<String> items = FXCollections.observableArrayList(rooms);
            gameRooms.setItems(items);
            initGraphic();
        }
    }

    public void initGraphic() {
        gameRooms.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-text-fill: white; " +
                        " -fx-border-radius: 50px;" +
                        " -fx-background-color: black; " +
                        "-fx-border-color: white;" +
                        " -fx-font-style: italic;"); // Set custom styles here

            }
        });
    }

    public void goTo(ActionEvent actionEvent) {
        if (number.getText().isEmpty()) {
            throwAlert("hey you", "enter name of which member's game to join");
            return;
        }
        GameRoomControl.CurrentGames = null;
        GameRoomControl.AllGames = null;
        result = null;
        Client.client.JoinGame(number.getText());
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
        if (result.isSuccessful()) {
            changeMenuTo("cinema");
        }
    }
}
