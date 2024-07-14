package Enum;

import Model.InGameObjects.Cards.Card;

import java.util.ArrayList;

public enum CardStates {
    DES_PRE_GAME_MENU("DES_PRE_GAME_MENU"),
    DES_IN_GAME_SELECTABLE("DES_IN_GAME_SELECTABLE"),
    DES_IN_GAME_SHOW_OFF("DES_IN_GAME_SHOW_OFF"),
    DES_IN_GAME_RIGHT_SIDE_DISPLAY("DES_IN_GAME_RIGHT_SIDE_DISPLAY"), // the state where the row card is selected and about to play.
    ROW_IN_GAME_HAND("ROW_IN_GAME_HAND"),
    ROW_IN_GAME_PLAYED("ROW_IN_GAME_PLAYED"),
    ROW_IN_GAME_DISCARD_PILE("ROW_IN_GAME_DISCARD_PILE"),
    ROW_IN_GAME_DECK("ROW_IN_GAME_DECK"),
    ROW_HIDDEN("ROW_HIDDEN"); //for transformer and berserker after applying ability cards

    private final String cardStateString;

    CardStates(String cardStateString) {
        this.cardStateString = cardStateString;
    }

    public String getCardStateString() {
        return cardStateString;
    }

    public static CardStates getCardStateFromString(String cardStateString) {
        for (CardStates cardState : values()) {
            if (cardState.getCardStateString().equals(cardStateString))
                return cardState;
        }

        return null;
    }
}

