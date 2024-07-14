package Model.User;

import Model.InGameObjects.Cards.Card;

import java.io.Serializable;
import java.util.ArrayList;
import Enum.FactionName;
import Model.InGameObjects.Cards.CardFactory;
import Model.InGameObjects.Cards.LeaderCard;

public class UserFaction implements Serializable {
    private User owner;
    private FactionName factionName;
    private ArrayList<LeaderCard> leaderCards = new ArrayList<>();
    private LeaderCard currentLeader;
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> storage = new ArrayList<>();

    public UserFaction(User owner, FactionName factionName) {
        this.owner = owner;
        this.factionName = factionName;
        createFactionCards();
    }

    private void createFactionCards() {
        ArrayList<Card> factionInventory = CardFactory.cloneAllCardsOfFaction(factionName);
        for (Card card : factionInventory) {
            if (card instanceof LeaderCard) {
                leaderCards.add((LeaderCard) card);
                currentLeader = (LeaderCard) card;
            }
        }

        for (Card card : factionInventory) {
            if (!(card instanceof LeaderCard)) {
                storage.add(card);
            }
        }
    }

    public FactionName getFactionName() {
        return factionName;
    }
    public ArrayList<Card> getDeck() {
        return deck;
    }
    public ArrayList<Card> getStorage() {
        return storage;
    }
    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
    public void setCurrentLeader(LeaderCard currentLeader) {
        this.currentLeader = currentLeader;
    }

    public Card getCurrentLeader() {
        return this.currentLeader;
    }

    public void addCardToStorage(Card card) {
        storage.add(card);
    }

    public void addCardToDeck(Card card) {
        deck.add(card);
    }

    public void removeCardFromStorage(Card card) {
        storage.remove(card);
    }

    public void removeCardFromDeck(Card card) {
        deck.remove(card);
    }

    public void addCardToLeaderCards(LeaderCard card) {
        leaderCards.add(card);
    }

    public Card getDeckCardByName(String name) {
        for (Card card : deck)
            if (card.getCardName().equals(name))
                return card;
        return null;
    }

    public boolean isCardByNameInDeck(String name) {
        return getDeckCardByName(name) != null;
    }

    public Card getStorageCardByName(String name) {
        for (Card card : deck)
            if (card.getCardName().equals(name))
                return card;
        return null;
    }

    public boolean isCardByNameInStorage(String name) {
        return getDeckCardByName(name) != null;
    }

    public Card getLeaderCardByName(String name) {
        for (Card card : deck)
            if (card.getCardName().equals(name))
                return card;
        return null;
    }

    public boolean isLeaderCardByNameExisting(String name) {
        return getDeckCardByName(name) != null;
    }

    public void printAllCardsDetails() {
        currentLeader.printCardDetail();

        for (Card card : leaderCards)
            card.printCardDetail();

        for (Card card : deck)
            card.printCardDetail();

        for (Card card : storage)
            card.printCardDetail();
    }
}
