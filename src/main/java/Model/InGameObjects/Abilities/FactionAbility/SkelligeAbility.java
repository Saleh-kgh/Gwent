package Model.InGameObjects.Abilities.FactionAbility;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.UnitCard;

import java.util.ArrayList;
import java.util.Random;
import Enum.CardStates;
import Model.Game.Player;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class SkelligeAbility extends FactionAbility {

    public SkelligeAbility(Player player, Player owner) {
        super(player, owner);
        setCancelled(false);
    }

    @Override
    public boolean canAbilityBeTriggered() {
        return getPlayer().getGameObject().getRoundNumber() == 2 && !isCancelled();
    }

    @Override
    public void applyAbility(int duration) {
        if(isCancelled())
            return;

        if (getPlayer().equals(getOwner())) {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {
                if (isCancelled())
                    return;

                getPlayer().getGameObject().getGameViewController().displayNotifBar("Skellige Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_skellige.png");

                ArrayList<UnitCard> unitCards = new ArrayList<>();

                Random random = new Random();

                for (int i = 0; i < 2 && i < getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection().size(); ) {
                    Card card = getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection().get(
                            random.nextInt(getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection().size())
                    );

                    if (card instanceof UnitCard && !unitCards.contains(card)) {
                        unitCards.add((UnitCard) card);

                        getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                                getPlayer().getGameController().getCardBoardSection(card), CardStates.ROW_IN_GAME_PLAYED);

                        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                                getPlayer().getGameController().getCardBoardSection(card), CardStates.ROW_IN_GAME_PLAYED);

                        i++;
                    }
                }
            });
            pauseTransition.play();
        }
        else {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {
                getPlayer().getGameObject().getGameViewController().displayNotifBar("Skellige Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_skellige.png");
            });
            pauseTransition.play();
        }
    }
}
