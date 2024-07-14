package Model.InGameObjects.Abilities.FactionAbility;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardStates;
import java.util.ArrayList;
import java.util.Random;
import Enum.CardNames;
import Model.Game.Player;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MonstersAbility extends FactionAbility {

    public MonstersAbility(Player player, Player owner) {
        super(player, owner);
        setCancelled(false);
    }

    @Override
    public boolean canAbilityBeTriggered() {
        return !allFriendlyUnitCards().isEmpty() && !isCancelled();
    }

    @Override
    public void applyAbility(int duration) {
        if (isCancelled())
            return;

        if (getPlayer().equals(getOwner())) {
            Random random = new Random();

            ArrayList<Card> allFieldCards = allFriendlyUnitCards();

            Card card = allFieldCards.get(random.nextInt(allFieldCards.size()));

            ((Row) card.getCurrentPlaceInGame()).addToUnitCardsToPutNextRound((UnitCard) card);

            BoardSection boardSection = card.getCurrentPlaceInGame();

            Client.client.sendAddCardPackage(card, boardSection, CardStates.ROW_IN_GAME_PLAYED, true);

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {
                if (isCancelled())
                    return;

                getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection().remove(card);
                getPlayer().getBoard().getFriendlyDiscardPileArea().updateSection();

                Client.client.sendRemoveCardPackage(card, getPlayer().getBoard().getFriendlyDiscardPileArea(),
                                CardStates.ROW_IN_GAME_PLAYED, false);

                getPlayer().getGameObject().getGameViewController().displayNotifBar("Monsters Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_monsters.png");
            });
            pauseTransition.play();
        }
        else {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {
                getPlayer().getGameObject().getGameViewController().displayNotifBar("Monsters Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_monsters.png");
            });
            pauseTransition.play();
        }
    }

    private ArrayList<Card> allFriendlyUnitCards() {
        ArrayList<Card> allFieldCards = new ArrayList<>();

        for (Card card : getPlayer().getBoard().getFriendlyCloseArea().getCardsOnSection()) {
            if (!card.getCardName().equals(CardNames.DECOY))
                allFieldCards.add(card);
        }
        for (Card card : getPlayer().getBoard().getFriendlyRangedArea().getCardsOnSection()) {
            if (!card.getCardName().equals(CardNames.DECOY))
                allFieldCards.add(card);
        }
        for (Card card : getPlayer().getBoard().getFriendlySiegeArea().getCardsOnSection()) {
            if (!card.getCardName().equals(CardNames.DECOY))
                allFieldCards.add(card);
        }

        return allFieldCards;
    }
}
