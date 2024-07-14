package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import Enum.CardNames;
import Enum.CardMusterTypes;
import java.util.HashMap;
import Enum.CardStates;
import controller.Client;

public class UnitMusterAbility extends CardAbility {

    private CardMusterTypes cardMusterType;

    public UnitMusterAbility(CardMusterTypes cardMusterType) {
        this.cardMusterType = cardMusterType;
    }

    public CardMusterTypes getCardMusterType() {
        return cardMusterType;
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitMusterAbility(cardMusterType);
    }

    @Override
    public void writeDescription() {
        setDescription("Muster:\nFind any cards with the same name in your deck and play them instantly.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_muster.png";
    }




    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();
        CardNames cardName;

        if (unitCard.getCardName().equals(CardNames.CERYS)) {
            cardName = CardNames.SHIELD_MAIDEN_1;
            placeMusterGroupByCardName(cardName);

            cardName = CardNames.SHIELD_MAIDEN_2;
            placeMusterGroupByCardName(cardName);

            cardName = CardNames.SHIELD_MAIDEN_3;
            placeMusterGroupByCardName(cardName);
        }
        else if (unitCard.getCardName().equals(CardNames.GAUNTER_ODIMM)) {
            placeMusterGroupByMusterName(CardMusterTypes.GAUNTER_ODIMM_DARKNESS, unitCard);
        }
        else {
            placeMusterGroupByMusterName(cardMusterType, unitCard);
        }

        playSfx("ally");
        playAnimation("muster");

        Client.client.sendSfxAnimationPackage(getCard(), "ally", "muster");
    }

    private void placeMusterGroupByCardName(CardNames cardName) {
        Card card = null;

        for (Card deckCard : getCard().getPlayer().getDeck()) {
            if (deckCard.getCardName().equals(cardName)) {
                card = deckCard;
                deckCard.createGraphicalFields();
                break;
            }
        }

        for (Card handCard : getCard().getPlayer().getHand()) {
            if (handCard.getCardName().equals(cardName)) {
                card = handCard;
                break;
            }
        }

        putMusterCardInField(card);
    }

    private void putMusterCardInField(Card card) {
        BoardSection boardSection = null;
        for (BoardSection boardSection1 : getCard().getPlayer().
                getBoard().getBoardSections()) {
            if (boardSection1.getCardPlace().equals(card.getCardPlace())) {
                boardSection = boardSection1;
                break;
            }
        }
        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                        boardSection, CardStates.ROW_IN_GAME_PLAYED);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                boardSection, CardStates.ROW_IN_GAME_PLAYED);
    }

    private void placeMusterGroupByMusterName(CardMusterTypes cardMusterType, UnitCard unitCard) {

        HashMap<Card, BoardSection> deckCards = new HashMap<>();
        HashMap<Card, BoardSection> handCards = new HashMap<>();
        BoardSection boardSection = null;

        for (Card card : unitCard.getPlayer().getDeck()) {

            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitMusterAbility) {
                    if (((UnitMusterAbility) cardAbility).getCardMusterType().equals(cardMusterType)) {
                        deckCards.put(card, getCard().getPlayer().getGameController().getCardBoardSection(card));
                        break;
                    }
                }
            }
        }

        for (Card card : unitCard.getPlayer().getHand()) {

            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitMusterAbility) {
                    if (((UnitMusterAbility) cardAbility).getCardMusterType().equals(cardMusterType)) {
                        handCards.put(card, getCard().getPlayer().getGameController().getCardBoardSection(card));
                        break;
                    }
                }
            }
        }

        if (!deckCards.isEmpty()) {
            for (Card card : deckCards.keySet()) {
                card.createGraphicalFields();
                card.getPlayer().getGameController().moveCardProtocol(card, card.getPlayer().getBoard().getFriendlyDeckArea(),
                        deckCards.get(card), CardStates.ROW_IN_GAME_PLAYED);
                Client.client.sendMoveCardPackage(card, card.getPlayer().getBoard().getFriendlyDeckArea(),
                        deckCards.get(card), CardStates.ROW_IN_GAME_PLAYED);
            }
        }

        if (!handCards.isEmpty()) {
            for (Card card : handCards.keySet()) {
                card.createGraphicalFields();
                card.getPlayer().getGameController().moveCardProtocol(card, card.getPlayer().getBoard().getHandArea(),
                        handCards.get(card), CardStates.ROW_IN_GAME_PLAYED);
                Client.client.sendMoveCardPackage(card, card.getPlayer().getBoard().getHandArea(),
                        handCards.get(card), CardStates.ROW_IN_GAME_PLAYED);
            }
        }
    }
}
