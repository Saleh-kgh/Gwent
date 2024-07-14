package Model.InGameObjects.Abilities.LeaderAbilities.Foltest;

import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.FadingAnimation;

import java.util.ArrayList;

public class SonOfMedell extends CardAbility {

    @Override
    public void applyAbility() {
        Row opponentRangedArea = getCard().getPlayer().getBoard().getOpponentRangedArea();

        ArrayList<Card> scorchedCards = new ArrayList<>();

        if (opponentRangedArea.calculateRowTotalPower() >= 10) {
            int highestCardPower = opponentRangedArea.getHighestCardPowerInRow();

            for (Card card : opponentRangedArea.getCardsOnSection()) {
                if (card instanceof UnitCard) {
                    if (((UnitCard) card).getCurrentPower() == highestCardPower && !((UnitCard) card).isHero()) {
                        scorchedCards.add(card);
                    }
                }
            }

            for (Card card : scorchedCards) {
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
                pauseTransition.setOnFinished(event -> {
                    card.getPlayer().getGameController().
                            putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), false);
                });
                pauseTransition.play();

                new FadingAnimation(card.getRowCard(), "anim_scorch").play();

                Client.client.sendSfxAnimationPackage(card, "scorch", "anim_scorch");
            }
        }

        playSfx("scorch");

        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    @Override
    public CardAbility deepCopy() {
        return new SonOfMedell();
    }

    @Override
    public void writeDescription() {
        setDescription("Son Of Medell:\nDestroy your enemy's strongest Ranged Combat unit(s)" +
                    " if the combined strength of all his or her Ranged Combat units is 10 or more.");
    }
}
