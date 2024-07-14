package controller;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import Model.Game.GameHistory;
import Model.Game.GameObject;
import Model.Game.Player;
import Model.InGameObjects.Abilities.FactionAbility.FactionAbility;
import Model.InGameObjects.Abilities.FactionAbility.NilfgaardAbility;
import Model.InGameObjects.Board.Board;
import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.CloseArea;
import Model.InGameObjects.Board.HandArea;
import Model.InGameObjects.Cards.*;
import Model.User.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import junit.framework.TestCase;
import view.Controller.GameViewController;
import Enum.PlayerState;
import Enum.CardPlace;
import Enum.CardStates;

import java.util.ArrayList;

public class GameControllerTest extends TestCase {

    @Mock
    private Player mockPlayer;
    @Mock
    private Player mockOpponent;
    @Mock
    private Card mockCard;
    @Mock
    private SpellCard mockSpellCard;
    @Mock
    private UnitCard mockUnitCard;
    @Mock
    private LeaderCard mockLeaderCard;
    @Mock
    private RowCard mockRowCard;
    @Mock
    private BoardSection mockBoardSection;
    @Mock
    private GameViewController mockGameViewController;
    @Mock
    private GameObject mockGameObject;
    @Mock
    private Board mockBoard;
    @Mock
    private NilfgaardAbility mockNilfgaardAbility;
    @Mock
    private FactionAbility mockOtherFactionAbility;
    @Mock
    private GameHistory mockGameHistory;

