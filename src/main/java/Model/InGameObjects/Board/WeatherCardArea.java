package Model.InGameObjects.Board;

import Model.Game.Player;
import Enum.*;

public class WeatherCardArea extends BoardSection{
    private static final double prefWidthOfWeatherCardArea = Sizes.WEATHER_CARD_AREA.getWidth();
    private static final double prefHeightOfWeatherCardArea = Sizes.WEATHER_CARD_AREA.getHeight();
    private static final double layoutXOnBoardOfWeatherCardArea = 140;
    private static final double layoutYOnBoardOfWeatherCardArea = 450;

    public WeatherCardArea(Player player, Player owner) {
        super(prefWidthOfWeatherCardArea, prefHeightOfWeatherCardArea, layoutXOnBoardOfWeatherCardArea,
                    layoutYOnBoardOfWeatherCardArea, player, owner);

        setCardPlace(CardPlace.WEATHER_CARDS_AREA);
        //setBackgroundImage("/Pics/BoardObjects/WeatherCardArea.png");
    }
}
