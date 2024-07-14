package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import controller.Client;
import view.Animations.FadingAnimation;
import Enum.CardNames;

public class SpellTorrentialRainAbility extends CardAbility {

    @Override
    public void applyAbility() {
        playSfx("rain");

        int counter = 0;

        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if ( card.getCardName().equals(CardNames.STORM)) {
                getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                return;
            }
            else if (card.getCardName().equals(CardNames.RAIN)) {
                if (counter++ > 0) {
                    getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                    return;
                }
            }
        }

        new FadingAnimation(getCard().getPlayer().getBoard().getFriendlySiegeArea(), "overlay_rain").play();
        new FadingAnimation(getCard().getPlayer().getBoard().getOpponentSiegeArea(), "overlay_rain").play();

        Client.client.sendWeatherEffectPackage("overlay_rain");
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellTorrentialRainAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Torrential Rain:\nSets the strength of all Siege Combat cards to 1 for both players.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_weather_rain.png";
    }
}
