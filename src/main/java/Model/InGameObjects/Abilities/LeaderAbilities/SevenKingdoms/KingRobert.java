package Model.InGameObjects.Abilities.LeaderAbilities.SevenKingdoms;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.CardFactory;
import controller.Client;
import Enum.CardNames;
import Enum.CardStates;

public class KingRobert extends CardAbility {

    @Override
    public void applyAbility() {
        if (!getCard().getPlayer().getBoard().getFriendlyCloseArea().getHornArea().isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Card hornCard = CardFactory.getCopiedCardByName(CardNames.HORN);
        hornCard.createGraphicalFields();
        playCloseCommanderHorn(hornCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playCloseCommanderHorn(Card card) {
        card.setOwner(getCard().getOwner());
        card.setPlayer(getCard().getPlayer());
        card.setPreviousState(CardStates.ROW_HIDDEN);

        card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getFriendlyDeckArea());
        card.getRowCard().setLayoutX(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutX());
        card.getRowCard().setLayoutY(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutY());

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getFriendlyCloseArea().getHornArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.setId(8888); // agreed id for extra commander horn

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getFriendlyCloseArea(), CardStates.ROW_IN_GAME_PLAYED);

        playSfx("horn");

        Client.client.sendSfxAnimationPackage(card, "horn", "non");

        getCard().getPlayer().getBoard().getFriendlyCloseArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new KingRobert();
    }

    @Override
    public void writeDescription() {
        setDescription("King Robert:\nDoubles strength of all close combat units if a commander horn is not present.");
    }
}
