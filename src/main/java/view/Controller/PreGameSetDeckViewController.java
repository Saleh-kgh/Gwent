package view.Controller;


import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardFactory;
import Model.InGameObjects.Cards.LeaderCard;
import Model.InGameObjects.Cards.SpellCard;
import Model.InGameObjects.Cards.UnitCard;
import Model.User.User;
import Model.User.UserFaction;
import com.google.gson.*;
import controller.Client;
import controller.PreGameSetDeckController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import Enum.*;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class PreGameSetDeckViewController extends ViewController {


    private FXMLLoader loader;
    private PreGameSetDeckController preGameSetDeckController;
    private User user;
    private Pane middlePane;
    private Pane pane;
    private GridPane storageGridPane;
    private ScrollPane storageScrollPane;
    private GridPane deckGridPane;
    private ScrollPane deckScrollPane;
    private Text factionNameText;
    private ImageView factionIcon;
    private ImageView leaderCardImage;
    private Text changeFactionText;
    private Button startButton;
    private Button backButton;
    private Button saveButton;
    private Label totalCardsInDeck;
    private Label numberOfUnitCards;
    private Label numberOfSpecialCards;
    private TextField nameTextField;
    private TextField addressTextField;
    private Button saveDeckButton;
    private Button loadDeckButton;
    private ArrayList<CardsInPreGame> cardsInStorage = new ArrayList<>();
    private ArrayList<CardsInPreGame> cardsInDeck = new ArrayList<>();
    public static boolean canTouchScreen = true;

    public PreGameSetDeckViewController() {
        // fxml loader need this :)
    }

    public PreGameSetDeckViewController(FXMLLoader loader) {

        this.loader = loader;
        pane = (Pane) loader.getNamespace().get("MainPane");
        super.setPane(pane);
        user = Client.client.getUser();
        storageScrollPane = (ScrollPane) loader.getNamespace().get("StorageScrollPane");
        storageGridPane = (GridPane) loader.getNamespace().get("StorageGridPane");
        deckScrollPane = (ScrollPane) loader.getNamespace().get("DeckScrollPane");
        deckGridPane = (GridPane) loader.getNamespace().get("DeckGridPane");
        middlePane = (Pane) loader.getNamespace().get("MiddlePane");
        factionNameText = (Text) loader.getNamespace().get("FactionNameText");
        factionIcon = (ImageView) loader.getNamespace().get("FactionIcon");
        leaderCardImage = (ImageView) loader.getNamespace().get("LeaderCardImage");
        changeFactionText = (Text) loader.getNamespace().get("ChangeFactionText");
        totalCardsInDeck = (Label) loader.getNamespace().get("totalCardsInDeck");
        numberOfUnitCards = (Label) loader.getNamespace().get("numberOfUnitCards");
        numberOfSpecialCards = (Label) loader.getNamespace().get("numberOfSpecialCards");
        startButton = (Button) loader.getNamespace().get("startButton");
        backButton = (Button) loader.getNamespace().get("backButton");
        saveButton = (Button) loader.getNamespace().get("saveButton");
        nameTextField = (TextField) loader.getNamespace().get("nameTextField");
        addressTextField = (TextField) loader.getNamespace().get("addressTextField");
        saveDeckButton = (Button) loader.getNamespace().get("saveDeckButton");
        loadDeckButton = (Button) loader.getNamespace().get("loadDeckButton");
        preGameSetDeckController = new PreGameSetDeckController();

        startButton.setOnMouseClicked(eve -> {
            if (isItCorrectDeck()) {
                Client.client.sendUserUpdate(user);
                changeMenuTo(Menus.Game.getValue());
            } else {
                throwAlert("Unacceptable deck", "Min 22 unit card and Max 10 special card");
            }
        });
        backButton.setOnMouseClicked(e -> {
            changeMenuTo(Menus.Main.getValue());
        });
        saveButton.setOnMouseClicked(e -> {
            if (isItCorrectDeck()) {
                Client.client.sendUserUpdate(user);
            } else {
                throwAlert("Unacceptable deck", "Min 22 unit card and Max 10 special card");
            }

        });
        saveDeckButton.setOnMouseClicked(e -> {
            if (isItCorrectDeck()) {
                saveDeck();
            } else {
                throwAlert("Unacceptable deck", "Min 22 unit card and Max 10 special card");
            }
        });
        loadDeckButton.setOnMouseClicked(e -> {
            loadDeck();
        });

        run();
    }

    private void saveDeck() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();

        if (name.isEmpty() && address.isEmpty()) {
            throwAlert("hey lad", "save what??huh?");
        } else if (name.isEmpty()) {
            String path = translateToAddress(address);
            saveDeckWithAddress(path);
        } else {
            String path = translateToAddress(name);
            saveDeckWithAddress(path);
        }
    }

    private void loadDeck() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();

        if (name.isEmpty() && address.isEmpty()) {
            throwAlert("hey lad", "load what??huh?");
        } else if (name.isEmpty()) {
            loadDeckWithAddress(address);
        } else {
            loadDeckWithAddress(name);
        }
    }

    private String translateToAddress(String name) {
        if (name.endsWith(".json"))
            return name;
        return name + ".json";
    }

    private void saveDeckWithAddress(String address) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(address);
        if (file.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("oops, name or dir is taken");
            alert.setContentText("do you confirm overwrite?");
            alert.initStyle(StageStyle.UNDECORATED); // Removes the default dialog window decorations
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/StyleSheets/witcher-theme.css").toExternalForm()
            );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                throwAlert("overWriting...", "the old file is being replaced");
            } else {
                throwAlert("aborting...", "did not overwrite");
                return;
            }
        }
        JsonArray deckArray = new JsonArray();
        JsonObject deckJson = new JsonObject();
        deckJson.addProperty("factionName", user.getCardInventory().getCurrentFaction().getFactionName().name());
        deckJson.addProperty("leader",
                user.getCardInventory().getCurrentFaction().getCurrentLeader().getCardName().getCardNameString());
        for (Card card : user.getCardInventory().getCurrentFaction().getDeck()) {
            deckArray.add(card.getCardName().getCardNameString());
        }
        deckJson.add("deck", deckArray);
        try (FileWriter writer = new FileWriter(address)) {
            gson.toJson(deckJson, writer);
        } catch (Exception e) {
            e.printStackTrace();
            throwAlert("Error", "Failed to save deck: " + e.getMessage());
        }
    }

    private void loadDeckWithAddress(String address) {
        Gson gson = new Gson();
        File file = new File(address);
        if (!file.exists()) {
            throwAlert("Error", "Directory or file does not exist: " + address);
            return;
        }

        try (FileReader reader = new FileReader(address)) {
            JsonObject deckJson = gson.fromJson(reader, JsonObject.class);
            if (deckJson == null) {
                throwAlert("Error", "Directory or file does not exist: " + address);
                return;
            }
            if (!address.endsWith(".json") ||
                    !deckJson.has("factionName") ||
                    !deckJson.has("leader") ||
                    !deckJson.has("deck")) {
                throwAlert("Error", "Invalid file format: " + address);
                return;
            }
            CardNames cardNames = CardNames.ARACHAS_1;
            String factionName = deckJson.get("factionName").getAsString();
            Card leader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                    deckJson.get("leader").getAsString()));
            JsonArray deckArray = deckJson.getAsJsonArray("deck");
            ArrayList<Card> deckCards = new ArrayList<>();
            for (int j = 0; j < deckArray.size(); j++) {
                deckCards.add(
                        CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(deckArray.get(j).getAsString())));
            }
            System.out.println(deckJson);
            if (factionName.equals(FactionName.ScoiaTael.name()))
                user.getCardInventory().setCurrentFaction(new UserFaction(user, FactionName.ScoiaTael));
            else if (factionName.equals(FactionName.Monsters.name()))
                user.getCardInventory().setCurrentFaction(new UserFaction(user, FactionName.Monsters));
            else if (factionName.equals(FactionName.NilfgaardianEmpire.name()))
                user.getCardInventory().setCurrentFaction(new UserFaction(user, FactionName.NilfgaardianEmpire));
            else if (factionName.equals(FactionName.NorthernRealms.name()))
                user.getCardInventory().setCurrentFaction(new UserFaction(user, FactionName.NorthernRealms));
            else if (factionName.equals(FactionName.Skellige.name()))
                user.getCardInventory().setCurrentFaction(new UserFaction(user, FactionName.Skellige));
            user.getCardInventory().getCurrentFaction().setCurrentLeader((LeaderCard) leader);
            user.getCardInventory().getCurrentFaction().getDeck().addAll(deckCards);
            loadFaction(user.getCardInventory().getCurrentFaction());
        } catch (Exception e) {
            e.printStackTrace();
            throwAlert("Error", "Failed to load deck: ");
        }
    }

    private void run() {

        loadFaction(user.getCardInventory().getCurrentFaction());
        adjustChangeFaction();
        adjustChangeLeaderCard();
        storageScrollPane.setContent(storageGridPane);
        deckScrollPane.setContent(deckGridPane);

    }

    private void adjustChangeLeaderCard() {

        PreGameSetDeckViewController preGameSetDeckViewController = this;

        leaderCardImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (canTouchScreen) {
                    GridPane tempPane = new GridPane();

                    tempPane.setLayoutX(PreGameSetDeck.CHANGE_LEADER_GRID_PANE_X_POSITION.getValue());
                    tempPane.setLayoutY(PreGameSetDeck.CHANGE_LEADER_GRID_PANE_Y_POSITION.getValue());
                    tempPane.setHgap(PreGameSetDeck.CHANGE_LEADER_GRID_PANE_H_GAP.getValue());
                    tempPane.setVgap(PreGameSetDeck.CHANGE_LEADER_GRID_PANE_V_GAP.getValue());


                    for (int i = 0; i < user.getCardInventory().getCurrentFaction().getLeaderCards().size(); i++) {


                        LeaderCardInPreGame leaderCardInPreGame = new LeaderCardInPreGame(
                                user.getCardInventory().getCurrentFaction().getLeaderCards().get(i).getDescriptionCardImagePath(),
                                PreGameSetDeck.LEADER_CARD_WIDTH.getValue(), PreGameSetDeck.LEADER_CARD_HEIGHT.getValue(),
                                pane, tempPane, user, preGameSetDeckViewController,
                                user.getCardInventory().getCurrentFaction().getLeaderCards().get(i));

                        tempPane.add(leaderCardInPreGame, i, 0);
                    }

                    pane.getChildren().add(tempPane);
                    canTouchScreen = false;
                }

            }
        });

    }

    private void adjustChangeFaction() {

        PreGameSetDeckViewController preGameSetDeckViewController = this;
        changeFactionText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (canTouchScreen) {
                    GridPane tempPane = new GridPane();

                    tempPane.setLayoutX(PreGameSetDeck.CHANGE_FACTION_GRID_PANE_X_POSITION.getValue());
                    tempPane.setLayoutY(PreGameSetDeck.CHANGE_FACTION_GRID_PANE_Y_POSITION.getValue());
                    tempPane.setHgap(PreGameSetDeck.CHANGE_FACTION_GRID_PANE_H_GAP.getValue());
                    tempPane.setVgap(PreGameSetDeck.CHANGE_FACTION_GRID_PANE_V_GAP.getValue());

                    for (int i = 0; i < 6; i++) {
                        String path = null;
                        switch (i) {
                            case 0:
                                path = "/Pics/Icons/FactionIcons/deck_back_monsters.jpg";
                                break;
                            case 1:
                                path = "/Pics/Icons/FactionIcons/deck_back_nilfgaard.jpg";
                                break;
                            case 2:
                                path = "/Pics/Icons/FactionIcons/deck_back_realms.jpg";
                                break;
                            case 3:
                                path = "/Pics/Icons/FactionIcons/deck_back_scoiatael.jpg";
                                break;
                            case 4:
                                path = "/Pics/Icons/FactionIcons/deck_back_skellige.jpg";
                                break;
                            case 5:
                                path = "/Pics/Icons/FactionIcons/deck_back_sevenKingdoms.jpg";
                                break;
                        }
                        FactionCard factionCard = new FactionCard(path,
                                PreGameSetDeck.CHANGE_FACTION_GRID_PANE_WIDTH.getValue(), PreGameSetDeck.CHANGE_FACTION_GRID_PANE_HEIGHT.getValue(),
                                pane, tempPane, user, preGameSetDeckViewController);

                        tempPane.add(factionCard, i, 0);

                    }

                    pane.getChildren().add(tempPane);
                    canTouchScreen = false;
                }


            }
        });

    }

    private void updateFactionNameAndIcon(UserFaction userFaction) {

        switch (userFaction.getFactionName()) {
            case NorthernRealms:
                factionNameText.setText("Northern Realms");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_realms.png").toString()));
                break;
            case NilfgaardianEmpire:
                factionNameText.setText("Nilfgaardian Empire");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_nilfgaard.png").toString()));
                break;
            case Monsters:
                factionNameText.setText("Monsters");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_monsters.png").toString()));
                break;
            case ScoiaTael:
                factionNameText.setText("Scoia'tael");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_scoiatael.png").toString()));
                break;
            case Skellige:
                factionNameText.setText("Skellige");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_skellige.png").toString()));
                break;
            case SevenKingdoms:
                factionNameText.setText("Seven Kingdoms");
                factionIcon.setImage(new Image(getClass().getResource("/Pics/Icons/FactionIcons/deck_shield_sevenKingdoms.png").toString()));
                break;
        }

    }

    public void loadFaction(UserFaction userFaction) {

        updateFactionNameAndIcon(userFaction);

        cardsInDeck.clear();
        cardsInStorage.clear();

        for (Card card : userFaction.getDeck()) {

            CardsInPreGame cardsInPreGame = new CardsInPreGame(card.getDescriptionCardImagePath(),
                    PreGameSetDeck.CARD_WIDTH.getValue(), PreGameSetDeck.CARD_HEIGHT.getValue(),
                    card, false, this);

            cardsInDeck.add(cardsInPreGame);
        }
        for (Card card : userFaction.getStorage()) {

            CardsInPreGame cardsInPreGame = new CardsInPreGame(card.getDescriptionCardImagePath(),
                    PreGameSetDeck.CARD_WIDTH.getValue(), PreGameSetDeck.CARD_HEIGHT.getValue(),
                    card, true, this);

            cardsInStorage.add(cardsInPreGame);
        }
        updateTotalCardsInDeckLabel();
        updateNumberOfUnitCardsInDeckLabel();
        updateNumberOfSpecialCardsInDeckLabel();
        updateLeaderCardImage();
        updateGridPaneDeckCard();
        updateGridPaneStorageCard();
    }

    private boolean isItCorrectDeck() {
        int x = 0;
        for (Card card : user.getCardInventory().getCurrentFaction().getDeck()) {
            if (card instanceof UnitCard) {
                x++;
            }
        }
        if (x < 22) {
            return false;
        }
        x = 0;
        for (Card card : user.getCardInventory().getCurrentFaction().getDeck()) {
            if (card instanceof SpellCard) {
                x++;
            }
        }
        return x <= 10;
    }

    private void updateNumberOfSpecialCardsInDeckLabel() {
        int x = 0;
        for (Card card : user.getCardInventory().getCurrentFaction().getDeck()) {
            if (card instanceof SpellCard) {
                x++;
            }
        }
        numberOfSpecialCards.setText(String.valueOf(x));
    }

    private void updateNumberOfSpecialCardsInDeckLabel(int numberOfAdd) {
        int x = Integer.parseInt(numberOfSpecialCards.getText());
        numberOfSpecialCards.setText(String.valueOf(x + numberOfAdd));
    }

    private void updateNumberOfUnitCardsInDeckLabel(int numberOfAdd) {
        int x = Integer.parseInt(numberOfUnitCards.getText());
        numberOfUnitCards.setText(String.valueOf(x + numberOfAdd));
    }

    private void updateNumberOfUnitCardsInDeckLabel() {
        int x = 0;
        for (Card card : user.getCardInventory().getCurrentFaction().getDeck()) {
            if (card instanceof UnitCard) {
                x++;
            }
        }
        numberOfUnitCards.setText(String.valueOf(x));
    }

    private void updateTotalCardsInDeckLabel() {
        totalCardsInDeck.setText(String.valueOf(user.getCardInventory().getCurrentFaction().getDeck().size()));
    }

    public void addToDeckCard(CardsInPreGame cardsInPreGame) {

        deckGridPane.add(cardsInPreGame, cardsInDeck.size() % 3, cardsInDeck.size() / 3);
        cardsInDeck.add(cardsInPreGame);

        preGameSetDeckController.addToDeckCard(user, cardsInPreGame.getCard());
        updateTotalCardsInDeckLabel();
        if (cardsInPreGame.getCard() instanceof UnitCard) {
            updateNumberOfUnitCardsInDeckLabel(1);
        }
        if (cardsInPreGame.getCard() instanceof SpellCard) {
            updateNumberOfSpecialCardsInDeckLabel(1);
        }
    }

    public void addToStorageCard(CardsInPreGame cardsInPreGame) {

        storageGridPane.add(cardsInPreGame, cardsInStorage.size() % 3, cardsInStorage.size() / 3);
        cardsInStorage.add(cardsInPreGame);

        preGameSetDeckController.addToStorageCard(user, cardsInPreGame.getCard());
    }

    private void updateGridPaneDeckCard() {

        deckGridPane.getChildren().clear();
        for (int i = 0; i < cardsInDeck.size(); i++) {
            deckGridPane.add(cardsInDeck.get(i), i % 3, i / 3);
        }
    }

    private void updateGridPaneStorageCard() {

        storageGridPane.getChildren().clear();
        for (int i = 0; i < cardsInStorage.size(); i++) {
            storageGridPane.add(cardsInStorage.get(i), i % 3, i / 3);
        }
    }

    public void removeFromDeckCard(CardsInPreGame cardsInPreGame) {

        for (int i = 0; i < cardsInDeck.size(); i++) {
            if (cardsInDeck.get(i) == cardsInPreGame) {

                cardsInDeck.remove(i);
                updateGridPaneDeckCard();

                preGameSetDeckController.removeCardFromDeck(user, cardsInPreGame.getCard());
                updateTotalCardsInDeckLabel();
                if (cardsInPreGame.getCard() instanceof UnitCard) {
                    updateNumberOfUnitCardsInDeckLabel(-1);
                }
                if (cardsInPreGame.getCard() instanceof SpellCard) {
                    updateNumberOfSpecialCardsInDeckLabel(-1);
                }
                break;
            }
        }
    }

    public void removeFromStorageCard(CardsInPreGame cardsInPreGame) {
        for (int i = 0; i < cardsInStorage.size(); i++) {
            if (cardsInStorage.get(i) == cardsInPreGame) {

                cardsInStorage.remove(i);
                updateGridPaneStorageCard();

                preGameSetDeckController.removeCardFromStorage(user, cardsInPreGame.getCard());
                break;
            }
        }
    }

    public void updateLeaderCardImage() {
        leaderCardImage.setImage(new Image(getClass().getResource(user.getCardInventory().getCurrentFaction().getCurrentLeader().getDescriptionCardImagePath()).toString()));
    }

    public PreGameSetDeckController getPreGameSetDeckController() {
        return preGameSetDeckController;
    }
}

