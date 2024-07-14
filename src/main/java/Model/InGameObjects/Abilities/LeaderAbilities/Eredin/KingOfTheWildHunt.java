package Model.InGameObjects.Abilities.LeaderAbilities.Eredin;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.LeaderCard;
import Enum.CardNames;
import Enum.PlayerState;
import Enum.CardStates;
import Model.InGameObjects.Cards.SpellCard;
import controller.Client;

import java.util.ArrayList;
import java.util.Random;

public class KingOfTheWildHunt extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<Card> weatherCards = new ArrayList<>();

        boolean isWeatherCardAvailable = false;

        for (Card card : getCard().getPlayer().getBoard().getFriendlyDeckArea().getCardsOnSection()) {
            if (card instanceof SpellCard) {
                if (card.getCardName().equals(CardNames.RAIN) ||
                        card.getCardName().equals(CardNames.FROST) ||
                        card.getCardName().equals(CardNames.FOG) ||
                        card.getCardName().equals(CardNames.STORM) ||
                        card.getCardName().equals(CardNames.CLEAR_WEATHER)) {

                    card.setCurrentState(CardStates.DES_IN_GAME_SELECTABLE);
                    weatherCards.add(card);
                    card.createGraphicalFields();
                    isWeatherCardAvailable = true;
                }
            }
        }

        if (!isWeatherCardAvailable) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        if (!isLeaderEmhyrInvaderOfNorth()) {
            getCard().getPlayer().setPlayerState(PlayerState.LEADER_CHOOSING_CARD);

            getCard().getPlayer().getGameController().showChooseScrollBox(weatherCards);
        }
        else {
            Random random = new Random();
            Card card = weatherCards.get(random.nextInt(weatherCards.size()));
            card.createGraphicalFields();
            playWeatherCard(card);
            getCard().getPlayer().getGameController().finishLeaderAbility();
        }
    }

    private boolean isLeaderEmhyrInvaderOfNorth() {
        return getCard().getPlayer().getOpponent().getLeaderCard().getCardName().
                                            equals(CardNames.EMHYR_INVADER_OF_THE_NORTH);
    }

    public void playWeatherCard(Card card) {
        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.applyAbility();

        getCard().getPlayer().getBoard().getWeatherCardArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new KingOfTheWildHunt();
    }

    @Override
    public void writeDescription() {
        setDescription("King Of The Wild Hunt:\nPick any weather card from your deck and play it instantly.");
    }
}
