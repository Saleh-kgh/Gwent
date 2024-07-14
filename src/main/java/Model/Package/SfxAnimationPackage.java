package Model.Package;

import Model.InGameObjects.Cards.Card;

import java.io.Serializable;

public class SfxAnimationPackage implements Serializable {

    private int cardId;
    private String sfxName;
    private String animationName;

    public SfxAnimationPackage(Card card, String sfxName, String animationName) {
        this.cardId = card.getId();
        this.sfxName = sfxName;
        this.animationName = animationName;
    }

    public int getCardId() {
        return cardId;
    }

    public String getSfxName() {
        return sfxName;
    }

    public String getAnimationName() {
        return animationName;
    }
}
