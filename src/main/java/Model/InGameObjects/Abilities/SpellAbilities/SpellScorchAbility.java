package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.SpellCard;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardPlace;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.FadingAnimation;

import java.util.ArrayList;

public class SpellScorchAbility extends CardAbility {

    @Override
    public void applyAbility() {
        SpellCard spellCard = (SpellCard) getCard();

        int highestPower = 0;

        ArrayList<Card> allUnitCards = new ArrayList<>();

        for (BoardSection boardSection : getCard().getPlayer().getBoard().getBoardSections()) {
            if (!(boardSection instanceof Row))
                continue;

            for (Card card : boardSection.getCardsOnSection()) {
                if (card instanceof UnitCard) {
                    if (((UnitCard) card).isHero())
                        continue;

                    allUnitCards.add(card);
                    highestPower = Math.max(highestPower, ((UnitCard) card).getCurrentPower());
                }
            }
        }

        ArrayList<Card> scorchedCards = new ArrayList<>();

        for (Card card : allUnitCards) {
            if (((UnitCard)card).getCurrentPower() == highestPower)
                scorchedCards.add(card);
        }

        for (Card card : scorchedCards) {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
            pauseTransition.setOnFinished(event -> {
                card.getPlayer().getGameController().
                        putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), false);
            });
            pauseTransition.play();
            new FadingAnimation(card.getRowCard(), "anim_scorch").play();
            Client.client.sendSfxAnimationPackage(card,  "scorch", "anim_scorch");
        }

        playSfx("scorch");
    }

    private boolean isCardInFriendlySections(Card card) {
        if ((card.getCardPlace().equals(CardPlace.OPPONENT_CLOSE) ||
                card.getCardPlace().equals(CardPlace.OPPONENT_RANGED) ||
                card.getCardPlace().equals(CardPlace.OPPONENT_SIEGE)) &&
                !card.getOwner().equals(getCard().getOwner()))

            return true;

        else if (card.getOwner().equals(getCard().getOwner()))
            return true;

        return false;
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellScorchAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Scorch:\nDiscards after playing. Kills the strongest card(s) on the battlefield.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_special_scorch.png";
    }
}
