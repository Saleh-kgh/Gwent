package Model.User;

import Model.FriendRequest;
import Model.Game.GameHistory;
import Model.Game.GameObject;
import Model.InGameObjects.Cards.Card;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private UserCardInventory cardInventory;
    private UserGameInfo gameInfo;
    private ArrayList<GameHistory> userGameHistories = new ArrayList<>();
    private ArrayList<String> passwordQuestions = new ArrayList<>();
    private ArrayList<String> passwordAnswers = new ArrayList<>();
    private ArrayList<String> friendsNames = new ArrayList<>();
    private ArrayList<User> friends = new ArrayList<>();
    private ArrayList<FriendRequest> friendRequests = new ArrayList<>();
    private ArrayList<String> friendRequestsLog = new ArrayList<>();
    private ArrayList<String> gameRequestsLog = new ArrayList<>();
    public boolean online = false;
    private boolean isPlayingGame;
    private transient GameObject currentGame;
    private User opponentUser;
    private static ArrayList<User> allUsers = new ArrayList<>();

    public User(String username, String password, String nickname, String email, boolean addToDataBase) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.cardInventory = new UserCardInventory(this);
        this.gameInfo = new UserGameInfo(this);
        if (addToDataBase) allUsers.add(this);
    }

    public void recreateUserCardInventory() {
        this.cardInventory = new UserCardInventory(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public User getOpponentUser() {
        return opponentUser;
    }

    public void setOpponentUser(User opponentUser) {
        this.opponentUser = opponentUser;
    }

    public UserGameInfo getGameInfo() {
        return gameInfo;
    }

    public ArrayList<GameHistory> getUserGameHistories() {
        return userGameHistories;
    }

    public ArrayList<String> getPasswordQuestions() {
        return passwordQuestions;
    }

    public ArrayList<String> getPasswordAnswers() {
        return passwordAnswers;
    }

    public boolean isPlayingGame() {
        return isPlayingGame;
    }

    public GameObject getCurrentGame() {
        return currentGame;
    }

    public void setCardInventory(UserCardInventory cardInventory) {
        this.cardInventory = cardInventory;
    }

    public void setGameInfo(UserGameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void setUserGameHistories(ArrayList<GameHistory> userGameHistories) {
        this.userGameHistories = userGameHistories;
    }

    public void setPasswordQuestions(ArrayList<String> passwordQuestions) {
        this.passwordQuestions = passwordQuestions;
    }

    public void setPasswordAnswers(ArrayList<String> passwordAnswers) {
        this.passwordAnswers = passwordAnswers;
    }

    public void setPlayingGame(boolean playingGame) {
        isPlayingGame = playingGame;
    }

    public void setCurrentGame(GameObject currentGame) {
        this.currentGame = currentGame;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserCardInventory getCardInventory() {
        return this.cardInventory;
    }

    public void addGameHistory(GameHistory gameHistory) {
        this.userGameHistories.add(0, gameHistory);
    }

    public static void removeUser(User user) {
        allUsers.remove(user);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers)
            if (user.getUsername().equals(username)) {
                return user;
            }
        return null;
    }

    public String returnQuestion(int questionNumber) {
        return passwordAnswers.get(questionNumber);
    }

    public static boolean doesUsernameExist(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public static boolean doesNicknameExist(String nickname) {
        for (User user : allUsers) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    public static boolean doesEmailExist(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email))
                return true;
        }
        return false;
    }

    public static boolean doesUserExist(String username) {
        for (User user : allUsers)
            if (user.username.equals(username))
                return true;
        return false;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public ArrayList<String> getFriendsNames() {
        return friendsNames;
    }

    public void setFriendsNames(ArrayList<String> friendsNames) {
        this.friendsNames = friendsNames;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public boolean isYourFriend(User user) {
        for (User friend : this.getFriends())
            if (user.getUsername().equals(friend.getUsername()))
                return true;
        return false;
    }

    public String isOnline() {
        if (online) return "online";
        else return "offline";
    }

    public ArrayList<String> getFriendRequestsLog() {
        return friendRequestsLog;
    }

    public ArrayList<String> getGameRequestsLog() {
        return gameRequestsLog;
    }

    public void removeAllCardsGraphics() {
        for (Card card : cardInventory.getCurrentFaction().getDeck())
            removeEveryGraphicalFieldOfCard(card);
        removeEveryGraphicalFieldOfCard(cardInventory.getCurrentFaction().getCurrentLeader());
    }
    
    private void removeEveryGraphicalFieldOfCard(Card card) {
        card.removeGraphicalFields();
        card.setCurrentPlaceInGame(null);
        card.setPlayer(null);
        card.setOwner(null);
        card.setCurrentState(null);
    }

    public void checkForAnyGraphicalField() {
        getCardInventory().getCurrentFaction().printAllCardsDetails();
        getCardInventory().getNilfgaardEmpire().printAllCardsDetails();
        getCardInventory().getNorthernRealms().printAllCardsDetails();
        getCardInventory().getMonsters().printAllCardsDetails();
        getCardInventory().getScoiaTael().printAllCardsDetails();
        getCardInventory().getSkellige().printAllCardsDetails();
    }
}