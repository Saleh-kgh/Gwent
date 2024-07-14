package Model.InGameObjects.Abilities.FactionAbility;

import Model.InGameObjects.Cards.Card;
import Enum.CardStates;
import Model.Game.Player;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class NorthernRealmsAbility extends FactionAbility {

    public NorthernRealmsAbility(Player player, Player owner) {
        super(player, owner);
        setCancelled(false);
    }

    @Override
   public boolean canAbilityBeTriggered() {

        if (getPlayer().getDeck().isEmpty())
            return false;

        if (getPlayer().equals(getOwner()))
            return getPlayer().getBoard().getFriendlyTotalScoreArea().getTotalScore() >
                    getPlayer().getBoard().getOpponentTotalScoreArea().getTotalScore() && !isCancelled();
        else
            return getPlayer().getBoard().getFriendlyTotalScoreArea().getTotalScore() <
                    getPlayer().getBoard().getOpponentTotalScoreArea().getTotalScore() && !isCancelled();
    }

    @Override
    public void applyAbility(int duration) {

        if (getPlayer().equals(getOwner())) {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {

                getPlayer().getGameObject().getGameViewController().displayNotifBar("Northern Realms Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_north.png");
                Card card = getPlayer().getOneRandomCardFromDeck();
                card.createGraphicalFields();

                getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                        getPlayer().getBoard().getHandArea(), CardStates.ROW_IN_GAME_HAND);

                Client.client.sendAddCardPackage(card, getPlayer().getBoard().getHandArea(),
                                CardStates.ROW_IN_GAME_HAND, false);
            });
            pauseTransition.play();
        }
        else {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(duration));
            pauseTransition.setOnFinished(event -> {
                getPlayer().getGameObject().getGameViewController().displayNotifBar("Northern Realms Ability Triggered!");
                getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_north.png");
            });
            pauseTransition.play();
        }
    }
}
