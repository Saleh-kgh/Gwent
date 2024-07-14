package Model.InGameObjects.Board;

import Model.Game.Player;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.RowCard;
import java.util.ArrayList;
import Enum.CardPlace;

public class OpponentHand extends BoardSection{
    public OpponentHand(double prefWidth, double prefHeight, double layoutXOnBoard, double layoutYOnBoard, Player player, Player owner) {
        super(prefWidth, prefHeight, layoutXOnBoard, layoutYOnBoard, player, owner);

        setCardPlace(CardPlace.OPPONENT_HAND);
    }

    @Override
    public double getCardLayoutXAfterTransition(Card addedCard) {
        return 100;
    }

    @Override
    public double getCardLayoutYAfterTransition(Card card) {
        return -100;
    }

    @Override
    public double translateRowCardAbsoluteLayoutX(Card card) {
        return 100;
    }

    @Override
    public double translateRowCardAbsoluteLayoutY(Card card) {
        return -100;
    }
}
