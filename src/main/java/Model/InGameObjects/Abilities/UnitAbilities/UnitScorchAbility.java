package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;

import java.util.ArrayList;

import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.FadingAnimation;

public class UnitScorchAbility extends CardAbility {


    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();
        Row opponentRow = unitCard.getRow().getOpponentSameKindRow();

        ArrayList<Card> scorchedCards = new ArrayList<>();

        if (opponentRow.calculateRowTotalPower() >= 10) {
            int highestCardPower = opponentRow.getHighestCardPowerInRow();

            for (Card card : opponentRow.getCardsOnSection()) {
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
            playSfx("scorch");
        }
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitScorchAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Scorch:\nKills the strongest card(s) on its same row if combined strength of all cards on that row is more than 10.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_scorch.png";
    } // fix  it later
}
