package view;

import Model.User.User;
import controller.ControlServer;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import Enum.*;
import view.Controller.PreGameSetDeckViewController;

public class AppView extends Application {
    private static User loggedInUser;
    private static Stage currentStage;

    private static Scene currentScene;
    private static String currentMenu;
    private static ControlServer server;

    private static MediaPlayer musicPlayer;
    private static ArrayList<MediaPlayer> sfxPlayers = new ArrayList<>();

    public static void main(String[] args) {
        AppView.server = new ControlServer();
        launch(args);
        //DataSaver.getGsonInstance();
    }

    @Override
    public void start(Stage stage) throws Exception {
        new LoginController().instantiateAllCards();

        this.currentStage = stage;
        Image icon = new Image(getClass().getResourceAsStream("/Pics/Icons/GameIcon.jpeg"));
        stage.getIcons().add(icon);
        stage.setTitle("Gwent");

//        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
//        stage.setScene(new Scene(root, Sizes.SCENE.getWidth(), Sizes.SCENE.getHeight()));
//        currentScene = stage.getScene();
        setRootForMenu("Login");
        stage.centerOnScreen();
//        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("`"));
        stage.show();
    }

    private static void changeSceneRoot(Parent root) {
        currentStage.setScene(new Scene(root, Sizes.SCENE.getWidth(), Sizes.SCENE.getHeight()));
        currentScene = currentStage.getScene();
//        currentStage.setFullScreen(true);
    }

