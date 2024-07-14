package Model.Game;

import Model.InGameObjects.Abilities.FactionAbility.*;
import Model.InGameObjects.Board.Board;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.LeaderCard;
import Model.InGameObjects.Cards.SpellCard;
import Model.InGameObjects.Cards.UnitCard;
import Model.User.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import Enum.*;
import controller.GameController;
import javafx.scene.layout.Pane;

public class Player implements Serializable {

    private User user;
    private GameObject gameObject;
    private Player opponent;
    private Board board;
    private Pane gamePane;
    private GameController gameController;
    private int remainingGems;
    private boolean paralyzedByAnimation;
    private boolean hasOpponentSwappedInitialHand;
    private PlayerState playerState;
    private FactionName playersFaction;
    private FactionAbility factionAbility;
    private LeaderCard leaderCard;
    private Card selectedCard;
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> deck = new ArrayList<>();

    public Player(User user, FactionName playersFaction, ArrayList<Card> deck, LeaderCard leaderCard) {
        this.user = user;
        this.playersFaction = playersFaction;
        this.deck = deck;
        this.leaderCard = leaderCard;
        this.remainingGems = 2;
        this.gameController = new GameController(this);
        this.hasOpponentSwappedInitialHand = false;

        assignOwnerAndPlayerToCards(deck);
        assignOwnerAndPlayerToLeader();
    }

    private FactionAbility createFactionAbility() {
        switch (playersFaction) {
            case NilfgaardianEmpire:
                return new NilfgaardAbility(gameObject.getCurrentPlayer(), this);
            case NorthernRealms:
                return new NorthernRealmsAbility(gameObject.getCurrentPlayer(), this);
            case Monsters:
                return new MonstersAbility(gameObject.getCurrentPlayer(), this);
            case ScoiaTael:
                return new ScoiaTaelAbility(gameObject.getCurrentPlayer(), this);
            case Skellige:
                return new SkelligeAbility(gameObject.getCurrentPlayer(), this);
            case SevenKingdoms:
                return new SevenKingdomsAbility(gameObject.getCurrentPlayer(), this);
        }
        return null;
    }

    public void assignGameObject() {
        this.gameObject = user.getCurrentGame();
        this.factionAbility = createFactionAbility();
    }

    private void assignOwnerAndPlayerToCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            card.setOwner(this);
            card.setPlayer(this);
        }
    }

    private void assignOwnerAndPlayerToLeader() {
        leaderCard.setOwner(this);
        leaderCard.setPlayer(this);
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
    }

    public boolean isParalyzedByAnimation() {
        return paralyzedByAnimation;
    }

    public void setParalyzedByAnimation(boolean paralyzedByAnimation) {
        this.paralyzedByAnimation = paralyzedByAnimation;
    }

    public boolean isHasOpponentSwappedInitialHand() {
        return hasOpponentSwappedInitialHand;
    }

    public void setHasOpponentSwappedInitialHand(boolean hasOpponentSwappedInitialHand) {
        this.hasOpponentSwappedInitialHand = hasOpponentSwappedInitialHand;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public User getUser() {
        return user;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public int getRemainingGems() {
        return remainingGems;
    }

    public FactionAbility getFactionAbility() {
        return factionAbility;
    }

    public void setFactionAbility(FactionAbility factionAbility) {
        this.factionAbility = factionAbility;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public FactionName getPlayersFaction() {
        return playersFaction;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public LeaderCard getLeaderCard() {
        return leaderCard;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setLeaderCard(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void setRemainingGems(int remainingGems) {
        this.remainingGems = remainingGems;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setRandomHand() {
        ArrayList<Integer> unitCardChosenIndexes = new ArrayList<>();
        ArrayList<Integer> spellCardChosenIndexes = new ArrayList<>();

        Random random = new Random();
        int chosenUnitCards = random.nextInt(3) + 7;
        int totalCards = 10;

        if (getLeaderCard().getCardName().equals(CardNames.FRANCESCA_DAISY_OF_THE_VALLEY)) {
            chosenUnitCards += 1;
            totalCards += 1;
        }

        for (int i = 0; i < chosenUnitCards;) {
            int index = random.nextInt(getDeck().size());
            if (unitCardChosenIndexes.contains(index))
                continue;
            Card card = getDeck().get(index);
            if (!(card instanceof UnitCard))
                continue;
            unitCardChosenIndexes.add(index);
            getHand().add(card);
            card.createGraphicalFields();
            i++;
        }

        for (int i = 0; i < totalCards - chosenUnitCards;) {
            int index = random.nextInt(getDeck().size());
            if (spellCardChosenIndexes.contains(index))
                continue;
            Card card = getDeck().get(index);
            if (!(card instanceof SpellCard))
                continue;
            spellCardChosenIndexes.add(index);
            getHand().add(card);
            card.createGraphicalFields();
            i++;
        }

        for (int i = 0; i < getHand().size(); i++) {
            getHand().get(i).setPreviousState(CardStates.ROW_IN_GAME_HAND);
            getHand().get(i).setCurrentState(CardStates.ROW_IN_GAME_HAND);
            getHand().get(i).setCurrentPlaceInGame(getBoard().getHandArea());
            getDeck().remove(getHand().get(i));
        }
    }

    public void setInGameDeck() {
        for (Card card : gameObject.getPlayer1().getDeck()) {
            card.setPreviousState(CardStates.ROW_IN_GAME_DECK);
            card.setCurrentState(CardStates.ROW_IN_GAME_DECK);
            card.setCurrentPlaceInGame(getBoard().getFriendlyDeckArea());
        }
    }

    public Card getOneRandomCardFromDeck() {
        if (!getDeck().isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(getDeck().size());
            Card card = getDeck().get(index);
            getDeck().remove(card);
            card.createGraphicalFields();
            getBoard().getFriendlyDeckArea().removeFromCardsOnSection(card);
            getBoard().getFriendlyDeckArea().updateSection();
            return card;
        }
        return null;
    }
}
