package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import controller.Client;
import view.Animations.FadingAnimation;
import Enum.CardNames;

public class SpellSkelligeStormAbility extends CardAbility {

    @Override
    public void applyAbility() {
        playSfx("rain");

        boolean rainy = false;

        boolean foggy = false;

        int counter = 0;

        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardName().equals(CardNames.STORM)) {
                if (counter++ > 0) {
                    getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                    return;
                }
            }
            else if (card.getCardName().equals(CardNames.RAIN))
                rainy = true;
            else if (card.getCardName().equals(CardNames.FOG))
                foggy = true;
        }


        if (!rainy) {
            new FadingAnimation(getCard().getPlayer().getBoard().getFriendlySiegeArea(), "overlay_rain").play();
            new FadingAnimation(getCard().getPlayer().getBoard().getOpponentSiegeArea(), "overlay_rain").play();
            Client.client.sendWeatherEffectPackage("overlay_rain");
        }

        if (!foggy) {
            new FadingAnimation(getCard().getPlayer().getBoard().getFriendlyRangedArea(), "overlay_fog").play();
            new FadingAnimation(getCard().getPlayer().getBoard().getOpponentRangedArea(), "overlay_fog").play();
            Client.client.sendWeatherEffectPackage("overlay_fog");
        }
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellSkelligeStormAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Skellige Storm:\nReduces the Strength of all Range and Siege Units to 1.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_weather_storm.png";
    }
}
