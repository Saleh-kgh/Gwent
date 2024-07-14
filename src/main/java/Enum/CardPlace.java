package Enum;

public enum CardPlace {
    HAND_AREA("HAND_AREA"),
    OPPONENT_HAND("OPPONENT_HAND"),
    FRIENDLY_CLOSE_RANGED("FRIENDLY_CLOSE_RANGED"),
    OPPONENT_CLOSE_RANGED("OPPONENT_CLOSE_RANGED"),
    FRIENDLY_CLOSE("FRIENDLY_CLOSE"),
    FRIENDLY_RANGED("FRIENDLY_RANGED"),
    FRIENDLY_SIEGE("FRIENDLY_SIEGE"),
    FRIENDLY_LEADER("FRIENDLY_LEADER"),
    OPPONENT_CLOSE("OPPONENT_CLOSE"),
    OPPONENT_RANGED("OPPONENT_RANGED"),
    OPPONENT_SIEGE("OPPONENT_SIEGE"),
    OPPONENT_LEADER("OPPONENT_LEADER"),
    HORN_AREA("HORN_AREA"),
    WEATHER_CARDS_AREA("WEATHER_CARDS_AREA"),
    DECOY_PLACE("DECOY_PLACE"),
    ROW_SCORE_AREA("ROW_SCORE_AREA"),
    FRIENDLY_TOTAL_SCORE_AREA("FRIENDLY_TOTAL_SCORE_AREA"),
    OPPONENT_TOTAL_SCORE_AREA("OPPONENT_TOTAL_SCORE_AREA"),
    FRIENDLY_DISCARD_PILE_AREA("FRIENDLY_DISCARD_PILE_AREA"),
    OPPONENT_DISCARD_PILE_AREA("OPPONENT_DISCARD_PILE_AREA"),
    FRIENDLY_DECK_AREA("FRIENDLY_DECK_AREA"),
    OPPONENT_DECK_AREA("OPPONENT_DECK_AREA");

    private final String cardPlaceString;

    CardPlace(String cardPlaceString) {
        this.cardPlaceString = cardPlaceString;
    }

    public String getCardPlaceString() {
        return cardPlaceString;
    }

    public static CardPlace getCardPlaceFromString(String cardStateString) {
        for (CardPlace cardPlace : values()) {
            if (cardPlace.getCardPlaceString().equals(cardStateString))
                return cardPlace;
        }

        return null;
    }
}
