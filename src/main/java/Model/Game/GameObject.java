package Model.Game;

import Model.InGameObjects.Cards.Card;
import view.Controller.GameViewController;

import java.util.ArrayList;

public class GameObject {

    private final GameHistory gameHistory;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private int turnNumber;
    private int roundNumber;
    private ArrayList<Card> allCards = new ArrayList<>();
    private GameViewController gameViewController;

    public GameObject(Player player1, Player player2) {
        this.player1 = player1;
        player1.getUser().setCurrentGame(this);
        this.player2 = player2;
        player2.getUser().setCurrentGame(this);
        this.gameHistory = new GameHistory(player1.getUser(), player2.getUser());
        turnNumber = 1;
        roundNumber = 1;

        allCards.addAll(player1.getDeck());
        allCards.add(player1.getLeaderCard());
        allCards.addAll(player2.getDeck());
        allCards.add(player2.getLeaderCard());
        setCardIds();
    }

    private void setCardIds() {
        int id = 1;
        for (Card card : allCards) {
            card.setId(id++);
        }
    }

    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public Card getCardById(int id) {
        for (Card card : allCards) {
            if (card.getId() == 6660)
                System.out.println(card.getId());
            if (card.getId() == id)
                return card;
        }

        return null;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void addToRoundNumber() {
        roundNumber++;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private void getPlayer1Score() {}

    private void getPlayer2Score() {}

    public int getTurnNumber() {
        return turnNumber;
    }

    public void addTurnNumber() {
        turnNumber++;
    }

    public GameViewController getGameViewController() {
        return gameViewController;
    }

    public void setGameViewController(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }
}
