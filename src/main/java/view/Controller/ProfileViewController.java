package view.Controller;

import Model.Result;
import controller.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Enum.Menus;
import javafx.event.EventHandler;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileViewController extends ViewController implements Initializable {
    public ListView RequestsListView;
    public Label friendUsername;
    public TextField searchText;
    public ListView usersListview;
    public TextField friend;
    public AnchorPane friendsTab;
    public ListView friendsListView;
    public TextField friendName;
    public ListView reqListView;
    public AnchorPane reqTab;
    double initialX = 693.0;
    double initialY = 112.0;
    public Pane pane;
    public TextField username;
    public TextField nickname;
    public TextField email;
    public TextField password;
    public TextField oldPassword;
    public TextField numberOfGames;
    public static Result result;
    public ChoiceBox choiceBox;
    @FXML
    private AnchorPane movableAnchorPane;
    private boolean altKeyPressed = false;
    @FXML
    private ListView historyScrollPane;
    public static ArrayList<String> ranks = new ArrayList<>();
    public static ArrayList<String> users = new ArrayList<>();
    @FXML
    private AnchorPane historyPane;

    private void waitForResponse() {
        while (result == null && users == null && ranks == null) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                // Handle interruption (e.g., log it)
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movableAnchorPane.setVisible(!movableAnchorPane.isVisible());
        super.setPane(pane);
        setScaleOnInitialization();
        movableAnchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initialX = event.getSceneX();
                initialY = event.getSceneY();
            }
        });

        movableAnchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!altKeyPressed) {
                    // Update the translation values directly
                    movableAnchorPane.setTranslateX(event.getSceneX() - initialX);
                    movableAnchorPane.setTranslateY(event.getSceneY() - initialY);
                }
            }
        });

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ALT) {
                movableAnchorPane.setVisible(!movableAnchorPane.isVisible());
                altKeyPressed = true;
            }
        });

        pane.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ALT) {
                altKeyPressed = false;
                movableAnchorPane.setTranslateX(0);
                movableAnchorPane.setTranslateY(0);
            }
        });
    }

    public void usernameChange(MouseEvent mouseEvent) {
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange(username.getText(), "usernameChange");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
        if (result.isSuccessful())
            Client.client.getUser().setUsername(username.getText());
    }

    public void nicknameChange(MouseEvent mouseEvent) {
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange(nickname.getText(), "nicknameChange");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
        if (result.isSuccessful())
            Client.client.getUser().setNickname(nickname.getText());
    }

    public void emailChange(MouseEvent mouseEvent) {
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange(email.getText(), "emailChange");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
        if (result.isSuccessful())
            Client.client.getUser().setEmail(email.getText());
    }

    public void passwordChange(MouseEvent mouseEvent) {
        if (Client.client.getUser().getPassword().equals(oldPassword.getText())) {
            if (password.getText().equals(oldPassword.getText())) {
                throwAlert("nah", "new one should be different");
                return;
            }
            result = null;
            users = null;
            ranks = null;
            Client.client.sendPasswordChange(password.getText());
            waitForResponse();
            if (result.isSuccessful())
                Client.client.getUser().setPassword(password.getText());
            throwAlert(result.getTitle(), result.getMessage());
        } else {
            throwAlert("passwords", "you did not enter old password properly");
        }
    }

    public void showInfo(MouseEvent mouseEvent) {
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange("none", "info");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
    }


    public void Back(MouseEvent mouseEvent) {
        changeMenuTo(Menus.Main.getValue());
    }

    public void showHistory(MouseEvent mouseEvent) {
        if (!Pattern.compile("[+-]?\\d+").matcher(numberOfGames.getText()).matches()) {
            throwAlert("invalid number", "you should enter a number lad not random chars");
        } else {
            int n = Integer.parseInt(numberOfGames.getText());
            result = null;
            users = null;
            Client.client.sendUsernameChange(String.valueOf(n), "gameHistory");
            waitForResponse();
            throwAlert(result.getTitle(), result.getMessage());
        }
    }

    public void search(ActionEvent actionEvent) {
        if (searchText.getText().isEmpty()) {
            throwAlert("search failed", "enter a name bro");
            return;
        }
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange(searchText.getText(), "search");
        waitForResponse();
        ObservableList<String> items = FXCollections.observableArrayList(users);
        usersListview.setItems(items);
        usersListview.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-text-fill: white;     -fx-border-radius: 50px; -fx-background-color: black; -fx-border-color: white; -fx-font-style: italic;"); // Set custom styles here

            }
        });
    }

    public void send(ActionEvent actionEvent) {
        if (searchText.getText().isEmpty()) {
            throwAlert("search failed", "enter a name bro");
            return;
        }
        result = null;
        users = null;
        ranks = null;
        Client.client.sendUsernameChange(searchText.getText(), "friendReq");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
    }

    public void refresh(ActionEvent actionEvent) {
        result = null;
        users = null;
        ranks = null;
        Client.client.getHistory();
        waitForResponse();
        ObservableList<String> items = FXCollections.observableArrayList(ranks);
        RequestsListView.setItems(items);
        RequestsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-font-style: italic;"); // Set custom styles here

            }
        });
    }

    public void reject(ActionEvent actionEvent) {
        if (friend.getText().isEmpty()) {
            throwAlert("failed", "enter a username!");
            return;
        }
        result = null;
        users = null;
        ranks = null;
        Client.client.reject(friend.getText());
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());

    }

    public void accept(ActionEvent actionEvent) {
        if (friend.getText().isEmpty()) {
            throwAlert("failed", "enter a username!");
            return;
        }
        result = null;
        users = null;
        ranks = null;
        Client.client.accept(friend.getText());
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
    }

    public void getFriends(Event event) {
        result = null;
        users = null;
        ranks = null;
        Client.client.getFriends();
        waitForResponse();
        ObservableList<String> items = FXCollections.observableArrayList(ranks);
        friendsListView.setItems(items);
        friendsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-font-style: Bold;");
            }
        });
    }

    public void showProfile(ActionEvent actionEvent) {
        result = null;
        users = null;
        ranks = null;
        if (friendName.getText().isEmpty()) {
            throwAlert("error", "enter his/her name bro");
            return;
        }
        Client.client.sendUsernameChange(friendName.getText(), "getProfile");
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
    }

    public void startGame() {
        result = null;
        users = null;
        ranks = null;
        if (friendName.getText().isEmpty()) {
            throwAlert("hey you", "enter his/her name bro");
            return;
        }
        Client.client.requestForNewGameWithFriend(friendName.getText());
        waitForResponse();
        throwAlert(result.getTitle(), result.getMessage());
        if (result.isSuccessful()) {
            changeMenuTo(Menus.PreGame.getValue());
        }
    }

    public void getReqs(Event event) {
        result = null;
        users = null;
        ranks = null;
        Client.client.getReqs();
        waitForResponse();
        ObservableList<String> items = FXCollections.observableArrayList(ranks);
        reqListView.setItems(items);
        reqListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-border-color: white; -fx-font-style: Bold;");
            }
        });
    }
}