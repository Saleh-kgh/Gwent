package view.Controller.ClickEvaluator;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.LeaderCardArea;
import Model.Game.Player;
import Model.InGameObjects.Cards.Card;
import javafx.scene.Node;
import Enum.CardPlace;
import Enum.PlayerState;

public class BoardSectionClickEvaluator extends ClickEvaluator {

    public BoardSectionClickEvaluator(Player player) {
        super(player);
    }

    @Override
    void manageOnChoosingFromHand(Node node) {
        BoardSection boardSection = (BoardSection) node;
        if (canBoardSectionCardsBeShown(boardSection)) {

            if (boardSection.getCardsOnSection().isEmpty())
                return;

            getPlayer().getGameController().showArrayOfCards(boardSection.getCardsOnSection());
        }
        else if (boardSection.getCardPlace().equals(CardPlace.FRIENDLY_LEADER)) {
            Card leaderCard = boardSection.getCardsOnSection().get(0);
            getPlayer().getGameController().selectCard(leaderCard);
        }
    }

    @Override
    void manageOnChoosingRowToPut(Node node) {
        if (node instanceof LeaderCardArea) {
            Card leaderCard = ((LeaderCardArea) node).getCardsOnSection().get(0);
            getPlayer().getGameController().selectCard(leaderCard);
        }
        else {
            getPlayer().getGameController().playACardFromHand((BoardSection) node);
        }
    }

    @Override
    void manageOnLookingAtCards(Node node) {
        getPlayer().getGameController().stopPlayerFromLookingAtCards();
    }

    @Override
    void manageOnChoosingScrollBoxCard(Node node) {
        return;
    }

    @Override
    void manageOnSwappingInitialHand(Node node) {
        getPlayer().getGameController().stopSwappingInitialHand();
    }

    @Override
    void manageOnLeaderChoosingCards(Node node) {
        return;
    }

    @Override
    void manageOnLeaderLookingCards(Node node) {
        return;
    }

    private boolean canBoardSectionCardsBeShown(BoardSection boardSection) { // because hand, decks and leader cards shouldn't bring
        return !boardSection.getCardPlace().equals(CardPlace.HAND_AREA) &&      // up the scroll box for showing cards.
                !boardSection.getCardPlace().equals(CardPlace.FRIENDLY_DECK_AREA) &&
                !boardSection.getCardPlace().equals(CardPlace.OPPONENT_DECK_AREA) &&
                !boardSection.getCardPlace().equals(CardPlace.FRIENDLY_LEADER);
    }
}
