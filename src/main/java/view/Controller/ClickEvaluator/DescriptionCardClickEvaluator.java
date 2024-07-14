package view.Controller.ClickEvaluator;

import Model.InGameObjects.Abilities.LeaderAbilities.SevenKingdoms.QueenCersei;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.DescriptionCard;
import Model.InGameObjects.Abilities.LeaderAbilities.Eredin.DestroyerOfWorlds;
import Model.InGameObjects.Abilities.LeaderAbilities.Eredin.KingOfTheWildHunt;
import Model.InGameObjects.Cards.LeaderCard;
import Model.Game.Player;
import javafx.scene.Node;
import Enum.CardStates;
import Enum.CardNames;

public class DescriptionCardClickEvaluator extends ClickEvaluator {

    public DescriptionCardClickEvaluator(Player player) {
        super(player);
    }

    @Override
    void manageOnChoosingFromHand(Node node) {
        return;
    }

    @Override
    void manageOnChoosingRowToPut(Node node) {
        return;
    }

    @Override
    void manageOnLookingAtCards(Node node) {
        getPlayer().getGameController().stopPlayerFromLookingAtCards(); // i changed it recently with leaderAbilityScrollBox display remove
    }

    @Override
    void manageOnChoosingScrollBoxCard(Node node) {
        DescriptionCard descriptionCard = (DescriptionCard) node;
        if (!descriptionCard.getCard().getCurrentState().equals(CardStates.DES_IN_GAME_SELECTABLE)) {
            return;
        }
        getPlayer().getGameController().scrollBoxReviveDiscardedCard(descriptionCard.getCard());
    }

    @Override
    void manageOnSwappingInitialHand(Node node) {
        Card card = ((DescriptionCard) node).getCard();
        getPlayer().getGameController().swapInitialHandCard(card);
    }

    @Override
    void manageOnLeaderChoosingCards(Node node) {
        Card card = ((DescriptionCard)node).getCard();
        LeaderCard leaderCard = getPlayer().getLeaderCard();

        if (leaderCard.getCardName().equals(CardNames.EREDIN_DESTROYER_OF_WORLDS)) {
            ((DestroyerOfWorlds)leaderCard.getCardAbilities().get(0)).drawCardFromDeck(card);
        }

        else if (leaderCard.getCardName().equals(CardNames.EREDIN_KING_OF_WILDHUNT)) {
            ((KingOfTheWildHunt)leaderCard.getCardAbilities().get(0)).playWeatherCard(card);
        }

        else if (leaderCard.getCardName().equals(CardNames.CERSEI_LANNISTER)) {
            ((QueenCersei)leaderCard.getCardAbilities().get(0)).drawCardFromDeck(card);
        }

        getPlayer().getGameController().stopShowingScrollBoxOfLeaderAbility();
    }

    @Override
    void manageOnLeaderLookingCards(Node node) {
        getPlayer().getGameController().stopShowingScrollBoxOfLeaderAbility();
    }
}
