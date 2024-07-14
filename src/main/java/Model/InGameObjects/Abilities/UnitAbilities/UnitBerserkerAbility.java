package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardStates;
import controller.Client;
import Enum.CardNames;

public class UnitBerserkerAbility extends CardAbility {

    private UnitCard mardroemedCard;

    public UnitBerserkerAbility(UnitCard mardroemedCard) {
        this.mardroemedCard = mardroemedCard;
    }

    public UnitCard getMardroemedCard() {
        return mardroemedCard;
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitBerserkerAbility((UnitCard) mardroemedCard.deepCopy());
    }

    @Override
    public void writeDescription() {
        setDescription("Berserker:\nTransforms into a bear when a Mardroeme card is on its row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_berserker.png";
    }

    @Override
    public void applyAbility() {

        UnitCard unitCard = (UnitCard) getCard();

        if (!unitCard.getRow().doesHaveMardroeme()) {
            return;
        }

        UnitCard mardroemedCopy = (UnitCard) mardroemedCard.deepCopy();
        mardroemedCopy.createGraphicalFields();

        BoardSection boardSection = unitCard.getCurrentPlaceInGame();

        unitCard.getPlayer().getGameController().
                putFriendlyCardInDiscardPile(unitCard, boardSection, false);

        Client.client.sendRemoveCardPackage(unitCard, boardSection, CardStates.ROW_HIDDEN, false);

        mardroemedCopy.setPreviousState(CardStates.ROW_HIDDEN);
        mardroemedCopy.setOwner(unitCard.getOwner());
        mardroemedCopy.setPlayer(getCard().getPlayer());

        if (mardroemedCopy.getCardName().equals(CardNames.BERSERKER_MARDOREMED))
            mardroemedCopy.setId(9999); // agreed id for added berserker
        else
            mardroemedCopy.setId(9990); // agreed id for added young berserker

        unitCard.getPlayer().getGameController().addCardToBoardSection(mardroemedCopy,
                                boardSection);

        unitCard.getPlayer().getGameController().applyCardAbilityWithDelay(mardroemedCopy,
                boardSection);

        Client.client.sendAddCardPackage(mardroemedCopy, boardSection, CardStates.ROW_IN_GAME_PLAYED, false);

        if (unitCard.getCardName().equals(CardNames.BERSERKER)) {
            Client.client.sendSfxAnimationPackage(unitCard, "moral", "morale");
        }
        else {
            Client.client.sendSfxAnimationPackage(unitCard, "ally", "bond");
        }
    }
}
