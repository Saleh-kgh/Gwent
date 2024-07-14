package controller;

import Model.Game.Player;
import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.Card;
import Model.Package.*;
import Model.Package.ServerPackage;
import Model.User.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import view.Controller.GameViewController;
import Model.Result;
import view.Controller.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import Enum.Menus;
import Enum.CardStates;

import javax.imageio.ImageIO;

import static view.Controller.CinemaViewController.mainScene;

public class Client implements Runnable {
    public static Client client;
    private Socket socket;
    DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;

    public Client(Socket socket) {

        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            client = this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {

                    try {
                        ServerPackage obj = (ServerPackage) objectInputStream.readObject();

                        if (Objects.equals(obj.command, "makeGameWithUsers")) {
                            makeGameWithUsers(obj.object);
                        } else if (Objects.equals(obj.command, "getMessageFromOther")) {
                            receiveMessage((String) (obj.object));
                        }
                        // game rooms
                        else if (Objects.equals(obj.command, "currentGamesList")) {
                            GameRoomControl.CurrentGames = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "allGamesList")) {
                            GameRoomControl.AllGames = (ArrayList<String>) obj.object;
                        }
                        //register
                        else if (Objects.equals(obj.command, "registerResult")) {
                            assert obj.object instanceof Result;
                            RegisterViewController.result = (Result) obj.object;
                        } else if (Objects.equals(obj.command, "ld")) {
                            LeaderBoard.ranks = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "loginResult")) {
                            assert obj.object instanceof Result;
                            LoginViewController.result = (Result) ((ArrayList<Object>) obj.object).get(0);
                            if (((Result) ((ArrayList<Object>) obj.object).get(0)).isSuccessful()) {
                                user = (User) ((ArrayList<Object>) obj.object).get(1);
                            }
                        } else if (Objects.equals(obj.command, "secQuestion")) {
                            assert obj.object instanceof Result;
                            LoginViewController.result = ((Result) obj.object);
                        } else if (Objects.equals(obj.command, "RHistory")) {
                            ProfileViewController.ranks = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "search")) {
                            ProfileViewController.users = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "reqRes")) {
                            ProfileViewController.result = (Result) obj.object;
                        } else if (Objects.equals(obj.command, "friends")) {
                            ProfileViewController.ranks = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "joinObserver")) {
                            GameRoomViewControl.result = (Result) obj.object;
                        } else if (Objects.equals(obj.command, "changeUsername") ||
                                Objects.equals(obj.command, "changeNickName") ||
                                Objects.equals(obj.command, "changeEmail") ||
                                Objects.equals(obj.command, "passR") ||
                                Objects.equals(obj.command, "getInfo") ||
                                Objects.equals(obj.command, "gameHistory") ||
                                Objects.equals(obj.command, "accR") ||
                                Objects.equals(obj.command, "rejR") ||
                                Objects.equals(obj.command, "friendIsBusy") ||
                                Objects.equals(obj.command, "friendIsOffline") ||
                                Objects.equals(obj.command, "friendRejectGame") ||
                                Objects.equals(obj.command, "friendAcceptGame")) {
                            ProfileViewController.result = ((Result) obj.object);
                        } else if (Objects.equals(obj.command, "passL")) {
                            LoginViewController.result = ((Result) obj.object);
                        } else if (Objects.equals(obj.command, "getFameReq")) {
                            ProfileViewController.ranks = (ArrayList<String>) obj.object;
                        } else if (Objects.equals(obj.command, "moveCard") ||
                                Objects.equals(obj.command, "addCard") ||
                                Objects.equals(obj.command, "removeCard") ||
                                Objects.equals(obj.command, "sfxAnimation") ||
                                Objects.equals(obj.command, "playerTurn") ||
                                Objects.equals(obj.command, "playerDeck") ||
                                Objects.equals(obj.command, "playerHand") ||
                                Objects.equals(obj.command, "weatherEffect")) {

                            receiveGamePackage(obj);
                        } else if (Objects.equals(obj.command, "cheatPackage")) {
                            receiveCheatPackage(obj);
                        } else if (Objects.equals(obj.command, "someoneWantToPlayWithYou")) {
                            someoneWantToPlayWithYou((String) obj.object);
                        } else if (Objects.equals(obj.command, "receiveEmoji")) {
                            receiveEmoji((Integer) obj.object);
                        } else if (Objects.equals(obj.command, "checkCode")) {
                            RegisterViewController.result = new Result("title", "message", (boolean) obj.object);
                        } else if (Objects.equals(obj.command, "image")) {
                            receiveImage(obj);
                        } else if (Objects.equals(obj.command, "receiveReactionMessage")) {
                            receiveReactionMessage((Integer) obj.object);
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();
    }
    public void endGame(String username) {
        try {
            objectOutputStream.writeObject(new ServerPackage("endGame", username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addUser(String username, String password, String nickname, String email) {
        ArrayList<String> out = new ArrayList<>();
        out.add(username);
        out.add(password);
        out.add(nickname);
        out.add(email);
        try {
            objectOutputStream.writeObject(new ServerPackage("addUser", out));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkCode(String code) {
        try {
            objectOutputStream.writeObject(new ServerPackage("checkCode", code));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveReactionMessage(Integer integer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameViewController.getCurrentGameViewController().updateGUIWhenGetReactionMessage(integer);
            }
        });
    }
    private void receiveEmoji(Integer integer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameViewController.getCurrentGameViewController().updateGUIWhenGetEmoji(integer);
            }
        });
    }

    private void someoneWantToPlayWithYou(String username) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("friends");
                alert.setHeaderText("let's play");
                alert.setContentText(username + " wants to play with you");

                ButtonType buttonTypeOne = new ButtonType("i don't want to play");
                ButtonType buttonTypeTwo = new ButtonType("let's go");

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert.showAndWait();
                ArrayList<String> output = new ArrayList<>();
                output.add(username);
                output.add(client.user.getUsername());
                if (result.isPresent() && result.get() == buttonTypeOne) {
                    try {
                        objectOutputStream.writeObject(new ServerPackage("broDoesn'tWantToPlay", output));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (result.isPresent() && result.get() == buttonTypeTwo) {
                    try {
                        ViewController.changeMenuTo(Menus.PreGame.getValue());
                        objectOutputStream.writeObject(new ServerPackage("broWantsToPlay", output));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });


    }

    public void getLeaderBoard() {
        try {
            objectOutputStream.writeObject(new ServerPackage("leaderBoard", "leaderBoard"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestForNewGameWithFriend(String username) {

        try {
            objectOutputStream.writeObject(new ServerPackage("requestForNewGameWithFriend", username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startNewRandomGame() {
        try {
            // we need reset because we want to send <<user>> several times
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("startNewRandomGame", user));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(new ServerPackage("sendMessage", message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void receiveMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameViewController.getCurrentGameViewController().updateGUIWhenGetMessage(message);
            }
        });
    }

    private void makeGameWithUsers(Object object) {
        ArrayList<User> bothUsers = (ArrayList<User>) object;
        GameViewController.getCurrentGameViewController().setUserOfPlayer(bothUsers.get(0));
        GameViewController.getCurrentGameViewController().setUserOfOpponent((bothUsers.get(1)));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameViewController.getCurrentGameViewController().startGame();
            }
        });
    }

    public void sendGetAllGamesRequest() {
        String message = "getAllGames";
        try {
            objectOutputStream.writeObject(new ServerPackage("getAllGames", message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendGetCurrentGamesRequest() {
        String message = "getCurrentGames";
        try {
            objectOutputStream.writeObject(new ServerPackage("getCurrentGames", message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void authorization(String email) {
        try {
            objectOutputStream.writeObject(new ServerPackage("giveNumberForAuthorization", email));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRegisterRequest(String username, String password, String nickName, String email) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        arrayList.add(password);
        arrayList.add(nickName);
        arrayList.add(email);
        try {
            objectOutputStream.writeObject(new ServerPackage("register", arrayList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUpdateSecurityRequest(String username, String question, String answer) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        arrayList.add(question);
        arrayList.add(answer);
        try {
            objectOutputStream.writeObject(new ServerPackage("registerSec", arrayList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLoginRequest(String username, String password) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        arrayList.add(password);
        try {
            objectOutputStream.writeObject(new ServerPackage("login", arrayList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendQuestionSecurityRequest(String username, String num) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        arrayList.add(num);
        try {
            objectOutputStream.writeObject(new ServerPackage("securityQuestion", arrayList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSecurityAnswers(String username, String num, String answer, String text) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        arrayList.add(num);
        arrayList.add(answer);
        arrayList.add(text);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("securityAnswer", arrayList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUsernameChange(String newThing, String message) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage(message, newThing));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void sendPasswordChange(String passwordText) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("passCh", passwordText));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEndClient() {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("endClient", "no"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getHistory() {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("getFriendRequests", "no"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reject(String username) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("reject", username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void accept(String username) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("accept", username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMoveCardPackage(Card card, BoardSection beginning, BoardSection destination, CardStates cardState) {
        ServerPackage serverPackage = new ServerPackage("moveCard",
                new MoveCardPackage(card, beginning, destination, cardState));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSfxAnimationPackage(Card card, String sfxName, String animationName) {
        ServerPackage serverPackage = new ServerPackage("sfxAnimation",
                new SfxAnimationPackage(card, sfxName, animationName));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAddCardPackage(Card card, BoardSection destination, CardStates cardState, boolean isForPuttingNextRound) {
        ServerPackage serverPackage = new ServerPackage("addCard",
                new AddCardPackage(card, destination, cardState, isForPuttingNextRound));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRemoveCardPackage(Card card, BoardSection destination, CardStates cardState, boolean isForPuttingNextRound) {
        ServerPackage serverPackage = new ServerPackage("removeCard",
                new RemoveCardPackage(card, destination, cardState, isForPuttingNextRound));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendPlayerTurnPackage(int cardsInHandCount, int cardsInDeckCount, boolean hasPassed) {
        ServerPackage serverPackage = new ServerPackage("playerTurn",
                new PlayerTurnPackage(cardsInHandCount, cardsInDeckCount, hasPassed));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void getFriends() {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("getFriends", "hah"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUserUpdate(User user) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("getNewUserForDeck", user));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUserUpdateForGameHistory(User user) {
        try {
            ServerPackage serverPackage = new ServerPackage("getNewUserForGameHistory", new GameHistoryInformationPackage(
                    user.getUserGameHistories().get(user.getUserGameHistories().size() - 1),
                    user.getGameInfo()
            ));

            objectOutputStream.reset();
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User updatedUser) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("updateUser", updatedUser));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPlayerHandPackage(ArrayList<Card> handCards) {
        ServerPackage serverPackage = new ServerPackage("playerHand",
                new PlayerHandPackage(handCards));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendWeatherEffectPackage(String animationName) {
        ServerPackage serverPackage = new ServerPackage("weatherEffect",
                new WeatherAbilityPackage(animationName));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCheatPackage(int cheatCode) {
        ServerPackage serverPackage = new ServerPackage("cheatPackage",
                new CheatPackage(cheatCode));

        try {
            objectOutputStream.writeObject(serverPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void receiveGamePackage(ServerPackage serverPackage) {
        if (serverPackage.command.equals("moveCard")) {
            getPlayerOfClientUser().getGameController().serverMoveCard(serverPackage);
        } else if (serverPackage.command.equals("addCard")) {
            getPlayerOfClientUser().getGameController().serverAddCard(serverPackage);
        } else if (serverPackage.command.equals("removeCard")) {
            getPlayerOfClientUser().getGameController().serverRemoveCard(serverPackage);
        } else if (serverPackage.command.equals("sfxAnimation")) {
            getPlayerOfClientUser().getGameController().serverPlaySfxAnimation(serverPackage);
        } else if (serverPackage.command.equals("playerTurn")) {
            getPlayerOfClientUser().getGameController().serverPlayerTurn(serverPackage);
        } else if (serverPackage.command.equals("playerDeck")) {
        } else if (serverPackage.command.equals("playerHand")) {
            getPlayerOfClientUser().getGameController().serverPlayerHand(serverPackage);
        } else if (serverPackage.command.equals("weatherEffect")) {
            getPlayerOfClientUser().getGameController().serverWeatherAbility(serverPackage);
        }
    }

    public void receiveCheatPackage(ServerPackage serverPackage) {
        switch (((CheatPackage) serverPackage.object).getCheatCode()) {
            case 1:
                getPlayerOfClientUser().getGameController().serverGiveHomelander();
                break;
            case 2:
                getPlayerOfClientUser().getGameController().serverGiveButcher();
                break;
            case 3:
                getPlayerOfClientUser().getGameController().serverRecoverCrystal();
                break;
            case 4:
                getPlayerOfClientUser().getGameController().serverWinRound();
                break;
            case 5:
                getPlayerOfClientUser().getGameController().serverLoseRound();
        }
    }

    private Player getPlayerOfClientUser() {
        return GameController.currentGameObject.getCurrentPlayer();
    }
    public void sendReactionMessage(Integer whichEmoji) {
        try {
            objectOutputStream.writeObject(new ServerPackage("sendReactionMessage", whichEmoji));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendEmoji(Integer whichEmoji) {
        try {
            objectOutputStream.writeObject(new ServerPackage("sendEmoji", whichEmoji));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void JoinGame(String name) {
        try {
            objectOutputStream.writeObject(new ServerPackage("joinGameAsObserver", name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getReqs() {
        try {
            objectOutputStream.writeObject(new ServerPackage("getGameReqs", "huh"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendScreenShot() {
        try {
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(rectangle);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            objectOutputStream.writeObject(new ServerPackage("screenShot", bytes));
            socket.close();

        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveImage(ServerPackage serverPackage) {
        byte[] imageData = (byte[]) serverPackage.object;

        try {
            // Save the image to a file
            File imageFile = new File(user.getUsername() + ".jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();

            // Load the image from the file and set it to mainScene
            mainScene = new Image(imageFile.toURI().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
