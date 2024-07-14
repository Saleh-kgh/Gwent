package view.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import view.AppView;
import Enum.Sizes;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewController {

    private static final double alertAnimDuration = 0.5;

    @FXML
    private Pane pane;

    public double getAlertAnimDuration() {
        return alertAnimDuration;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public void setScaleOnInitialization() {
        double scaleFactor = Sizes.getUserScreenScale();
        Scale scale = new Scale(scaleFactor, scaleFactor);
        pane.getTransforms().add(scale);
    }

    public static void changeMenuTo(String menuName) {
        PauseTransition delay = new PauseTransition(Duration.seconds(alertAnimDuration + 1));
        delay.setOnFinished(event -> {
            AppView.setRootForMenu(menuName);
        });
        delay.play();
    }

    public void exitProgram() {
        PauseTransition delay = new PauseTransition(Duration.seconds(alertAnimDuration + 1));
        delay.setOnFinished(actionEvent -> {
            System.exit(0);
        });
        delay.play();
    }

    public void throwAlert(String title, String message) {

        Rectangle alertBox = new Rectangle(600, 200);
        alertBox.getStyleClass().add("alert-box");
        alertBox.setOpacity(0.1);

        Label alertTitle = new Label(title);
        alertTitle.getStyleClass().add("alert-title");

        Label alertMessage = new Label(message);
        alertMessage.getStyleClass().add("alert-message");

        VBox vbox = new VBox(10, alertTitle, alertMessage);
        vbox.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(alertBox, vbox);

        alertBox.layoutXProperty().bind(pane.widthProperty().subtract(alertBox.widthProperty()).divide(2));
        alertBox.layoutYProperty().bind(pane.heightProperty().subtract(alertBox.heightProperty()).divide(2));

        vbox.layoutXProperty().bind(pane.widthProperty().subtract(vbox.widthProperty()).divide(2));
        vbox.layoutYProperty().bind(pane.heightProperty().subtract(vbox.heightProperty()).divide(2));

        FadeTransition fadeInVBox = new FadeTransition(Duration.millis(500), vbox);
        fadeInVBox.setFromValue(0.0);
        fadeInVBox.setToValue(1.0);

        FadeTransition fadeInAlertBox = new FadeTransition(Duration.millis(500), alertBox);
        fadeInAlertBox.setFromValue(0.0);
        fadeInAlertBox.setToValue(1.0);

        FadeTransition fadeOutVBox = new FadeTransition(Duration.millis(500), vbox);
        fadeOutVBox.setFromValue(1.0);
        fadeOutVBox.setToValue(0.0);

        FadeTransition fadeOutAlertBox = new FadeTransition(Duration.millis(500), alertBox);
        fadeOutAlertBox.setFromValue(1.0);
        fadeOutAlertBox.setToValue(0.0);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            fadeOutVBox.play();
            fadeOutAlertBox.play();
            fadeOutVBox.setOnFinished(e -> pane.getChildren().removeAll(alertBox, vbox));
        });

        fadeInVBox.play();
        fadeInAlertBox.play();
        delay.play();
    }

    public void throwConfirmAlert(String title, String message, Runnable onConfirm) {

        Rectangle alertBox = new Rectangle(600, 200);
        alertBox.getStyleClass().add("alert-box");
        alertBox.setOpacity(0.1);

        Label alertTitle = new Label(title);
        alertTitle.getStyleClass().add("alert-title");

        Label alertMessage = new Label(message);
        alertMessage.getStyleClass().add("alert-message");


        // Buttons
        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("confirm-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("cancel-button");

        HBox buttonBox = new HBox(20, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, alertTitle, alertMessage, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        confirmButton.setOnAction(event -> {
            onConfirm.run(); // Execute the provided runnable
            closeAlert(alertBox, vbox);
        });

        cancelButton.setOnAction(event -> {
            closeAlert(alertBox, vbox);
        });


        pane.getChildren().addAll(alertBox, vbox);

        // Centering on the pane
        alertBox.layoutXProperty().bind(pane.widthProperty().subtract(alertBox.widthProperty()).divide(2));
        alertBox.layoutYProperty().bind(pane.heightProperty().subtract(alertBox.heightProperty()).divide(2));

        vbox.layoutXProperty().bind(pane.widthProperty().subtract(vbox.widthProperty()).divide(2));
        vbox.layoutYProperty().bind(pane.heightProperty().subtract(vbox.heightProperty()).divide(2));

        // Fade-in animation for the alert box and text
        FadeTransition fadeInVBox = new FadeTransition(Duration.millis(500), vbox);
        fadeInVBox.setFromValue(0.0);
        fadeInVBox.setToValue(1.0);

        FadeTransition fadeInAlertBox = new FadeTransition(Duration.millis(500), alertBox);
        fadeInAlertBox.setFromValue(0.0);
        fadeInAlertBox.setToValue(1.0);

        fadeInVBox.play();
        fadeInAlertBox.play();
        AppView.playSfxAudio("warning");
    }

    private void closeAlert(Rectangle alertBox, VBox vbox) {
        // Fade-out animation for the alert box and text
        FadeTransition fadeOutVBox = new FadeTransition(Duration.millis(500), pane.getChildren().get(pane.getChildren().indexOf(vbox))); // Index 1 for vbox
        fadeOutVBox.setFromValue(1.0);
        fadeOutVBox.setToValue(0.0);

        FadeTransition fadeOutAlertBox = new FadeTransition(Duration.millis(500), pane.getChildren().get(pane.getChildren().indexOf(alertBox))); // Index 0 for alertBox
        fadeOutAlertBox.setFromValue(1.0);
        fadeOutAlertBox.setToValue(0.0);

        fadeOutVBox.play();
        fadeOutAlertBox.play();

        // Remove alert elements from the pane after fade-out
        fadeOutVBox.setOnFinished(e -> {
            pane.getChildren().remove(pane.getChildren().get(pane.getChildren().indexOf(vbox)));
            pane.getChildren().remove(pane.getChildren().get(pane.getChildren().indexOf(alertBox)));
        });
    }

    public static String addBorderPane(Pane mainPane,
                                       ArrayList<Button> buttons,
                                       TextField textFields,
                                       TextField labels) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(mainPane);

        double scale = 0.25;
        mainPane.setScaleX(scale);
        mainPane.setScaleY(scale);

        anchorPane.setPrefWidth(mainPane.getPrefWidth() * scale);
        anchorPane.setPrefHeight(mainPane.getPrefHeight() * scale);
        VBox main = new VBox(10);
        anchorPane.getChildren().add(main);
        main.getChildren().addAll(labels);
        main.getChildren().addAll(textFields);
        HBox hBox = new HBox(5);
        main.getChildren().add(hBox);
        hBox.getChildren().addAll(buttons);
        return anchorPane.toString();
    }
//    public static WritableImage takeScreenshot() {
        //  screen shot begire ke badesh bedim be client befreste
//    }
}
