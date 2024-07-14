package Model.InGameObjects.Abilities.FactionAbility;

import Enum.FactionName;
import Model.Game.Player;

public class NilfgaardAbility extends FactionAbility {

    public NilfgaardAbility(Player player, Player owner) {
        super(player, owner);
        setCancelled(false);
    }

    @Override
    public boolean canAbilityBeTriggered() {
        return !(getOwner().getOpponent().getPlayersFaction().equals(FactionName.NilfgaardianEmpire)) &&
                    getPlayer().getBoard().getFriendlyTotalScoreArea().getTotalScore() == getPlayer().getBoard().
                    getOpponentTotalScoreArea().getTotalScore() && !isCancelled();
    }

    @Override
    public void applyAbility(int duration) {
        if (isCancelled())
            return;

        getPlayer().getGameObject().getGameViewController().displayNotifBar("Nilfgaard Ability Triggered! Draws Are Won.");
        getPlayer().getGameObject().getGameViewController().displayNotifIcon("/Pics/Icons/NotificationIcons/notif_win_round.png");

        getOwner().getOpponent().setRemainingGems(getOwner().getOpponent().getRemainingGems() - 1);

        getPlayer().getBoard().getOpponentInformationBar().breakGem();
        getPlayer().getBoard().getPlayerInformationBar().breakGem();
    }
}
