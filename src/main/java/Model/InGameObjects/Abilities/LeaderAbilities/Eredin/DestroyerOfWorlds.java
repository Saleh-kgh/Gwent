package Model.InGameObjects.Abilities.LeaderAbilities.Eredin;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Enum.CardStates;
import Enum.PlayerState;
import Enum.CardNames;
import Model.InGameObjects.Cards.LeaderCard;
import controller.Client;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import view.Animations.FadingAnimation;

import java.util.ArrayList;
import java.util.Random;

public class DestroyerOfWorlds extends CardAbility {

    @Override
    public void applyAbility() {
        if (getCard().getPlayer().getDeck().isEmpty() || getCard().getPlayer().getHand().size() < 2) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Random random0 = new Random();

        for (int i = 0; i < 2; i++) {
            Card card = getCard().getPlayer().getHand().get(random0.nextInt(getCard().getPlayer().getHand().size()));

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
            pauseTransition.setOnFinished(event -> {
                card.getPlayer().getGameController().
                        putOpponentCardInDiscardPile(card, card.getCurrentPlaceInGame(), false);
            });
            pauseTransition.play();

            new FadingAnimation(card.getRowCard(), "anim_scorch").play();
            playSfx("scorch");

            Client.client.sendSfxAnimationPackage(card, "scorch", "anim_scorch");
        }

        if (!isLeaderEmhyrInvaderOfNorth()) {
            ArrayList<Card> deckCards = new ArrayList<>();

            for (Card card : getCard().getPlayer().getBoard().getFriendlyDeckArea().getCardsOnSection()) {
                card.setCurrentState(CardStates.DES_IN_GAME_SELECTABLE);
                deckCards.add(card);
                card.createGraphicalFields();
            }

            getCard().getPlayer().setPlayerState(PlayerState.LEADER_CHOOSING_CARD);

            getCard().getPlayer().getGameController().showChooseScrollBox(deckCards);
        }
        else {
            ArrayList<Card> deckCards = new ArrayList<>();
            deckCards.addAll(getCard().getPlayer().getBoard().getFriendlyDeckArea().getCardsOnSection());

            Random random = new Random();
            Card card = deckCards.get(random.nextInt(deckCards.size()));
            card.createGraphicalFields();
            drawCardFromDeck(card);
            getCard().getPlayer().getGameController().finishLeaderAbility();
        }
    }

    private boolean isLeaderEmhyrInvaderOfNorth() {
        return getCard().getPlayer().getOpponent().getLeaderCard().getCardName().
                equals(CardNames.EMHYR_INVADER_OF_THE_NORTH);
    }

    public void drawCardFromDeck(Card card) {
        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getHandArea(), CardStates.ROW_IN_GAME_HAND);

        Client.client.sendRemoveCardPackage(card, card.getCurrentPlaceInGame(),
                CardStates.ROW_IN_GAME_HAND, false);

        Client.client.sendAddCardPackage(card, getCard().getPlayer().getBoard().getHandArea(),
                    CardStates.ROW_IN_GAME_HAND, false);

        playSfx("redraw");

        Client.client.sendSfxAnimationPackage(card, "redraw", "non");

        getCard().getPlayer().getBoard().getHandArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new DestroyerOfWorlds();
    }

    @Override
    public void writeDescription() {
        setDescription("Destroyer Of Worlds:\nDiscard 2 card and draw 1 card of your choice from your deck.");
    }
}
