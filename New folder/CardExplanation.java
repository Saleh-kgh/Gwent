package Model.InGameObjects.Cards;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class CardExplanation extends Rectangle {

    private Label label;

    public CardExplanation(String description) {
        super(400, 200);
        super.setFill(Color.BLACK);
        super.setStroke(Color.web("#292929"));
        super.setStrokeWidth(2);

        label = new Label(description);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        label.setWrapText(true);
    }

    public void showDescriptionInScene(Pane pane) {
        label.setPrefWidth(200); // Set a preferred width for the label

        double labelWidth = label.prefWidth(-1);
        double labelHeight = label.prefHeight(labelWidth);

        label.setLayoutX(getLayoutX() + this.getWidth() / 2 - labelWidth / 2);
        label.setLayoutY(getLayoutY() + this.getHeight() / 2 - labelHeight / 2);
        if (labelHeight > this.getHeight()) {
            label.setLayoutY(getLayoutY()); // Adjust the label position to the top if it exceeds the box height
        }
        pane.getChildren().addAll(this, label);
    }

    public void removeDescriptionFromScene(Pane pane) {
        pane.getChildren().removeAll(this, label);
    }
}
