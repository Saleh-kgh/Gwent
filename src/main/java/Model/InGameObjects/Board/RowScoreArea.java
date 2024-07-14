package Model.InGameObjects.Board;

import javafx.scene.control.Label;
import Enum.*;
import javafx.scene.paint.Color;

public class RowScoreArea extends BoardSection{
    private static final double prefWidthOfRowScoreArea = Sizes.ROW_SCORE_AREA.getWidth();
    private static final double prefHeightOfRowScoreArea = Sizes.ROW_SCORE_AREA.getHeight();
    private Row row;
    private Label totalPower;

    public RowScoreArea(Row scoreRow) {
        super(prefWidthOfRowScoreArea, prefHeightOfRowScoreArea,
    scoreRow.getLayoutXOnBoard() - prefWidthOfRowScoreArea - Sizes.HORN_AREA.getWidth(),
                  scoreRow.getLayoutYOnBoard(), scoreRow.getPlayer(), scoreRow.getOwner());

        setCardPlace(CardPlace.ROW_SCORE_AREA);
        this.row = scoreRow;

        CardPlace cardPlace = scoreRow.getCardPlace();
//        if (cardPlace.equals(CardPlace.FRIENDLY_CLOSE) || cardPlace.equals(CardPlace.FRIENDLY_RANGED) ||
//                                                          cardPlace.equals(CardPlace.FRIENDLY_SIEGE))
//            setBackgroundImage("/Pics/BoardObjects/FriendlyRowScoreArea.png");
//        else
//            setBackgroundImage("/Pics/BoardObjects/OpponentRowScoreArea.png");

        this.totalPower = new Label("0");
        totalPower.setLayoutX(35);
        totalPower.setLayoutY(40);
        totalPower.setTextFill(Color.BLACK);
        totalPower.setStyle("-fx-font-size: 20px;");
        this.getChildren().add(totalPower);
    }

    public void updateTotalPowerLabel(int rowPower) {
        String rowPowerStr = String.format("%d", rowPower);
        totalPower.setText(rowPowerStr);
        totalPower.setLayoutX(this.getWidth() / 2 - totalPower.getWidth() / 2);
    }

    public Row getRow() {
        return row;
    }

    public int getRowPower() {
        return 0;
    }

    public Label getTotalPower() {
        return totalPower;
    }

    @Override
    public void updateSection() {
        int totalPower = getRow().calculateRowTotalPower();
        updateTotalPowerLabel(totalPower);
    }
}
