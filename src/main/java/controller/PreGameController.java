package controller;

import Model.Result;
import Model.User.User;

public class PreGameController {

    private final User user1;
    private final User user2;
    private User currentUser;
    private boolean user1Ready;
    private boolean user2Ready;

    public PreGameController(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        user1Ready = false;
        user2Ready = false;
    }

    public Result showFactions() { // in result message create faction names with current faction
        return null;
    }

    public Result chooseFaction(String factionName) {
        return null; // see if the faction is already chosen or not then choose current faction of userInventory as this faction;
    }

    public Result showCurrentFactionAllCards() {
        return null; // in the string builder create a line for each card with its description in front of it
    }

    public Result showCurrentFactionDeck() {
        return null; // in the string builder create a line for each card with its description in front of it
    }

    public Result showCurrentPlayerInfo() {
        return null;
    }

    public Result saveDeckInAddress(String address) {
        return null;
    }

    public Result saveDeckByName(String name) {
        return null;
    }

    public Result loadDeckByAddress(String address) {
        return null;
    }

    public Result ladDeckByName(String name) {
        return null;
    }

    public Result showCurrentFactionLeaders() {
        return null;
    }

    public Result showCurrentFactionCurrentLeader() {
        return null;
    }

    public Result selectLeader(String leaderName) {
        return null;
    }

    public Result addCardToDeck(String cardName, int cardCount) {
        return null;
    }

    public Result removeCardFromDeck(String cardName, int cardCount) {
        return null;
    }

    public Result changeTurn() {
        return null; // check for conditions to pass deck setting input to other player or not, and if this is second player start game?
    }

    public Result startGame() {
        return null;
    }
}
