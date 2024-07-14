package Enum;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public enum Sizes {
    SCENE(1920, 1080, 16, 9),
    ROW_CARD_ABILITY_ICON(30 , 30, 1, 1),
    ROW_CARD_POWER_ICON(60 , 60, 1, 1),
    ROW_CARD_ROW_ICON(30 , 30, 1, 1),
    ROW_CARD(72.4,105, 20, 29),
    FIELD_CARD(72.4,105, 20, 29),
    DESCRIPTIVE_CARD(220, 387.5, 18, 31),
    PRE_GAME_CARD(72.4,105, 20, 29), // donno exactly, maybe has to change
    SCROLL_SHOW_BOX(1920, 600, 28, 15),
    CLOSE_AREA(816, 119, 48, 7),
    RANGED_AREA(816, 119, 48, 7),
    SIEGE_AREA(816, 119, 48, 7),
    HORN_AREA(137, 126, 1.1, 1),
    ROW_SCORE_AREA(65, 120, 13, 24),
    DISCARD_PILE_AREA(115, 151, 23, 25),
    HAND_AREA(951, 141, 20.2, 3),
    DECK_AREA(113, 147, 7, 9.2),
    LEADER_CARD_AREA(121, 154, 11,14),
    TOTAL_SCORE_AREA(55, 57, 1, 1.05),
    WEATHER_CARD_AREA(290, 135, 16.4, 9.1);

    private double width;
    private double height;
    private double widthRatio;
    private double heightRatio;

    Sizes(double width, double height, double widthRatio, double heightRatio) {
        this.width = width;
        this.height = height;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public double getWidth() {
        return width * getUserScreenScale();
    }

    public double getHeight() {
        return height * getUserScreenScale();
    }

    public double getWidthRatio() {
        return widthRatio * getUserScreenScale();
    }

    public double getHeightRatio() {
        return heightRatio * getUserScreenScale();
    }

    public static double getUserScreenScale() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double dpi = screen.getDpi();
        return (dpi / 141);
    }
}
