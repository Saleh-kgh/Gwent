package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.Row;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.Game.Player;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;

public class SpellClearWeatherAbility extends CardAbility {


    @Override
    public void applyAbility() {
        ArrayList<Card> discardedCards = new ArrayList<>();

        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            discardedCards.add(card);
        }

        for (Card card : discardedCards) {
            card.getPlayer().getGameController().
                    putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), false);
        }

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
        pauseTransition.setOnFinished(event -> {
            getCard().getPlayer().getGameController().
                    putOpponentCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
        });
        pauseTransition.play();

        playSfx("clear");

        removeWeatherEffectFromPlayer(getCard().getPlayer());

        Client.client.sendWeatherEffectPackage("clearWeather");
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

    @Override
    public CardAbility deepCopy() {
        return new SpellClearWeatherAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Clear Weather:\nRemoves all Weather Card (Biting Frost, Impenetrable Fog and Torrential Rain) effects.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_weather_clearpng.png";
    }
}