class LeaderCardInPreGame extends Rectangle {
    private PreGameSetDeckViewController preGameSetDeckViewController;
    private User user;
    private Pane pane;
    private GridPane gridPane;
    private LeaderCard leaderCard;

    public LeaderCardInPreGame(String imagePath, double width, double height, Pane pane, GridPane gridPane, User user,
                               PreGameSetDeckViewController preGameSetDeckViewController, LeaderCard leaderCard) {

        this.preGameSetDeckViewController = preGameSetDeckViewController;
        this.user = user;
        this.pane = pane;
        this.gridPane = gridPane;
        this.setWidth(width);
        this.setHeight(height);
        this.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(imagePath)))));
        this.leaderCard = leaderCard;
    }

    {
        setMouse();
    }

    private void setMouse() {
        setMouseClick(this);
    }

    private void setMouseClick(LeaderCardInPreGame leaderCardInPreGame) {
        leaderCardInPreGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                preGameSetDeckViewController.getPreGameSetDeckController().setLeaderFaction(user, leaderCard);
                preGameSetDeckViewController.updateLeaderCardImage();

                pane.getChildren().remove(gridPane);
                PreGameSetDeckViewController.canTouchScreen = true;
            }
        });
    }
}

class FactionCard extends Rectangle {

    private PreGameSetDeckViewController preGameSetDeckViewController;
    private User user;
    private Pane pane;
    private GridPane gridPane;
    private String imagePath;

