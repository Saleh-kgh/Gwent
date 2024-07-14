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

public class TheSteelForged extends CardAbility {

    @Override
    public void applyAbility() {
        Row opponentSiegeArea = getCard().getPlayer().getBoard().getOpponentSiegeArea();

        ArrayList<Card> scorchedCards = new ArrayList<>();

        if (opponentSiegeArea.calculateRowTotalPower() >= 10) {
            int highestCardPower = opponentSiegeArea.getHighestCardPowerInRow();

            for (Card card : opponentSiegeArea.getCardsOnSection()) {
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
                playSfx("scorch");

                Client.client.sendSfxAnimationPackage(card, "scorch", "anim_scorch");
            }
        }
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    @Override
    public CardAbility deepCopy() {
        return new TheSteelForged();
    }

    @Override
    public void writeDescription() {
        setDescription("The Steel Forged:\nDestroy your enemy's strongest Siege " +
                     "unit(s) if the combined strength of all his or her Siege units is 10 or more.");
    }
}
