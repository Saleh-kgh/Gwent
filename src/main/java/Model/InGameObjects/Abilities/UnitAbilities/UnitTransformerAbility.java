package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardStates;
import Enum.CardNames;
import controller.Client;

public class UnitTransformerAbility extends CardAbility {

    private UnitCard transformedCard;

    public UnitTransformerAbility(UnitCard transformedCard) {
        this.transformedCard = transformedCard;
    }

    public UnitCard getTransformedCard() {
        return transformedCard;
    }

    @Override
    public void applyAbility() {
        UnitCard transformedCopy = (UnitCard) transformedCard.deepCopy();
        transformedCopy.createGraphicalFields();

        UnitCard unitCard = (UnitCard) getCard();

        transformedCopy.setPreviousState(CardStates.ROW_HIDDEN);
        transformedCopy.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
        transformedCopy.setOwner(unitCard.getOwner());
        transformedCopy.setCurrentPlaceInGame(unitCard.getPlayer().getBoard().getFriendlyCloseArea());
        transformedCopy.setPlayer(getCard().getPlayer());

        if (transformedCopy.getCardName().equals(CardNames.KAMBI_TRANSFORMED))
            transformedCopy.setId(6666);
        else
            transformedCopy.setId(6660);

        ((Row)transformedCopy.getCurrentPlaceInGame()).addToUnitCardsToPutNextRound(transformedCopy);

        Client.client.sendAddCardPackage(transformedCopy, transformedCopy.getCurrentPlaceInGame(),
                        CardStates.ROW_IN_GAME_PLAYED, true);
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitTransformerAbility((UnitCard) transformedCard.deepCopy());
    }

    @Override
    public void writeDescription() {
        setDescription("Transformer:\nWhen this card is removed from the battlefield, it summons a powerful new Unit Card to take its place.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_avenger.png";
    }
}