    public FactionCard(String imagePath, double width, double height, Pane pane, GridPane gridPane, User user,
                       PreGameSetDeckViewController preGameSetDeckViewController) {

        this.preGameSetDeckViewController = preGameSetDeckViewController;
        this.user = user;
        this.pane = pane;
        this.gridPane = gridPane;
        this.imagePath = imagePath;
        this.setWidth(width);
        this.setHeight(height);
        this.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(imagePath)))));
    }

    {
        setMouse();
    }

    private void setMouse() {
        setMouseClick(this);
    }

    private void setMouseClick(FactionCard factionCard) {
        factionCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_monsters.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getMonsters());

                } else if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_nilfgaard.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getNilfgaardEmpire());

                } else if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_realms.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getNorthernRealms());

                } else if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_scoiatael.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getScoiaTael());

                } else if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_skellige.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getSkellige());
                }  else if (Objects.equals(imagePath, "/Pics/Icons/FactionIcons/deck_back_sevenKingdoms.jpg")) {

                    preGameSetDeckViewController.getPreGameSetDeckController().setFaction(user, user.getCardInventory().getSevenKingdoms());
                }

                preGameSetDeckViewController.loadFaction(user.getCardInventory().getCurrentFaction());
                pane.getChildren().remove(gridPane);

                PreGameSetDeckViewController.canTouchScreen = true;
            }
        });
    }
}

