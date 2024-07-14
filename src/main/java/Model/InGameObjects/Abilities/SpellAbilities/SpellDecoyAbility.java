package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import Enum.CardStates;

public class SpellDecoyAbility extends CardAbility {

    private Card cardToReplace;

    public void swapDecoyWithCard(Card card) {
        this.cardToReplace = card;
        applyAbility();
    }

    @Override
    public void applyAbility() {
        Card tempCard = cardToReplace.deepCopy();

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(10));
        pauseTransition.setOnFinished(event -> {
            ((UnitCard) cardToReplace).setCurrentPower(((UnitCard) cardToReplace).getBasePower());
            cardToReplace.getRowCard().setPowerLabelText();

            getCard().getPlayer().getGameController().moveCardProtocol(
                    cardToReplace, tempCard.getCurrentPlaceInGame(), getCard().getPlayer().getBoard().getHandArea(),
                    CardStates.ROW_IN_GAME_HAND);
            Client.client.sendMoveCardPackage(cardToReplace, tempCard.getCurrentPlaceInGame(), getCard().getPlayer().getBoard().getHandArea(),
                    CardStates.ROW_IN_GAME_HAND);

            getCard().getPlayer().getGameController().moveCardProtocol(
                    getCard(), getCard().getPlayer().getBoard().getHandArea(), tempCard.getCurrentPlaceInGame(),
                    CardStates.ROW_IN_GAME_PLAYED);
            Client.client.sendMoveCardPackage(getCard(), getCard().getPlayer().getBoard().getHandArea(), tempCard.getCurrentPlaceInGame(),
                    CardStates.ROW_IN_GAME_PLAYED);

            getCard().getPlayer().getBoard().updateAllSections();
        });
        pauseTransition.play();

        playSfx("seize");
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellDecoyAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Decoy:\nSwap with a card on the battlefield to return it to your hand.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_special_decoy.png";
    }
}
