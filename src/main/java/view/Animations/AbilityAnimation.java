package view.Animations;

import Model.InGameObjects.Cards.RowCard;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class AbilityAnimation extends Transition {

    private final int animDuration = 2000;

    private RowCard rowCard;

    private String imageAddress;

    private Circle effect;

    private boolean isPlaying;

    public AbilityAnimation(RowCard rowCard, String imageAddress) {
        this.rowCard = rowCard;
        this.imageAddress = imageAddress;
        isPlaying = false;
        setUpEffect();

        setCycleCount(1);
        setCycleDuration(Duration.millis(animDuration));
    }

    private void setUpEffect() {
        effect = new Circle(26);
        effect.setFill(new ImagePattern(new Image(getClass().getResource(imageAddress).toExternalForm())));
        effect.setLayoutX(37);
        effect.setLayoutY(50);

        effect.setScaleX(0.01);
        effect.setScaleY(0.01);

        rowCard.getChildren().add(effect);
    }

    public RowCard getRowCard() {
        return rowCard;
    }

    public void setRowCard(RowCard rowCard) {
        this.rowCard = rowCard;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    protected void interpolate(double v) {
        if (v < 0.25) {
            double scale = v * 4;
            effect.setScaleX(scale);
            effect.setScaleY(scale);
        }
        else if (v >= 0.25 && v < 0.75) {
            effect.setScaleX(1);
            effect.setScaleY(1);
        }
        else {
            double scale = (1 - v) * 4;
            effect.setScaleX(scale);
            effect.setScaleY(scale);
            effect.setOpacity(1 - (v - 0.7) / 0.3);
        }

        this.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rowCard.getChildren().remove(effect);
            }
        });
    }
}
