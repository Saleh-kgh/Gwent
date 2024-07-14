package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import controller.Client;
import view.Animations.FadingAnimation;
import Enum.CardNames;

public class SpellBitingFrostAbility extends CardAbility {


    @Override
    public void applyAbility() {
        playSfx("cold");

        int counter = 0;

        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardName().equals(CardNames.FROST)) {
                if (counter++ > 0) {
                    getCard().getPlayer().getGameController().putFriendlyCardInDiscardPile(getCard(), getCard().getCurrentPlaceInGame(), false);
                    return;
                }
            }
        }

        new FadingAnimation(getCard().getPlayer().getBoard().getFriendlyCloseArea(), "overlay_frost").play();
        new FadingAnimation(getCard().getPlayer().getBoard().getOpponentCloseArea(), "overlay_frost").play();

        Client.client.sendWeatherEffectPackage("overlay_frost");
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellBitingFrostAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Biting Frost:\nSets the strength of all Close Combat cards to 1 for both players.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_weather_frost.png";
    }
}
