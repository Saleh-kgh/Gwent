package Model.InGameObjects.Board;

import Model.Game.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import Enum.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class TotalScoreArea extends BoardSection{
    private static final double prefWidthOfTotalScoreArea = Sizes.TOTAL_SCORE_AREA.getWidth();
    private static final double prefHeightOfTotalScoreArea = Sizes.TOTAL_SCORE_AREA.getHeight();
    private static final double layoutXOnBoardOfTotalScoreArea = 425;
    private static final double layoutYOnBoardOfTotalScoreArea = 304;
    private static final double layoutYDiffer = 402;
    private Label totalPower;
    private boolean isOpponent;
    private Rectangle higherScoreIcon;

    public TotalScoreArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfTotalScoreArea, prefHeightOfTotalScoreArea, layoutXOnBoardOfTotalScoreArea,
                    layoutYOnBoardOfTotalScoreArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfTotalScoreArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_TOTAL_SCORE_AREA);
            //setBackgroundImage("/Pics/BoardObjects/FriendlyTotalScoreArea.png");
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_TOTAL_SCORE_AREA);
            //setBackgroundImage("/Pics/BoardObjects/OpponentTotalScoreArea.png");
        }

        this.isOpponent = !isFriendly;

        setUpTotalPowerLabel();
        setUpHigherScoreIcon();
    }

    public int getTotalScore() {
        return calculateTotalPower();
    }

    public void updateTotalPowerLabel(int rowPower) {
        String rowPowerStr = String.format("%d", rowPower);
        totalPower.setText(rowPowerStr);
        totalPower.setLayoutX(this.getWidth() / 2 - totalPower.getWidth() / 2);
    }

    private int calculateTotalPower() {
        int totalPower = 0;
        if (isOpponent) {
            totalPower += getPlayer().getBoard().getOpponentCloseArea().calculateRowTotalPower();
            totalPower += getPlayer().getBoard().getOpponentRangedArea().calculateRowTotalPower();
            totalPower += getPlayer().getBoard().getOpponentSiegeArea().calculateRowTotalPower();
        }
        else {
            totalPower += getPlayer().getBoard().getFriendlyCloseArea().calculateRowTotalPower();
            totalPower += getPlayer().getBoard().getFriendlyRangedArea().calculateRowTotalPower();
            totalPower += getPlayer().getBoard().getFriendlySiegeArea().calculateRowTotalPower();
        }
        return totalPower;
    }

    private void setUpTotalPowerLabel() {
        this.totalPower = new Label("0");
        totalPower.setLayoutX(30);
        totalPower.setLayoutY(13);
        totalPower.setTextFill(Color.BLACK);
        totalPower.setStyle("-fx-font-size: 20px;");
        this.getChildren().add(totalPower);
    }

    private void setUpHigherScoreIcon() {
        higherScoreIcon = new Rectangle(102, 78);
        higherScoreIcon.setFill(new ImagePattern(new Image(getClass().getResource(
                "/Pics/Icons/NotificationIcons/icon_high_score.png").toExternalForm())));

        higherScoreIcon.setLayoutX(402);
        higherScoreIcon.setLayoutY(705);
        if (isOpponent)
            higherScoreIcon.setLayoutY(300);
    }

    public void addHigherScoreIcon() {
        if (getPlayer().getBoard() != null && !getPlayer().getBoard().getChildren().contains(higherScoreIcon))
            getPlayer().getBoard().getChildren().add(higherScoreIcon);
    }

    public void removeHigherScoreIcon() {
        if (getPlayer().getBoard() != null)
            getPlayer().getBoard().getChildren().remove(higherScoreIcon);
    }

    @Override
    public void updateSection() {
        updateTotalPowerLabel(calculateTotalPower());
    }
}
