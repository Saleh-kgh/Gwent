package Model.InGameObjects.Cards;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.AbilityAnimation;
import view.AppView;

import java.io.Serializable;

public abstract class CardAbility implements Serializable {
    private Card card;

    private String description;

    abstract public void applyAbility();

    abstract public CardAbility deepCopy();

    public String getAbilityIcon() {
        return null;
    }

    abstract public void writeDescription();

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void playSfx(String mediaName) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(0));
        pauseTransition.setOnFinished(event -> {
            AppView.playSfxAudio(mediaName);
        });
        pauseTransition.play();
    }

    public void playAnimation(String imageName) {
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(0));
        pauseTransition.setOnFinished(event -> {
            new AbilityAnimation(getCard().getRowCard(),
                    "/Pics/Icons/InGameEffects/anim_" + imageName + ".png").play();
        });
        pauseTransition.play();
    }
}
