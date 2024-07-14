package view.Controller.ClickEvaluator;

import Model.Game.Player;
import javafx.scene.Node;

public abstract class ClickEvaluator {

    private final Player player;

    public ClickEvaluator(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void manageClickCommand(Node node) {
        if (getPlayer().isParalyzedByAnimation())
            return;

        switch (getPlayer().getPlayerState()) {
            case PARALYZED, PASSED, WAITING_FOR_OPPONENT:
                return;
            case CHOOSING_CARD_FROM_HAND:
                manageOnChoosingFromHand(node);
                break;
            case CHOOSING_ROW_TO_PUT:
                manageOnChoosingRowToPut(node);
                break;
            case LOOKING_AT_CARDS:
                manageOnLookingAtCards(node);
                break;
            case CHOOSING_SCROLL_BOX_CARD:
                manageOnChoosingScrollBoxCard(node);
                break;
            case SWAPPING_INITIAL_HAND:
                manageOnSwappingInitialHand(node);
                break;
            case LEADER_CHOOSING_CARD:
                manageOnLeaderChoosingCards(node);
                break;
            case LEADER_LOOKING_CARDS:
                manageOnLeaderLookingCards(node);
        }
    }

    abstract void manageOnChoosingFromHand(Node node);

    abstract void manageOnChoosingRowToPut(Node node);

    abstract void manageOnLookingAtCards(Node node);

    abstract void manageOnChoosingScrollBoxCard(Node node);

    abstract void manageOnSwappingInitialHand(Node node);

    abstract void manageOnLeaderChoosingCards(Node node);

    abstract void manageOnLeaderLookingCards(Node node);
}
