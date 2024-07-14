package controller;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.LeaderCard;
import Model.User.User;
import Model.User.UserFaction;

public class PreGameSetDeckController {


    public void addToDeckCard(User user, Card card) {
        user.getCardInventory().getCurrentFaction().addCardToDeck(card);
    }
    public void addToStorageCard(User user, Card card) {
        user.getCardInventory().getCurrentFaction().addCardToStorage(card);
    }
    public void removeCardFromDeck(User user, Card card) {
        user.getCardInventory().getCurrentFaction().removeCardFromDeck(card);
    }
    public void removeCardFromStorage(User user, Card card) {
        user.getCardInventory().getCurrentFaction().removeCardFromStorage(card);
    }
    public void setFaction(User user, UserFaction userFaction) {
        user.getCardInventory().setCurrentFaction(userFaction);
    }
    public void setLeaderFaction(User user, LeaderCard leaderCard) {
        user.getCardInventory().getCurrentFaction().setCurrentLeader(leaderCard);
    }
}
