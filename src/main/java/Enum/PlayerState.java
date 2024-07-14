package Enum;

public enum PlayerState {
    CHOOSING_CARD_FROM_HAND,
    CHOOSING_ROW_TO_PUT,
    PARALYZED, // when player has played a cart and can't play until next turn
    PASSED,
    WAITING_FOR_OPPONENT,
    LOOKING_AT_CARDS,
    CHOOSING_SCROLL_BOX_CARD,
    SWAPPING_INITIAL_HAND,
    LEADER_CHOOSING_CARD,
    LEADER_LOOKING_CARDS;
}