    public static void setRootForMenu(String menuName) {
        String address = "";
        switch (menuName) {
            case "Login":
                if (currentMenu == null)
                    playMenusMedia();
                address = "/FXML/Login.fxml";
                currentMenu = "Login";
                break;
            case "Register":
                address = "/FXML/Register.fxml";
                currentMenu = "Register";
                break;
            case "Main":
                if (currentMenu.equals("Game")) {
                    stopMusic();
                    playMenusMedia();
                }
                if (sfxPlayers.isEmpty())
                    initializeSfxPlayers();
                address = "/FXML/Main.fxml";
                currentMenu = "Main";
                break;
            case "Profile":
                address = "/FXML/Profile.fxml";
                currentMenu = "Profile";
                break;
            case "Game":
                stopMusic();
                playGameMusic();
                address = "/FXML/Game.fxml";
                currentMenu = "Game";
                break;
            case "LeaderBoard":
                address = "/FXML/LeaderBoard.fxml";
                currentMenu = "LeaderBoard";
                break;
            case "room":
                address = "/FXML/GameRoom.fxml";
                currentMenu = "Room";
                break;
            case "cinema":
                address = "/FXML/Cinema.fxml";
                currentMenu = "Cinema";
                break;
            case "PreGame":
                address = "/FXML/PreGameSetDeck.fxml";
                currentMenu = "PreGame";
                try {
                    FXMLLoader loader = new FXMLLoader(AppView.class.getResource(address));
                    changeSceneRoot(loader.load());
                    new PreGameSetDeckViewController(loader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
        }
        Parent root;
        try {
            root = FXMLLoader.load(AppView.class.getResource(address));
            changeSceneRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static ControlServer getServer() {
        return server;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        AppView.loggedInUser = loggedInUser;
    }

    public static void playMenusMedia() {
        try {
            URL musicUrl = AppView.class.getResource("/SFX/Musics/TavernMusic.mp3");
            if (musicUrl == null) {
                throw new RuntimeException("Media file not found");
            }

            Media musicMedia = new Media(musicUrl.toString());

            musicPlayer = new MediaPlayer(musicMedia);
            musicPlayer.setVolume(0);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            musicPlayer.setOnReady(() -> {
                musicPlayer.play();
            });

            musicPlayer.setOnError(() -> {
                System.err.println("Error occurred: " + musicPlayer.getError());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playGameMusic() {
        try {
            URL musicUrl = AppView.class.getResource(chooseRandomMedia());
            if (musicUrl == null) {
                throw new RuntimeException("Media file not found");
            }

            Media musicMedia = new Media(musicUrl.toString());

            musicPlayer = new MediaPlayer(musicMedia);
            musicPlayer.setVolume(0);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            musicPlayer.setOnReady(() -> {
                musicPlayer.play();
            });

            musicPlayer.setOnError(() -> {
                System.err.println("Error occurred: " + musicPlayer.getError());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
    }

    private static String chooseRandomMedia() {
        String[] mediaAddress = new String[2];
        mediaAddress[0] = "/SFX/Musics/AStoryYou.mp3";
        mediaAddress[1] = "/SFX/Musics/MustyScent.mp3";

        Random random = new Random();

        return mediaAddress[random.nextInt(mediaAddress.length)];
    }

    public static void playSfxAudio(String mediaName) {
        MediaPlayer sfxPlayer = chooseSfxPlayer(mediaName);

        if (sfxPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            return;
        } else {
            sfxPlayer.setVolume(0.35);
            sfxPlayer.setCycleCount(1);
            sfxPlayer.play();
        }

        sfxPlayer.setOnError(() -> {
            System.err.println("Error occurred: " + sfxPlayer.getError());
        });

        sfxPlayer.setOnEndOfMedia(() -> {
            sfxPlayer.stop();
        });
    }

    private static MediaPlayer chooseSfxPlayer(String mediaName) {
        if (mediaName.equals("ally")) return sfxPlayers.get(0);
        else if (mediaName.equals("card")) return sfxPlayers.get(1);
        else if (mediaName.equals("clear")) return sfxPlayers.get(2);
        else if (mediaName.equals("cold")) return sfxPlayers.get(3);
        else if (mediaName.equals("fog")) return sfxPlayers.get(4);
        else if (mediaName.equals("game_start")) return sfxPlayers.get(5);
        else if (mediaName.equals("game_lose")) return sfxPlayers.get(6);
        else if (mediaName.equals("game_win")) return sfxPlayers.get(7);
        else if (mediaName.equals("hero")) return sfxPlayers.get(8);
        else if (mediaName.equals("horn")) return sfxPlayers.get(9);
        else if (mediaName.equals("med")) return sfxPlayers.get(10);
        else if (mediaName.equals("moral")) return sfxPlayers.get(11);
        else if (mediaName.equals("rain")) return sfxPlayers.get(12);
        else if (mediaName.equals("redraw")) return sfxPlayers.get(13);
        else if (mediaName.equals("scorch")) return sfxPlayers.get(14);
        else if (mediaName.equals("common1")) return sfxPlayers.get(15);
        else if (mediaName.equals("common2")) return sfxPlayers.get(16);
        else if (mediaName.equals("common3")) return sfxPlayers.get(17);
        else if (mediaName.equals("resilience")) return sfxPlayers.get(18);
        else if (mediaName.equals("round_win")) return sfxPlayers.get(19);
        else if (mediaName.equals("round_lose")) return sfxPlayers.get(20);
        else if (mediaName.equals("seize")) return sfxPlayers.get(21);
        else if (mediaName.equals("spy")) return sfxPlayers.get(22);
        else if (mediaName.equals("turn_me")) return sfxPlayers.get(23);
        else if (mediaName.equals("turn_op")) return sfxPlayers.get(24);
        else if (mediaName.equals("warning")) return sfxPlayers.get(25);
        else return null;
    }

    private static void initializeSfxPlayers() {
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/ally.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/card.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/clear.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/cold.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/fog.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/game_start.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/game_lose.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/game_win.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/hero.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/horn.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/med.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/moral.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/rain.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/redraw.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/scorch.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/common1.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/common2.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/common3.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/resilience.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/round_win.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/round_lose.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/seize.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/spy.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/turn_me.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/turn_op.mp3").toString())));
        sfxPlayers.add(new MediaPlayer(new Media(AppView.class.getResource("/SFX/Effects/warning.mp3").toString())));

        for (MediaPlayer sfxPlayer : sfxPlayers) {
            sfxPlayer.setVolume(0);
            sfxPlayer.setCycleCount(1);
            sfxPlayer.play();
            sfxPlayer.setOnEndOfMedia(() -> {
                sfxPlayer.stop();
            });
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}
