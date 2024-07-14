package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardStates;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Random;

public class UnitSpyAbility extends CardAbility {


    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();

        int numberOfCardsInDeck = unitCard.getPlayer().getDeck().size() - 1;
        Random random = new Random();
        int cardIndex = 0;

        for (int i = 0; i < 2 && numberOfCardsInDeck >= 0; i++) {
            cardIndex = random.nextInt(numberOfCardsInDeck--);
            Card card = unitCard.getPlayer().getDeck().get(cardIndex);
            card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getFriendlyDeckArea());
            card.setPreviousState(CardStates.ROW_IN_GAME_HAND);
            card.createGraphicalFields();

            card.getPlayer().getGameController().moveCardProtocol(card, card.getPlayer().getBoard().getFriendlyDeckArea(),
                            card.getPlayer().getBoard().getHandArea(), CardStates.ROW_IN_GAME_HAND);

            Client.client.sendAddCardPackage(card, card.getPlayer().getBoard().getHandArea(),
                        CardStates.ROW_IN_GAME_HAND, false);

            playSfx("spy");

            playAnimation("spy");

            Client.client.sendSfxAnimationPackage(getCard(), "spy", "spy");

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
            pauseTransition.setOnFinished(event -> {
                playSfx("seize");
            });
            pauseTransition.play();
        }
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitSpyAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Spy:\nPlace on your opponent's battlefield (counts towards opponent's total) and draw 2 cards from your deck.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_spy.png";
    }
}
