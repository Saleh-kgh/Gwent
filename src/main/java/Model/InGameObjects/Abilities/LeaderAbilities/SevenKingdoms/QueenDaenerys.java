package Model.InGameObjects.Abilities.LeaderAbilities.SevenKingdoms;

import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.FadingAnimation;

import java.util.ArrayList;

public class QueenDaenerys extends CardAbility {

    @Override
    public void applyAbility() {
        Row opponentCloseArea = getCard().getPlayer().getBoard().getOpponentCloseArea();

        ArrayList<Card> scorchedCards = new ArrayList<>();

        if (opponentCloseArea.calculateRowTotalPower() >= 10) {
            int highestCardPower = opponentCloseArea.getHighestCardPowerInRow();

            for (Card card : opponentCloseArea.getCardsOnSection()) {
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
        return new QueenDaenerys();
    }

    @Override
    public void writeDescription() {
        setDescription("Queen Daenerys:\nDestroys the strongest enemy close unit if combined strength of its row is above 10.");
    }
}
