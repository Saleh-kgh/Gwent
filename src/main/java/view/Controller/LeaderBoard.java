package view.Controller;

import Enum.Menus;
import controller.Client;
import controller.LeaderBoardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import view.AppView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeaderBoard extends ViewController implements Initializable {
    public ListView leaderboardListView;
    public ChoiceBox choiceBox;
    public Pane pane;
    public static ArrayList<String> ranks;
    private ScheduledExecutorService executor;

    private void waitForResponse() {
        while (ranks == null) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                // Handle interruption (e.g., log it)
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.setPane(pane);
        setScaleOnInitialization();

        // Create a list of items to display in the ListView
        Client.client.getLeaderBoard();
        waitForResponse();
        // Create an ObservableList from the ranks
        ObservableList<String> items = FXCollections.observableArrayList(ranks);

        // Set the items to the ListView
        leaderboardListView.setItems(items);
        // Populate the ListView with the items
        leaderboardListView.setItems(items);
        ranks = null;
        initGraphic();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::refreshTask, 3, 2, TimeUnit.SECONDS);
    }

    private void refreshTask() {
        refresh(null);
    }

    public void refresh(ActionEvent actionEvent) {
        Client.client.getLeaderBoard();
        waitForResponse();
        ObservableList<String> items = FXCollections.observableArrayList(ranks);
        leaderboardListView.setItems(items);
        initGraphic();
    }

    public void back(ActionEvent actionEvent) {

        AppView.setRootForMenu(Menus.Main.getValue());
    }

    public void initGraphic() {
        leaderboardListView.setCellFactory(param -> new ListCell<String>() {
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
}
