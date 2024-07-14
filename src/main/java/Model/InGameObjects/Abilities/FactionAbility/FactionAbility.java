package Model.InGameObjects.Abilities.FactionAbility;

import Model.Game.Player;

public abstract class FactionAbility {

    private final Player player;
    private final Player owner;
    private boolean isCancelled;

    public FactionAbility(Player player, Player owner) {
        this.player = player;
        this.owner = owner;
        isCancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    abstract public boolean canAbilityBeTriggered();

    abstract public void applyAbility(int duration);
}
