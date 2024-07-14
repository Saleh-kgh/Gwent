package Model.InGameObjects.Abilities.LeaderAbilities.Foltest;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.CardFactory;
import Enum.CardStates;
import Enum.CardNames;
import controller.Client;

public class TheSiegeMaster extends CardAbility {

    @Override
    public void applyAbility() {
        if (!getCard().getPlayer().getBoard().getFriendlySiegeArea().getHornArea().isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Card hornCard = CardFactory.getCopiedCardByName(CardNames.HORN);
        hornCard.createGraphicalFields();
        playSiegeCommanderHorn(hornCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playSiegeCommanderHorn(Card card) {
        card.setOwner(getCard().getOwner());
        card.setPlayer(getCard().getPlayer());
        card.setPreviousState(CardStates.ROW_HIDDEN);

        card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getFriendlyDeckArea());
        card.getRowCard().setLayoutX(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutX());
        card.getRowCard().setLayoutY(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutY());

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getFriendlySiegeArea().getHornArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.setId(8888);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getFriendlySiegeArea(), CardStates.ROW_IN_GAME_PLAYED);

        playSfx("horn");

        Client.client.sendSfxAnimationPackage(card, "horn", "non");

        getCard().getPlayer().getBoard().getFriendlySiegeArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new TheSiegeMaster();
    }

    @Override
    public void writeDescription() {
        setDescription("The Siege Master:\nDoubles the strength of all your Siege units" +
                     " (unless a Commander's Horn is also present on that row).");
    }
}
