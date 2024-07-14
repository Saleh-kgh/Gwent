package Model.InGameObjects.Abilities.LeaderAbilities.Francesca;

import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Enum.CardPlace;
import Enum.CardStates;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;

import java.util.ArrayList;

public class HopeOfTheAenSeidhe extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<UnitCard> agileCards = new ArrayList<>();

        for (Card card : getCard().getPlayer().getBoard().getFriendlyCloseArea().getCardsOnSection()) {
            if (card.getCardPlace().equals(CardPlace.FRIENDLY_CLOSE_RANGED))
                agileCards.add((UnitCard) card);
        }
        for (Card card : getCard().getPlayer().getBoard().getFriendlyRangedArea().getCardsOnSection()) {
            if (card.getCardPlace().equals(CardPlace.FRIENDLY_CLOSE_RANGED))
                agileCards.add((UnitCard)card);
        }

        int closeRowTotalPower = 0;
        int rangedRowTotalPower = 0;

        Row closeRow = getCard().getPlayer().getBoard().getFriendlyCloseArea();
        Row rangedRow = getCard().getPlayer().getBoard().getFriendlyRangedArea();

        closeRowTotalPower = calculateTotalPowerOfRow(closeRow, agileCards);
        rangedRowTotalPower = calculateTotalPowerOfRow(rangedRow, agileCards);

        if (closeRowTotalPower > rangedRowTotalPower) moveCardsToCloseRow(agileCards);
        else moveCardsToRangedRow(agileCards);

        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    private int calculateTotalPowerOfRow(Row row, ArrayList<UnitCard> cards) {
        int totalPower = 0;
        int cardPower = 0;

        for (UnitCard card : cards) {
            cardPower = row.isWeatherBad() ? 1 : card.getBasePower();
            cardPower = (row.isCommanderHornUnitOnRow() || row.isRowCommanderHorned()) ? cardPower * 2 : cardPower;
            cardPower += row.getMoraleBoost();

            totalPower += cardPower;
        }

        return totalPower;
    }

    private void moveCardsToCloseRow(ArrayList<UnitCard> agileCards) {
        for (UnitCard card : agileCards) {
            if (card.getCurrentPlaceInGame().getCardPlace().equals(CardPlace.FRIENDLY_RANGED)) {
                getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                        getCard().getPlayer().getBoard().getFriendlyCloseArea(), CardStates.ROW_IN_GAME_PLAYED);

                Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                        getCard().getPlayer().getBoard().getFriendlyCloseArea(), CardStates.ROW_IN_GAME_PLAYED);
            }
        }
    }

    private void moveCardsToRangedRow(ArrayList<UnitCard> agileCards) {
        for (UnitCard card : agileCards) {
            if (card.getCurrentPlaceInGame().getCardPlace().equals(CardPlace.FRIENDLY_CLOSE)) {
                getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                        getCard().getPlayer().getBoard().getFriendlyRangedArea(), CardStates.ROW_IN_GAME_PLAYED);

                Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                        getCard().getPlayer().getBoard().getFriendlyRangedArea(), CardStates.ROW_IN_GAME_PLAYED);
            }
        }
    }

    @Override
    public CardAbility deepCopy() {
        return new HopeOfTheAenSeidhe();
    }

    @Override
    public void writeDescription() {
        setDescription("Hope of the Aen Seidhe:\nMove agile units to whichever" +
                    " valid row maximizes their strength (don't move units already in optimal row).");
    }
}
