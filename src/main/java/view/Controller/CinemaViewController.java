package view.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

public class CinemaViewController extends ViewController implements Initializable {
    public static Image mainScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setScaleOnInitialization();
    }

    public void back(ActionEvent actionEvent) {

    }
}