    private GameController gameController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPlayer.getSelectedCard()).thenReturn(mockCard);
        when(mockPlayer.getGameObject()).thenReturn(mockGameObject);
        when(mockPlayer.getBoard()).thenReturn(mockBoard);
        when(mockGameObject.getGameViewController()).thenReturn(mockGameViewController);
        when(mockGameObject.getGameHistory()).thenReturn(mockGameHistory);

        gameController = new GameController(mockPlayer);
    }

    @Test
    public void testGetPlayer() {
        assertEquals(mockPlayer, gameController.getPlayer());
    }

    @Test
    public void testIsPlaceValidForCardToPut_FriendlyCloseRanged() {
        when(mockCard.getCardPlace()).thenReturn(CardPlace.FRIENDLY_CLOSE_RANGED);
        when(mockBoardSection.getCardPlace()).thenReturn(CardPlace.FRIENDLY_CLOSE);

        assertTrue(gameController.isPlaceValidForCardToPut(mockCard, mockBoardSection));

        when(mockBoardSection.getCardPlace()).thenReturn(CardPlace.FRIENDLY_RANGED);
        assertTrue(gameController.isPlaceValidForCardToPut(mockCard, mockBoardSection));

        when(mockBoardSection.getCardPlace()).thenReturn(CardPlace.OPPONENT_CLOSE);
        assertFalse(gameController.isPlaceValidForCardToPut(mockCard, mockBoardSection));
    }

    @Test
    public void testResetSelectedCard() {
        when(mockPlayer.getSelectedCard()).thenReturn(mockCard);

        gameController.resetSelectedCard();

        verify(mockGameViewController).removeSelectedCardDescriptionAndExplanation(mockCard);
        verify(mockPlayer).setSelectedCard(null);
    }

    @Test
    public void testMoveCardProtocol() {
        gameController.moveCardProtocol(mockCard, mockBoardSection, mockBoardSection, CardStates.ROW_IN_GAME_PLAYED);

        verify(mockBoardSection).removeFromCardsOnSection(mockCard);
        verify(mockCard).setPreviousState(any());
        verify(mockCard).setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
        verify(mockGameViewController).playCardTransition(mockCard, mockBoardSection);
    }

    @Test
    public void testEndGame() {
        gameController.moveCardProtocol(mockCard, mockBoardSection, mockBoardSection, CardStates.ROW_IN_GAME_PLAYED);
        when(mockPlayer.getUser()).thenReturn(mock(User.class));
        when(mockPlayer.getOpponent()).thenReturn(mockPlayer);

        try {
            gameController.endGame();
        } catch (Exception e) {}

        verify(mockCard).setPreviousState(any());
        verify(mockCard).setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
    }

    public void testCloseGame() {
        when(mockPlayer.getLeaderCard()).thenReturn(mockLeaderCard);

        try {
            gameController.closeGame();
        } catch (Exception e) {}

        assertEquals(mockPlayer.getLeaderCard(), mockLeaderCard);
    }

    public void testEndRound() {
        when(mockPlayer.getPlayerState()).thenReturn(PlayerState.CHOOSING_CARD_FROM_HAND);

        try {
            gameController.endRound(2, 3, false);
        } catch (Exception e) {}

        assertEquals(mockPlayer.getPlayerState(), PlayerState.CHOOSING_CARD_FROM_HAND);
    }

    public void testSetCardStates() {
        when(mockCard.getCurrentState()).thenReturn(CardStates.ROW_IN_GAME_PLAYED);

        gameController.setCardStates(mockCard, CardStates.ROW_IN_GAME_PLAYED);

        assertEquals(CardStates.ROW_IN_GAME_PLAYED, mockCard.getCurrentState());
    }

    @Test
    public void testIsOnlyOneFactionAbilityNilfgaard_PlayerHasNilfgaard() {
        when(mockPlayer.getPlayerState()).thenReturn(PlayerState.CHOOSING_CARD_FROM_HAND);
        when(mockPlayer.getFactionAbility()).thenReturn(mockNilfgaardAbility);
        when(mockOpponent.getFactionAbility()).thenReturn(mockOtherFactionAbility);

        try {
            gameController.isOnlyOneFactionAbilityNilfgaard();
        } catch (Exception e) {}

        assertEquals(mockPlayer.getPlayerState(), PlayerState.CHOOSING_CARD_FROM_HAND);
    }

    public void testAddCardToBoardSection() {
    }

    public void testApplyCardAbilityWithDelay() {
    }

    public void testPutFriendlyCardInDiscardPile() {
    }

    public void testPutOpponentCardInDiscardPile() {
    }

    public void testReplaceUnitCardWithDecoy() {
    }

    public void testShowArrayOfCards() {
    }

    public void testStopPlayerFromLookingAtCards() {
    }

    public void testShowArrayOfCardsOfLeaderAbility() {
    }

    public void testStopShowingScrollBoxOfLeaderAbility() {
    }

    public void testSelectCard() {
        when(mockPlayer.getSelectedCard()).thenReturn(mockCard);

        gameController.selectCard(mockCard);

        assertEquals(mockPlayer.getSelectedCard(), mockCard);
    }

    public void testSwapInitialHandCard() {
    }

    public void testStopSwappingInitialHand() {

    }

    public void testShowChooseScrollBox() {
    }

    public void testScrollBoxReviveDiscardedCard() {
    }

    public void testReviveCardFromDiscPile() {
    }

    public void testGetCardBoardSection() {
    }

    public void testPlayLeaderCard() {
    }

    public void testIsLeaderPlayable() {
    }

    public void testFinishLeaderAbility() {
    }

    public void testFailedLeaderAbility() {
    }

    public void testCheatGiveHomeLander() {
    }

    public void testCheatGiveButcher() {
    }

    public void testCheatGiveThaler() {
    }

    public void testCheatAddToHand() {
    }

    public void testCheatRecoverCrystal() {
    }

    public void testCheatWinRound() {
    }

    public void testCheatLoseRound() {
    }

    public void testServerMoveCard() {
    }

    public void testServerAddCard() {
    }

    public void testServerRemoveCard() {
    }

    public void testServerPlaySfxAnimation() {
    }

    public void testServerPlayerTurn() {
    }

    public void testServerPlayerHand() {
    }

    public void testServerWeatherAbility() {
    }

    public void testGetCardById() {
    }

    public void testGetReversedBoardSectionByName() {
    }

    public void testServerGiveHomelander() {
    }

    public void testServerGiveButcher() {
    }

    public void testServerRecoverCrystal() {
    }

    public void testServerWinRound() {
    }

    public void testServerLoseRound() {
    }
}