class CardsInPreGame extends Rectangle {

    private boolean isMouseHere = false;
    private final Card card;
    private boolean isInStorage;

    private PreGameSetDeckViewController preGameSetDeckViewController;

    public CardsInPreGame(String imagePath, double width, double height, Card card, boolean isInStorage, PreGameSetDeckViewController preGameSetDeckViewController) {

        this.card = card;
        this.setWidth(width);
        this.setHeight(height);
        this.isInStorage = isInStorage;
        this.preGameSetDeckViewController = preGameSetDeckViewController;
        this.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(imagePath)))));
    }

    {
        setMouse();
    }

    private void setMouse() {

        setMouseClick(this);
        setMouseMoved(this);
        setMouseExited(this);

    }

    private void setMouseClick(CardsInPreGame cardsInPreGame) {
        cardsInPreGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (isInStorage) {

                    preGameSetDeckViewController.removeFromStorageCard(cardsInPreGame);
                    preGameSetDeckViewController.addToDeckCard(cardsInPreGame);

                    cardsInPreGame.isInStorage = false;

                } else {

                    preGameSetDeckViewController.removeFromDeckCard(cardsInPreGame);
                    preGameSetDeckViewController.addToStorageCard(cardsInPreGame);

                    cardsInPreGame.isInStorage = true;
                }

            }
        });
    }

    private void setMouseMoved(CardsInPreGame cardsInPreGame) {
        cardsInPreGame.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (!isMouseHere) {
                    cardsInPreGame.setStyle("-fx-stroke: yellow; -fx-stroke-width: 3;");
                    isMouseHere = true;
                }
            }
        });
    }

    private void setMouseExited(CardsInPreGame cardsInPreGame) {
        cardsInPreGame.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isMouseHere = false;
                cardsInPreGame.setStyle(null);
            }
        });
    }


    public Card getCard() {
        return card;
    }

}