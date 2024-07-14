package view.Animations;

import Model.InGameObjects.Board.Row;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FadingAnimation extends Transition {

    private final int animDuration = 2000;

    private Pane pane;

    private String imageAddress;

    private Rectangle effect;

    private boolean isRemovable;

    private boolean isPlaying;

    public FadingAnimation(Pane pane, String imageAddress) {
        this.pane = pane;
        this.imageAddress = imageAddress;
        isPlaying = false;

        isRemovable = true;
        if (pane instanceof Row)
            isRemovable = false;

        setUpEffect();

        setCycleCount(1);
        setCycleDuration(Duration.millis(animDuration));
    }

    private void setUpEffect() {
        if (isRemovable) effect = new Rectangle(72.4, 105);
        else effect = new Rectangle(950, 119);
        effect.setFill(new ImagePattern(new Image(getClass().getResource("/Pics/Icons/InGameEffects/" +
                imageAddress + ".png").toExternalForm())));
        effect.setLayoutX(0);
        effect.setLayoutY(0);

        if (!isRemovable) {
            if (((Row)pane).getWeatherEffect() == null) {
                ((Row) pane).setWeatherEffect(effect);
            }
            effect.setLayoutX(-134);
        }

        effect.setOpacity(0.01);

        pane.getChildren().add(effect);
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Rectangle getEffect() {
        return effect;
    }

    public void setEffect(Rectangle effect) {
        this.effect = effect;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
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
            effect.setOpacity(scale);
        } else if (v >= 0.25 && v < 0.75) {
            effect.setOpacity(1);
        } else if (isRemovable){
            effect.setOpacity(1 - (v - 0.7) / 0.3);
        }

        this.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isRemovable)
                    pane.getChildren().remove(effect);
            }
        });
    }
}
