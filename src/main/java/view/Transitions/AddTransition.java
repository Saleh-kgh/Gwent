package view.Transitions;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.CloseArea;
import Model.InGameObjects.Board.RangedArea;
import Model.InGameObjects.Board.SiegeArea;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;
import javafx.animation.Transition;
import javafx.util.Duration;
import view.AppView;

public class AddTransition extends Transition {

    private final double duration = 1.5;

    private Card card;

    private BoardSection boardSection;

    private double begLayoutX;

    private double begLayoutY;

    private double desLayoutX;

    private double desLayoutY;

    private double speedX;

    private double speedY;

    public AddTransition(Card card, BoardSection boardSection) {
        this.card = card;
        this.boardSection = boardSection;

        card.setTransiting(true);

        begLayoutX = card.getCurrentPlaceInGame().translateRowCardAbsoluteLayoutX(card);
        begLayoutY = card.getCurrentPlaceInGame().translateRowCardAbsoluteLayoutY(card);

        desLayoutX = boardSection.getCardLayoutXAfterTransition(card);
        desLayoutY = boardSection.getCardLayoutYAfterTransition(card);

        card.getPlayer().getGamePane().getChildren().add(card.getRowCard());
        card.getRowCard().setLayoutX(begLayoutX);
        card.getRowCard().setLayoutY(begLayoutY);

        calculateSpeedX();
        calculateSpeedY();

        playPuttingCardSfx();

        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(100));
    }

    private void playPuttingCardSfx() {
        if (card instanceof UnitCard) {
            if (((UnitCard) card).isHero()) AppView.playSfxAudio("hero");
            else if (boardSection instanceof CloseArea) AppView.playSfxAudio("common1");
            else if (boardSection instanceof RangedArea) AppView.playSfxAudio("common2");
            else if (boardSection instanceof SiegeArea) AppView.playSfxAudio("common3");
        }
    }

    private void calculateSpeedX() {
        speedX = (desLayoutX - begLayoutX) / duration;
    }

    private void calculateSpeedY() {
        speedY = (desLayoutY - begLayoutY) / duration;
    }

    private void stopTransition() {
        if (!card.isTransitioning())
            return;

        card.setTransiting(false);
        this.stop();
        card.getPlayer().setParalyzedByAnimation(false);
        card.getPlayer().getGameController().addCardToBoardSection(card, boardSection);
        card.getPlayer().getBoard().updateAllSections();
//        Client.client.sendScreenShot();
    }

    @Override
    protected void interpolate(double v) {
        double elapsedTime = v * getCycleDuration().toSeconds();

        double deltaX = elapsedTime * speedX;

        card.getRowCard().setLayoutX(card.getRowCard().getLayoutX() + deltaX);

        double deltaY = elapsedTime * speedY;

        card.getRowCard().setLayoutY(card.getRowCard().getLayoutY() + deltaY);

        if (begLayoutX > desLayoutX) {
            if (card.getRowCard().getLayoutX() <= desLayoutX) {
                stopTransition();
            }
        }
        else {
            if (card.getRowCard().getLayoutX() >= desLayoutX) {
                stopTransition();
            }
        }
    }
}