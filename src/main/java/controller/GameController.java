package controller;
import Model.Game.GameObject;
import Model.InGameObjects.Abilities.SpellAbilities.SpellDecoyAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitMoraleBoostAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitTightBondAbility;
import Model.InGameObjects.Board.*;
import Model.InGameObjects.Cards.*;
import Model.InGameObjects.Abilities.FactionAbility.NilfgaardAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitMedicAbility;
import Model.Game.Player;
import Model.Package.*;
import javafx.animation.PauseTransition;
import Enum.*;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameController {

    public static GameObject currentGameObject;

    private final int notificationDuration = 2000;

    private final int transitionDuration = 1500;

    private final Player player;

    public GameController(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void endGame() {

        writeEndOfGameHistory();

        boolean hasPlayerWon = false;
        boolean isResultDraw = false;
        if (getPlayer().getGameObject().getPlayer1().getRemainingGems() == 0 && getPlayer().getGameObject().getPlayer2().getRemainingGems() == 0) {
            isResultDraw = true;
            getPlayer().getUser().getGameInfo().addDrawCount(1);
        }
        else if (getPlayer().getOpponent().getRemainingGems() == 0) {
            hasPlayerWon = true;
            getPlayer().getUser().getGameInfo().addVictoryCount(1);
        }
        else {
            getPlayer().getUser().getGameInfo().addDefeatCount(1);
        }

        getPlayer().getGameObject().getGameHistory().setGameFinished(true);

        getPlayer().getUser().getUserGameHistories().add(getPlayer().getGameObject().getGameHistory());
        getPlayer().getUser().getGameInfo().calculateHighestScore();
        getPlayer().getUser().getGameInfo().calculateScore();

        getPlayer().getUser().removeAllCardsGraphics();
        getPlayer().getOpponent().getUser().removeAllCardsGraphics();


        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration));
        boolean finalHasPlayerWon = hasPlayerWon;
        boolean finalIsResultDraw = isResultDraw;
        pauseTransition.setOnFinished(event -> {
            getPlayer().getGameObject().getGameViewController().showEndGameResult(finalHasPlayerWon, finalIsResultDraw);
        });
        pauseTransition.play();
    }

    public void closeGame() {
        getPlayer().getGameObject().getPlayer1().getUser().setCurrentGame(null);
        getPlayer().getGameObject().getPlayer2().getUser().setCurrentGame(null);

        getPlayer().getGameObject().getPlayer1().getLeaderCard().setLeaderPlayed(false);
        getPlayer().getGameObject().getPlayer2().getLeaderCard().setLeaderPlayed(false);

        getPlayer().getBoard().removeCardsGraphics();
        getPlayer().getOpponent().getBoard().removeCardsGraphics();

        getPlayer().getUser().setOpponentUser(null);

        Client.client.sendUserUpdateForGameHistory(getPlayer().getUser());
        Client.client.endGame(getPlayer().getUser().getUsername());
    }

    private void writeEndOfGameHistory() {
        if (getPlayer().getGameObject().getPlayer1().getRemainingGems() == 0 && getPlayer().getGameObject().getPlayer2().getRemainingGems() == 0) {
            getPlayer().getGameObject().getGameHistory().setWinner(null);
            getPlayer().getGameObject().getGameHistory().setResultDraw(true);
        }
        else if (getPlayer().getRemainingGems() == 0) {
            getPlayer().getGameObject().getGameHistory().setWinner(getPlayer().getOpponent().getUser());
        }
        else if (getPlayer().getOpponent().getRemainingGems() == 0) {
            getPlayer().getGameObject().getGameHistory().setWinner(getPlayer().getUser());
        }
    }

    public void endRound(int playerTotalScore, int opponentTotalScore, boolean isPlayerTurn) {
        if (getPlayer().equals(getPlayer().getGameObject().getPlayer1())) {
            getPlayer().getGameObject().getGameHistory().getUser1EveryRoundScore().add(playerTotalScore);
            getPlayer().getGameObject().getGameHistory().getUser2EveryRoundScore().add(opponentTotalScore);
        }
        else {
            getPlayer().getGameObject().getGameHistory().getUser2EveryRoundScore().add(playerTotalScore);
            getPlayer().getGameObject().getGameHistory().getUser1EveryRoundScore().add(opponentTotalScore);
        }

        if (!isOnlyOneFactionAbilityNilfgaard() || opponentTotalScore == -1 || playerTotalScore == -1) {
            checkForRoundWinner(playerTotalScore, opponentTotalScore);
        }
        else {
            if (getPlayer().getFactionAbility() instanceof NilfgaardAbility)
                getPlayer().getFactionAbility().applyAbility(0);
            else
                getPlayer().getOpponent().getFactionAbility().applyAbility(0);
        }

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration));
        pauseTransition.setOnFinished(event -> {
            if (getPlayer().getOpponent().getRemainingGems() == 0 || getPlayer().getRemainingGems() == 0)
                endGame();

            else setUpNextRound(isPlayerTurn);
        });
        pauseTransition.play();
    }

    public boolean isOnlyOneFactionAbilityNilfgaard() {
        if (getPlayer().getFactionAbility().canAbilityBeTriggered() && getPlayer().getFactionAbility() instanceof NilfgaardAbility)
            return true;
        else if (getPlayer().getOpponent().getFactionAbility().canAbilityBeTriggered() && getPlayer().getOpponent().getFactionAbility() instanceof NilfgaardAbility)
            return true;

        return false;
    }

    private void checkForRoundWinner(int playerTotalScore, int opponentTotalScore) {
        if (playerTotalScore > opponentTotalScore) currentPlayerIsWinner();
        else if (playerTotalScore < opponentTotalScore) currentPlayerIsLoser();
        else currentPlayerHasDraw();
    }

    private void currentPlayerIsWinner() {
        getPlayer().getOpponent().setRemainingGems(getPlayer().getOpponent().getRemainingGems() - 1);

        getPlayer().getBoard().getOpponentInformationBar().breakGem();

        getPlayer().getOpponent().getBoard().getPlayerInformationBar().breakGem();

        getPlayer().getGameObject().getGameViewController().announceRoundWon();
    }

    private void currentPlayerIsLoser() {
        getPlayer().setRemainingGems(getPlayer().getRemainingGems() - 1);

        getPlayer().getBoard().getPlayerInformationBar().breakGem();

        getPlayer().getOpponent().getBoard().getOpponentInformationBar().breakGem();

        getPlayer().getGameObject().getGameViewController().announceRoundLost();
    }

    private void currentPlayerHasDraw() {
        getPlayer().setRemainingGems(getPlayer().getRemainingGems() - 1);
        getPlayer().getOpponent().setRemainingGems(getPlayer().getOpponent().getRemainingGems() - 1);

        getPlayer().getBoard().getOpponentInformationBar().breakGem();
        getPlayer().getBoard().getPlayerInformationBar().breakGem();

        getPlayer().getOpponent().getBoard().getOpponentInformationBar().breakGem();
        getPlayer().getOpponent().getBoard().getPlayerInformationBar().breakGem();

        getPlayer().getGameObject().getGameViewController().announceRoundDraw();
    }

    public void applyFactionAbilities() {
        Player player1 = getPlayer().getGameObject().getPlayer1();
        Player player2 = getPlayer().getGameObject().getPlayer2();

        if (player1.getFactionAbility().canAbilityBeTriggered() &&
                    !(player1.getFactionAbility() instanceof NilfgaardAbility)) {

            if (player2.getFactionAbility().canAbilityBeTriggered() &&
                    !(player2.getFactionAbility() instanceof NilfgaardAbility)) {

                    player2.getFactionAbility().applyAbility(notificationDuration * 4);
            }

            player1.getFactionAbility().applyAbility(notificationDuration * 3);
        }
        else if (player2.getFactionAbility().canAbilityBeTriggered() &&
                !(player2.getFactionAbility() instanceof NilfgaardAbility)){
            player2.getFactionAbility().applyAbility(notificationDuration * 3);
        }
    }

    public void setUpNextRound(boolean isPlayerTurn) {

        getPlayer().getGameObject().getGameViewController().giveGlowingTurnRectangleTo(isPlayerTurn);

        getPlayer().getGameObject().addToRoundNumber();

        getPlayer().getGameObject().getGameHistory().setRoundsPlayed(
                getPlayer().getGameObject().getRoundNumber());

        applyFactionAbilities();

        discardCurrentCards();

        getPlayer().getBoard().getPlayerInformationBar().removePassedIconFromPane();
        getPlayer().getBoard().getOpponentInformationBar().removePassedIconFromPane();

        getPlayer().getBoard().updateAllSections();
        getPlayer().getOpponent().getBoard().updateAllSections();

        getPlayer().getBoard().putNextRoundCardsOfRows();
        getPlayer().getOpponent().getBoard().putNextRoundCardsOfRows();

        PauseTransition pause = new PauseTransition(Duration.millis(notificationDuration));
        pause.setOnFinished(event -> {
            getPlayer().getGameObject().getGameViewController().announceRoundBegins();
        });
        pause.play();


        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration * 2));
        pauseTransition.setOnFinished(event -> {
            if (!isPlayerTurn) {
                getPlayer().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);
                getPlayer().getOpponent().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
                getPlayer().getGameObject().getGameViewController().announceOpponentTurn();
            }
            else {
                getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
                getPlayer().getOpponent().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);
                getPlayer().getGameObject().getGameViewController().announcePlayerTurn();
            }
        });
        pauseTransition.play();
    }

    private void discardCurrentCards() {
        for (BoardSection boardSection : getPlayer().getBoard().getBoardSections()) {
            if (!canBoardSectionDiscardInEndOfRound(boardSection))
                continue;

            ArrayList<Card> cards = new ArrayList<>();
            cards.addAll(boardSection.getCardsOnSection());
            boardSection.resetBoardSection();

            if (boardSection.getOwner().equals(getPlayer())) {
                for (Card card : cards) {
                    putFriendlyCardInDiscardPile(card, card.getCurrentPlaceInGame(), true);
                }
            }

            else {
                for (Card card : cards) {
                    putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), true);
                }
            }
        }
    }

    private boolean canBoardSectionDiscardInEndOfRound(BoardSection boardSection) {
        return !(boardSection instanceof HandArea || boardSection instanceof DeckArea
                || boardSection instanceof DiscardPileArea || boardSection instanceof LeaderCardArea);
    }

    public void receiveTurn() {

        getPlayer().getGameObject().getGameViewController().announcePlayerTurn();

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration));
        pauseTransition.setOnFinished(event -> {
            getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
            getPlayer().getGameObject().getGameViewController().moveButtonsToFront();
            getPlayer().getGameObject().getGameViewController().giveGlowingTurnRectangleTo(true);
        });
        pauseTransition.play();
    }

    public void giveTurn() {
        if (getPlayer().getOpponent().getPlayerState().equals(PlayerState.PASSED)) {
            if (getPlayer().getPlayerState().equals(PlayerState.PASSED)) {
                int playerTotalScore = getPlayer().getBoard().getFriendlyTotalScoreArea().getTotalScore();
                int opponentTotalScore = getPlayer().getBoard().getOpponentTotalScoreArea().getTotalScore();
                Client.client.sendPlayerTurnPackage(0,0, true);
                endRound(playerTotalScore, opponentTotalScore, false);
            }
            return;
        }

        if (!getPlayer().getPlayerState().equals(PlayerState.PASSED)) {
            getPlayer().getGameObject().getGameViewController().announceOpponentTurn();
            getPlayer().getGameObject().getGameViewController().giveGlowingTurnRectangleTo(false);
        }

        Client.client.sendPlayerTurnPackage(0,0, getPlayer().getPlayerState().equals(PlayerState.PASSED));
    }

    public void giveTurnWithDelay() {
        PauseTransition pause = new PauseTransition(Duration.millis(transitionDuration * 1.5));
        pause.setOnFinished(event -> {
            giveTurn();
        });
        pause.play();
    }

    public void passRound() {
        if (getPlayer().getPlayerState().equals(PlayerState.PARALYZED) ||
                getPlayer().getPlayerState().equals(PlayerState.PASSED) ||
                getPlayer().getPlayerState().equals(PlayerState.WAITING_FOR_OPPONENT))
            return;

        getPlayer().setPlayerState(PlayerState.PASSED);
        getPlayer().getGameObject().getGameViewController().announceRoundPassed();
    }

    public void setUpPlayerHandCards() {
        getPlayer().setRandomHand();
    }

    public void setUpPlayerDeckCards() {
        getPlayer().setInGameDeck();
    }

    public void playACardFromHand(BoardSection cardDestination) {

        Card card = getPlayer().getSelectedCard();

        if (!isPlaceValidForCardToPut(card, cardDestination)) {
            resetSelectedCard();
            getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
            return;
        }

        resetSelectedCard();

        moveCardProtocol(card, getPlayer().getBoard().getHandArea(), cardDestination, CardStates.ROW_IN_GAME_PLAYED);
        if (!cardDestination.getCardPlace().equals(CardPlace.HORN_AREA))
            Client.client.sendMoveCardPackage(card, getPlayer().getBoard().getHandArea(), cardDestination, CardStates.ROW_IN_GAME_PLAYED);
        else
            Client.client.sendMoveCardPackage(card, getPlayer().getBoard().getHandArea(),
                    ((HornArea)cardDestination).getHornRow(), CardStates.ROW_IN_GAME_PLAYED);


        applyCardAbilityWithDelay(card, cardDestination);

        if (!isMedicCardWaitingForPlayer(card)) {
            changePlayersStates();
            giveTurnWithDelay();
        }
    }

    private boolean isMedicCardWaitingForPlayer(Card card) {
        for (CardAbility cardAbility : card.getCardAbilities()) {
            if (cardAbility instanceof UnitMedicAbility)
                if (isDiscardPileMedicallyEmpty()) {
                    return true;
                }
        }
        return false;
    }

    private boolean isDiscardPileMedicallyEmpty() {
        for (Card card : getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection()) {
            if (card instanceof UnitCard && !((UnitCard) card).isHero())
                return true;
        }
        return false;
    }

    private void changePlayersStates() {
        if (!getPlayer().getOpponent().getPlayerState().equals(PlayerState.PASSED)) {
            getPlayer().getOpponent().setPlayerState(PlayerState.PARALYZED);
            getPlayer().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);
        }
        else {
            // because if player's hand is not empty, he can continue playing cards
            getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
        }

        if (getPlayer().getHand().isEmpty()) {
            getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND); // because in passRound the state will be corrected.
            passRound();
        }
    }

    public boolean isPlaceValidForCardToPut(Card card, BoardSection boardSection) {
        if (card.getCardPlace().equals(CardPlace.FRIENDLY_CLOSE_RANGED)) {
            if (boardSection.getCardPlace().equals(CardPlace.FRIENDLY_CLOSE) ||
                    boardSection.getCardPlace().equals(CardPlace.FRIENDLY_RANGED))
                return true;
            else
                return false;
        }
        else if (card.getCardPlace().equals(CardPlace.HORN_AREA)) {
            if (boardSection instanceof HornArea) {
                return ((HornArea) boardSection).isEmpty();
            }
            return false;
        }
        else {
            return card.getCardPlace().equals(boardSection.getCardPlace());
        }
    }

    public void resetSelectedCard() {
        Card card = getPlayer().getSelectedCard();
        getPlayer().getGameObject().getGameViewController().removeSelectedCardDescriptionAndExplanation(card);
        getPlayer().setSelectedCard(null);
    }

    //////////////////////////////////////////////// moving card from one board section to another protocol

    public void moveCardProtocol(Card card, BoardSection beginning, BoardSection destination, CardStates cardState) {
        removeCardFromBoardSection(card, beginning);
        setCardStates(card, cardState);

        getPlayer().setParalyzedByAnimation(true);
        getPlayer().getGameObject().getGameViewController().playCardTransition(card, destination);
    }

    public void removeCardFromBoardSection(Card card, BoardSection boardSection) {
        boardSection.removeFromCardsOnSection(card);
        boardSection.updateSection();
    }

    public void setCardStates(Card card, CardStates cardState) {
        card.setPreviousState(card.getCurrentState());
        card.setCurrentState(cardState);
    }

    public  void addCardToBoardSection(Card card, BoardSection boardSection) {
        card.setCurrentPlaceInGame(boardSection);
        boardSection.addToCardsOnSection(card);
        boardSection.updateSection();
    }

    /////////////////////////////////////////////////

    public void applyCardAbilityWithDelay(Card card, BoardSection boardSection) {
        PauseTransition pause = new PauseTransition(Duration.millis(transitionDuration));
        pause.setOnFinished(event -> {
            card.applyAbility();
            boardSection.updateSection();
            getPlayer().getBoard().updateAllSections();
        });
        pause.play();
    }

    public void putFriendlyCardInDiscardPile(Card card, BoardSection boardSection, boolean isEndRound) {
        if (card instanceof UnitCard) {
            ((UnitCard) card).setCurrentPower(((UnitCard) card).getBasePower());
            card.getRowCard().setPowerLabelText();
        }


        moveCardProtocol(card, boardSection, getPlayer().getBoard().getFriendlyDiscardPileArea(),
                CardStates.ROW_IN_GAME_DISCARD_PILE);
        if (!isEndRound)
            Client.client.sendMoveCardPackage(card, boardSection, getPlayer().getBoard().getFriendlyDiscardPileArea(),
                CardStates.ROW_IN_GAME_DISCARD_PILE);
    }

    public void putOpponentCardInDiscardPile(Card card, BoardSection boardSection, boolean isEndRound) {

        if (card instanceof UnitCard) {
            ((UnitCard) card).setCurrentPower(((UnitCard) card).getBasePower());
            card.getRowCard().setPowerLabelText();
        }


        moveCardProtocol(card, boardSection, getPlayer().getBoard().getOpponentDiscardPileArea(),
                        CardStates.ROW_IN_GAME_DISCARD_PILE);
        if(!isEndRound)
            Client.client.sendMoveCardPackage(card, boardSection, getPlayer().getBoard().getOpponentDiscardPileArea(),
                CardStates.ROW_IN_GAME_DISCARD_PILE);
    }

    public void replaceUnitCardWithDecoy(Card cardToReplace) {
        if (cardToReplace instanceof UnitCard) {
            if (((UnitCard) cardToReplace).isHero()) {
                return;
            }
        }
        else {
            return;
        }

        Card decoyCard = getPlayer().getSelectedCard();

        resetSelectedCard();

        ((SpellDecoyAbility)decoyCard.getCardAbilities().get(0)).swapDecoyWithCard(cardToReplace);

        cardToReplace.setOwner(getPlayer());

        changePlayersStates();
        giveTurnWithDelay();
    }

    public void showArrayOfCards(ArrayList<Card> cards) {
        ScrollBox scrollBox = new ScrollBox(getPlayer(),
                cards, 0, true);

        getPlayer().setPlayerState(PlayerState.LOOKING_AT_CARDS);

        getPlayer().getBoard().setScrollBox(scrollBox);
        getPlayer().getGameObject().getGameViewController().showScrollShowBox(scrollBox);
    }

    public void stopPlayerFromLookingAtCards() {
        getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
        getPlayer().getGameObject().getGameViewController().removeScrollBox(getPlayer().getBoard().getScrollBox());
    }


    public void showArrayOfCardsOfLeaderAbility(ArrayList<Card> cards) {
        ScrollBox scrollBox = new ScrollBox(getPlayer(),
                cards, 0, true);

        getPlayer().getBoard().setScrollBox(scrollBox);
        getPlayer().getGameObject().getGameViewController().showScrollShowBox(scrollBox);
    }

    public void stopShowingScrollBoxOfLeaderAbility() {
        getPlayer().getGameObject().getGameViewController().removeScrollBox(getPlayer().getBoard().getScrollBox());
        getPlayer().getGameController().finishLeaderAbility();
    }

    public void selectCard(Card card) {
        if (getPlayer().getSelectedCard() != null) {
            resetSelectedCard();
        }
        getPlayer().setSelectedCard(card);
        getPlayer().getGameObject().getGameViewController().displaySelectedCardDescriptionExplanation(card);
        getPlayer().setPlayerState(PlayerState.CHOOSING_ROW_TO_PUT);
    }

    public void swapInitialHandCard(Card card) {

        int remainingChoices = getPlayer().getBoard().getScrollBox().getCardsToSelect();
        getPlayer().getGameObject().getGameViewController().removeScrollBox(getPlayer().getBoard().getScrollBox());

        Card randomCard = getPlayer().getOneRandomCardFromDeck();
        randomCard.setPreviousState(CardStates.ROW_IN_GAME_DECK);
        randomCard.setCurrentState(CardStates.DES_IN_GAME_SELECTABLE);

        getPlayer().getHand().add(randomCard);
        getPlayer().getBoard().getHandArea().updateSection();
        randomCard.setCurrentPlaceInGame(getPlayer().getBoard().getHandArea());

        returnCardToDeckFromBoardSection(card, getPlayer().getBoard().getHandArea());

        getPlayer().getGameObject().getGameViewController().playSfxFromString("redraw");

        if (--remainingChoices != 0) {
            getPlayer().getBoard().setScrollBox(new ScrollBox(getPlayer(),
                    getPlayer().getBoard().getHandArea().getCardsOnSection(), remainingChoices, true));

            getPlayer().getGameObject().getGameViewController().showScrollShowBox(getPlayer().getBoard().getScrollBox());
        }
        else {
            stopSwappingInitialHand();
        }
    }

    private void returnCardToDeckFromBoardSection(Card card, BoardSection boardSection) {
        boardSection.removeFromCardsOnSection(card);
        boardSection.updateSection();
        if (boardSection instanceof HandArea) {
            getPlayer().getHand().remove(card);
        }

        card.setCurrentPlaceInGame(getPlayer().getBoard().getFriendlyDeckArea());
        card.setPreviousState(card.getCurrentState());
        card.setCurrentState(CardStates.ROW_IN_GAME_DECK);
        card.removeGraphicalFields();

        getPlayer().getDeck().add(card);
        getPlayer().getBoard().getFriendlyDeckArea().addToCardsOnSection(card);
        getPlayer().getBoard().getFriendlyDeckArea().updateSection();
    }

    public void stopSwappingInitialHand() {
        getPlayer().getGameObject().getGameViewController().removeScrollBox(getPlayer().getBoard().getScrollBox());

        PauseTransition pause1 = new PauseTransition(Duration.millis(notificationDuration));
        PauseTransition pause2 = new PauseTransition(Duration.millis(notificationDuration * 2));
        PauseTransition pause3 = new PauseTransition(Duration.millis(notificationDuration * 3));

        if (getPlayer().getGameObject().getGameViewController().isAmIFirst())
            getPlayer().getGameObject().getGameViewController().announcePlayerGoesFirst();
        else
            getPlayer().getGameObject().getGameViewController().announceOpponentGoesFirst();

        pause1.setOnFinished(event -> {
            getPlayer().getGameObject().getGameViewController().announceRoundBegins();
        });
        pause1.play();

        pause2.setOnFinished(event -> {
            if (getPlayer().getGameObject().getGameViewController().isAmIFirst())
                getPlayer().getGameObject().getGameViewController().announcePlayerTurn();
            else
                getPlayer().getGameObject().getGameViewController().announceOpponentTurn();
        });
        pause2.play();

        pause3.setOnFinished(event -> {
            if (getPlayer().getGameObject().getGameViewController().isAmIFirst() && getPlayer().isHasOpponentSwappedInitialHand()) {
                getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
            } else {
                getPlayer().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);
            }

            Client.client.sendPlayerHandPackage(getPlayer().getHand());

            for (Card card : getPlayer().getBoard().getHandArea().getCardsOnSection()) {
                card.setCurrentState(CardStates.ROW_IN_GAME_HAND);
            }
        });
        pause3.play();
    }

    public void showChooseScrollBox(ArrayList<Card> unitCards) {
        ScrollBox scrollBox = new ScrollBox(getPlayer(),
                unitCards, 1, false);

        getPlayer().getBoard().setScrollBox(scrollBox);

        getPlayer().getGameObject().getGameViewController().showScrollShowBox(scrollBox);
    }

    public void scrollBoxReviveDiscardedCard(Card card) {
        getPlayer().getGameObject().getGameViewController().removeScrollBox(getPlayer().getBoard().getScrollBox());
        reviveCardFromDiscPile(card);
    }

    public void reviveCardFromDiscPile(Card card) {

        BoardSection boardSection = getCardBoardSection(card);

        moveCardProtocol(card, getPlayer().getBoard().getFriendlyDiscardPileArea(),
                boardSection, CardStates.ROW_IN_GAME_PLAYED);
        Client.client.sendMoveCardPackage(card, getPlayer().getBoard().getFriendlyDiscardPileArea(), boardSection,
                CardStates.ROW_IN_GAME_PLAYED);

        getPlayer().getGameObject().getGameViewController().playSfxFromString("med");
        getPlayer().getGameObject().getGameViewController().playAnimationOnCard(card, "medic");

        Client.client.sendSfxAnimationPackage(card, "med", "medic");

        applyCardAbilityWithDelay(card, boardSection);

        if (!isMedicCardWaitingForPlayer(card)) {
            changePlayersStates();
            giveTurnWithDelay();
        }
    }

    public BoardSection getCardBoardSection(Card card) {
        if (card.getCardPlace().equals(CardPlace.FRIENDLY_CLOSE_RANGED))
            return getPlayer().getBoard().getFriendlyCloseArea();

        if (card.getCardPlace().equals(CardPlace.OPPONENT_CLOSE_RANGED))
            return getPlayer().getBoard().getOpponentCloseArea();

        for (BoardSection boardSection : getPlayer().getBoard().getBoardSections()) {
            if (boardSection.getCardPlace().equals(card.getCardPlace()))
                return boardSection;
        }

        return null;
    }

    public void playLeaderCard(Card card) {
        if (!isLeaderPlayable(card))
            return;

        resetSelectedCard();

        getPlayer().getLeaderCard().setLeaderPlayed(true);
        getPlayer().getLeaderCard().setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
        getPlayer().getGameObject().getGameViewController().giveDarkEffectToCard(getPlayer().getLeaderCard());

        card.applyAbility();
    }

    public boolean isLeaderPlayable(Card card) {
        LeaderCard leaderCard = (LeaderCard) card;

        if (leaderCard.isLeadersAbilityPassive())
            return false;

        if (leaderCard.isLeaderPlayed())
            return false;

        if (leaderCard.getCurrentState().equals(CardStates.ROW_IN_GAME_PLAYED))
            return false;


        return !getPlayer().getOpponent().getLeaderCard().getCardName().equals(CardNames.EMHYR_THE_WHITE_FLAME);
    }

    public void finishLeaderAbility() {
        changePlayersStates();
        giveTurnWithDelay();
    }

    public void failedLeaderAbility(Card card) {
        getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
        LeaderCard leaderCard = (LeaderCard) card;
        leaderCard.setLeaderPlayed(false);
        leaderCard.setCurrentState(CardStates.ROW_IN_GAME_HAND);
        getPlayer().getGameObject().getGameViewController().removeDarkEffectFromCard(card);
    }

    public void cheatGiveHomeLander(boolean isForPlayer) {
        Card homelander = CardFactory.getCopiedCheatCardByName(CardNames.HOMELANDER);
        homelander.createGraphicalFields();

        homelander.setPlayer(getPlayer());
        homelander.setPreviousState(CardStates.ROW_HIDDEN);
        homelander.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);

        if (isForPlayer) {
            homelander.setOwner(getPlayer());
            homelander.setCurrentPlaceInGame(getPlayer().getBoard().getFriendlyCloseArea());
        }
        else {
            homelander.setOwner(getPlayer().getOpponent());
            homelander.setCurrentPlaceInGame(getPlayer().getBoard().getOpponentCloseArea());
        }

        homelander.getCurrentPlaceInGame().addToCardsOnSection(homelander);
        homelander.getCurrentPlaceInGame().updateSection();

        getPlayer().getBoard().updateAllSections();

        if (isForPlayer)
            Client.client.sendCheatPackage(1);
    }

    public void cheatGiveButcher(boolean isForPlayer) {
        Card butcher = CardFactory.getCopiedCheatCardByName(CardNames.BUTCHER);
        butcher.createGraphicalFields();

        butcher.setPlayer(getPlayer());
        butcher.setPreviousState(CardStates.ROW_HIDDEN);
        butcher.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);

        if (isForPlayer) {
            butcher.setOwner(getPlayer());
            butcher.setCurrentPlaceInGame(getPlayer().getBoard().getFriendlyCloseArea());
        }
        else {
            butcher.setOwner(getPlayer().getOpponent());
            butcher.setCurrentPlaceInGame(getPlayer().getBoard().getOpponentCloseArea());
        }

        butcher.getCurrentPlaceInGame().addToCardsOnSection(butcher);
        butcher.getCurrentPlaceInGame().updateSection();

        getPlayer().getBoard().updateAllSections();

        if (isForPlayer)
            Client.client.sendCheatPackage(2);
    }

    public void cheatGiveThaler(boolean isForPlayer) {
        Card thaler = CardFactory.getCopiedCardByName(CardNames.THALER);
        thaler.createGraphicalFields();
        thaler.setOwner(getPlayer());
        thaler.setPlayer(getPlayer());
        thaler.setPreviousState(CardStates.ROW_HIDDEN);
        thaler.setCurrentState(CardStates.ROW_IN_GAME_HAND);
        thaler.setCurrentPlaceInGame(getPlayer().getBoard().getHandArea());
        thaler.getCurrentPlaceInGame().addToCardsOnSection(thaler);
        thaler.getCurrentPlaceInGame().updateSection();

        getPlayer().getBoard().updateAllSections();

        thaler.setId(5555);
        Client.client.sendAddCardPackage(thaler, thaler.getCurrentPlaceInGame(), thaler.getCurrentState(), false);
    }

    public void cheatAddToHand() {
        Card card = getPlayer().getOneRandomCardFromDeck();

        if (card != null) {
            card.setPreviousState(card.getPreviousState());
            card.setCurrentState(CardStates.ROW_IN_GAME_HAND);

            card.setCurrentPlaceInGame(getPlayer().getBoard().getHandArea());
            card.getCurrentPlaceInGame().addToCardsOnSection(card);

            card.getCurrentPlaceInGame().updateSection();
        }

        Client.client.sendRemoveCardPackage(card, getPlayer().getBoard().getFriendlyDeckArea(),
                                        card.getCurrentState(), false);
        Client.client.sendAddCardPackage(card, card.getCurrentPlaceInGame(), card.getCurrentState(), false);
    }

    public void cheatRecoverCrystal() {
        if (getPlayer().getRemainingGems() == 1) {
            getPlayer().setRemainingGems(2);
            getPlayer().getBoard().getPlayerInformationBar().recoverGem();
            getPlayer().getOpponent().getBoard().getOpponentInformationBar().recoverGem();
            Client.client.sendCheatPackage(3);
        }
    }

    public void cheatWinRound() {
        endRound(1, -1, false);

        Client.client.sendCheatPackage(4);
    }

    public void cheatLoseRound() {
        endRound(-1, 1, false);

        Client.client.sendCheatPackage(5);
    }













    ///////////////////// server package receiver


    private void serverUpdateBoard() {
        Platform.runLater(() -> getPlayer().getBoard().updateAllSections());
    }

    public void serverMoveCard(ServerPackage serverPackage) {
        MoveCardPackage moveCardPackage = (MoveCardPackage) serverPackage.object;

        Card card = getCardById(moveCardPackage.getCardId());
        card.setPlayer(getPlayer());

        BoardSection beginning = getReversedBoardSectionByName(moveCardPackage.getBeginning());

        if (beginning.getCardPlace().equals(CardPlace.OPPONENT_HAND)) {
            getPlayer().getOpponent().getHand().remove(card);
        }

        card.setCurrentPlaceInGame(beginning);

        BoardSection destination = getReversedBoardSectionByName(moveCardPackage.getDestination());

        if (card.getCardName().equals(CardNames.HORN) || card.getCardName().equals(CardNames.MARDROEME)) {
            if (destination instanceof Row) {
                destination = ((Row)destination).getHornArea();
            }
        }

        CardStates cardState = CardStates.getCardStateFromString(moveCardPackage.getCardState());

        BoardSection finalDestination = destination;
        Platform.runLater(() -> moveCardProtocol(card, beginning, finalDestination, cardState));
        serverUpdateBoard();

        if (card.getCardName().equals(CardNames.MARDROEME) && destination.getCardPlace().equals(CardPlace.HORN_AREA)) {
            ((HornArea)destination).getHornRow().setHasMardroeme(true);
        }
        else if (card.getCardName().equals(CardNames.ERMION) && destination instanceof Row) {
            ((Row) destination).setHasMardroeme(true);
        }
    }


    public void serverAddCard(ServerPackage serverPackage) {
        AddCardPackage addCardPackage = (AddCardPackage) serverPackage.object;
        Card card = getCardById(addCardPackage.getCardId());

        BoardSection destination = getReversedBoardSectionByName(addCardPackage.getDestination());

        if (card.getCardName().equals(CardNames.HORN) || card.getCardName().equals(CardNames.MARDROEME)) {
            if (destination instanceof Row) {
                destination = ((Row)destination).getHornArea();
            }
        }

        if (destination.getCardPlace().equals(CardPlace.OPPONENT_HAND)) {
            Platform.runLater(() -> getPlayer().getOpponent().getHand().add(card));
        }

        if (card.getCurrentPlaceInGame() != null) {
            Platform.runLater(() -> card.getCurrentPlaceInGame().removeFromCardsOnSection(card));
            Platform.runLater(() -> card.getCurrentPlaceInGame().updateSection());
        }

        card.setCurrentPlaceInGame(destination);

        CardStates cardState = CardStates.getCardStateFromString(addCardPackage.getCardState());

        if (addCardPackage.isForPuttingNextRound()) {
            ((Row)destination).addToUnitCardsToPutNextRound((UnitCard) card);
        }
        else {
            BoardSection finalDestination = destination;
            Platform.runLater(() -> addCardToBoardSection(card, finalDestination));
            Platform.runLater(() -> finalDestination.updateSection());
            card.setCurrentState(cardState);
        }
        serverUpdateBoard();
    }

    public void serverRemoveCard(ServerPackage serverPackage) {
        RemoveCardPackage removeCardPackage = (RemoveCardPackage) serverPackage.object;
        Card card = getCardById(removeCardPackage.getCardId());

        BoardSection destination = null;

        if (card.getCardName().equals(CardNames.HORN) || card.getCardName().equals(CardNames.MARDROEME)) {
            if (getReversedBoardSectionByName(removeCardPackage.getDestination()) instanceof Row)
                destination = ((Row) getReversedBoardSectionByName(removeCardPackage.getDestination())).getHornArea();
        } else {
            destination = getReversedBoardSectionByName(removeCardPackage.getDestination());
        }

        CardStates cardState = CardStates.getCardStateFromString(removeCardPackage.getCardState());

        if (removeCardPackage.isForPuttingNextRound()) {
            ((Row)destination).getUnitCardsToPutNextRound().remove((UnitCard) card);
        }
        else {
            BoardSection finalDestination = destination;
            Platform.runLater(() -> finalDestination.removeFromCardsOnSection(card));
            Platform.runLater(() -> finalDestination.updateSection());
            card.setCurrentState(cardState);
        }
        serverUpdateBoard();
    }

    public void serverPlaySfxAnimation(ServerPackage serverPackage) {
        SfxAnimationPackage sfxAnimationPackage = (SfxAnimationPackage) serverPackage.object;

        if (!sfxAnimationPackage.getSfxName().equals("non"))
            getPlayer().getGameObject().getGameViewController().playSfxFromString(sfxAnimationPackage.getSfxName());

        if (!sfxAnimationPackage.getAnimationName().equals("non"))
            Platform.runLater(() -> getPlayer().getGameObject().getGameViewController().playAnimationOnCard(getCardById(sfxAnimationPackage.getCardId()),
                    sfxAnimationPackage.getAnimationName()));

        serverUpdateBoard();
    }

    public void serverPlayerTurn(ServerPackage serverPackage) {
        PlayerTurnPackage playerTurnPackage = (PlayerTurnPackage) serverPackage.object;
        if (playerTurnPackage.isHasPassed()) {
            player.getOpponent().setPlayerState(PlayerState.PASSED);
            Platform.runLater(() -> getPlayer().getBoard().getOpponentInformationBar().addPassedIconToPane());

            if (getPlayer().getPlayerState().equals(PlayerState.PASSED)) {
                int playerTotalScore = getPlayer().getBoard().getFriendlyTotalScoreArea().getTotalScore();
                int opponentTotalScore = getPlayer().getBoard().getOpponentTotalScoreArea().getTotalScore();
                Platform.runLater(() -> endRound(playerTotalScore, opponentTotalScore, true));
                return;
            }
        }

        serverUpdateBoard();
        Platform.runLater(() -> receiveTurn());
    }

    public void serverPlayerHand(ServerPackage serverPackage) {
        getPlayer().setHasOpponentSwappedInitialHand(true);

        int[] cardIds = ((PlayerHandPackage) serverPackage.object).getCardIds();

        for (int index : cardIds) {
            Card card = getCardById(index);

            getPlayer().getOpponent().getHand().add(card);

            getPlayer().getOpponent().getDeck().remove(card);
        }

        Platform.runLater(() -> getPlayer().getBoard().getHandArea().updateSection());
        Platform.runLater(() -> getPlayer().getBoard().getFriendlyDeckArea().updateSection());

        if (!getPlayer().getPlayerState().equals(PlayerState.SWAPPING_INITIAL_HAND)) {
            if (getPlayer().getGameObject().getGameViewController().isAmIFirst())
                getPlayer().setPlayerState(PlayerState.CHOOSING_CARD_FROM_HAND);
            else
                getPlayer().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);
        }

        serverUpdateBoard();
    }

    public void serverWeatherAbility(ServerPackage serverPackage) {
        String animationName = ((WeatherAbilityPackage)serverPackage.object).getAnimationName();
        if (animationName.startsWith("overlay_"))
            Platform.runLater(() -> getPlayer().getGameObject().getGameViewController().playRowOverlayAnimation(animationName));
        else {
            Platform.runLater(() -> clearWeather());
        }
    }

    private void clearWeather() {
        ArrayList<Card> discardedCards = new ArrayList<>();

        for (Card card : getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            discardedCards.add(card);
        }

        for (Card card : discardedCards) {
            card.getPlayer().getGameController().
                    putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), false);
        }

        getPlayer().getGameObject().getGameViewController().playSfxFromString("clear");

        removeWeatherEffectFromPlayer(getPlayer());
    }

    private void removeWeatherEffectFromPlayer(Player player) {
        for (BoardSection boardSection : player.getBoard().getBoardSections()) {
            if (boardSection instanceof Row) {
                if (((Row) boardSection).getWeatherEffect() != null) {
                    boardSection.getChildren().remove(((Row) boardSection).getWeatherEffect());
                    ((Row) boardSection).setWeatherEffect(null);
                }
            }
        }
    }

    public Card getCardById(int cardId) {
        Card card0 = getPlayer().getGameObject().getCardById(cardId);
        if (card0 != null) {
            if (card0.getRowCard() == null)
                card0.createGraphicalFields();
            return card0;
        }
        else {
            if (cardId == 9999) {
                Card card = new UnitCard(CardNames.BERSERKER_MARDOREMED, CardPlace.FRIENDLY_CLOSE,
                        14, false, new ArrayList<CardAbility>() {{
                    add(new UnitMoraleBoostAbility());
                }});
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 9990) {
                Card card = new UnitCard(CardNames.YOUNG_BERSERKER_MARDOREMED, CardPlace.FRIENDLY_RANGED,
                        8, false, new ArrayList<CardAbility>() {{
                    add(new UnitTightBondAbility(CardTightBTypes.TRANSFORMED_YOUNG_BERSERKERS));
                }});
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 8888) {
                Card card = CardFactory.getCopiedCardByName(CardNames.HORN);
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);

                card.setCurrentPlaceInGame(getPlayer().getBoard().getOpponentDeckArea());
                card.getRowCard().setLayoutX(getPlayer().getBoard().getOpponentDeckArea().getLayoutX());
                card.getRowCard().setLayoutY(getPlayer().getBoard().getOpponentDeckArea().getLayoutY());
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 7777) {
                Card card = CardFactory.getCopiedCardByName(CardNames.CLEAR_WEATHER);
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);

                card.setCurrentPlaceInGame(getPlayer().getBoard().getOpponentDeckArea());
                card.getRowCard().setLayoutX(getPlayer().getBoard().getOpponentDeckArea().getLayoutX());
                card.getRowCard().setLayoutY(getPlayer().getBoard().getOpponentDeckArea().getLayoutY());
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 6666) {
                Card card = new UnitCard(CardNames.KAMBI_TRANSFORMED, CardPlace.FRIENDLY_CLOSE,
                        11, true, new ArrayList<CardAbility>());
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 6660) {
                System.out.println("gav daryaft shod");
                Card card = new UnitCard(CardNames.COW_TRANSFORMED, CardPlace.FRIENDLY_CLOSE,
                        8, false, new ArrayList<CardAbility>());
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            } else if (cardId == 5555) {
                Card card = CardFactory.getCopiedCardByName(CardNames.THALER);
                card.createGraphicalFields();
                card.setPlayer(getPlayer());
                card.setOwner(getPlayer().getOpponent());
                card.setCurrentState(CardStates.ROW_IN_GAME_HAND);

                card.setCurrentPlaceInGame(getPlayer().getBoard().getOpponentHand());
                card.getRowCard().setLayoutX(getPlayer().getBoard().getOpponentDeckArea().getLayoutX());
                card.getRowCard().setLayoutY(getPlayer().getBoard().getOpponentDeckArea().getLayoutY());
                getPlayer().getGameObject().getAllCards().add(card);
                return card;
            }
        }

        return null;
    }

    public BoardSection getReversedBoardSectionByName(String boardSectionName) {
        if (boardSectionName.equals("HAND_AREA")) {
            return getPlayer().getBoard().getOpponentHand();
        }
        else if (boardSectionName.equals("WEATHER_CARDS_AREA")) {
            return getPlayer().getBoard().getWeatherCardArea();
        }
        else if (boardSectionName.equals("FRIENDLY_CLOSE")) {
            return getPlayer().getBoard().getOpponentCloseArea();
        }
        else if (boardSectionName.equals("FRIENDLY_RANGED")) {
            return getPlayer().getBoard().getOpponentRangedArea();
        }
        else if (boardSectionName.equals("FRIENDLY_SIEGE")) {
            return getPlayer().getBoard().getOpponentSiegeArea();
        }
        else if (boardSectionName.equals("FRIENDLY_LEADER")) {
            return getPlayer().getBoard().getOpponentLeaderCardArea();
        }
        else if (boardSectionName.equals("FRIENDLY_DISCARD_PILE_AREA")) {
            return getPlayer().getBoard().getOpponentDiscardPileArea();
        }
        else if (boardSectionName.equals("FRIENDLY_DECK_AREA")) {
            return getPlayer().getBoard().getOpponentDeckArea();
        }
        else if (boardSectionName.equals("OPPONENT_CLOSE")) {
            return getPlayer().getBoard().getFriendlyCloseArea();
        }
        else if (boardSectionName.equals("OPPONENT_RANGED")) {
            return getPlayer().getBoard().getFriendlyRangedArea();
        }
        else if (boardSectionName.equals("OPPONENT_SIEGE")) {
            return getPlayer().getBoard().getFriendlySiegeArea();
        }
        else if (boardSectionName.equals("OPPONENT_LEADER")) {
            return getPlayer().getBoard().getFriendlyLeaderCardArea();
        }
        else if (boardSectionName.equals("OPPONENT_DISCARD_PILE_AREA")) {
            return getPlayer().getBoard().getFriendlyDiscardPileArea();
        }
        else if (boardSectionName.equals("OPPONENT_DECK_AREA")) {
            return getPlayer().getBoard().getFriendlyDeckArea();
        }

        return null;
    }

    public void serverGiveHomelander() {
        Platform.runLater(() -> cheatGiveHomeLander(false));
    }

    public void serverGiveButcher() {
        Platform.runLater(() -> cheatGiveButcher(false));
    }

    public void serverRecoverCrystal() {
        getPlayer().getOpponent().setRemainingGems(2);
        Platform.runLater(() -> getPlayer().getBoard().getOpponentInformationBar().recoverGem());
    }

    public void serverWinRound() {
        Platform.runLater(() -> endRound(-1, 1, true));
    }

    public void serverLoseRound() {
        Platform.runLater(() -> endRound(1, -1, true));
    }
}
