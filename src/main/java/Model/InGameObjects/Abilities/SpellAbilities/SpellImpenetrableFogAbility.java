package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import controller.Client;
import view.Animations.FadingAnimation;
import Enum.CardNames;

public class SpellImpenetrableFogAbility extends CardAbility {


    @Override
    public void applyAbility() {
        playSfx("fog");

        int counter = 0;

        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardName().equals(CardNames.STORM)) {
                getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                return;
            }
            else if (card.getCardName().equals(CardNames.FOG)) {
                if (counter++ > 0) {
                    getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                    return;
                }
            }
        }

        new FadingAnimation(getCard().getPlayer().getBoard().getFriendlyRangedArea(), "overlay_fog").play();
        new FadingAnimation(getCard().getPlayer().getBoard().getOpponentRangedArea(), "overlay_fog").play();

        Client.client.sendWeatherEffectPackage("overlay_fog");
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellImpenetrableFogAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Mardroeme:\nTriggers transformation of all Berserker cards on the same row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_weather_fog.png";
    }
}
