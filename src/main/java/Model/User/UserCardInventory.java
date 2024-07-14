package Model.User;
import Enum.FactionName;

import java.io.Serializable;

public class UserCardInventory implements Serializable {
    private User owner;
    private UserFaction northernRealms;
    private UserFaction nilfgaardEmpire;
    private UserFaction monsters;
    private UserFaction scoiaTael;
    private UserFaction skellige;
    private UserFaction sevenKingdoms;
    private UserFaction currentFaction;


    public UserCardInventory(User owner) {
        this.owner = owner;
        this.nilfgaardEmpire = new UserFaction(owner, FactionName.NilfgaardianEmpire);
        this.northernRealms = new UserFaction(owner, FactionName.NorthernRealms);
        this.monsters = new UserFaction(owner, FactionName.Monsters);
        this.scoiaTael = new UserFaction(owner, FactionName.ScoiaTael);
        this.skellige = new UserFaction(owner, FactionName.Skellige);
        this.sevenKingdoms = new UserFaction(owner, FactionName.SevenKingdoms);
        this.currentFaction = northernRealms;
    }

    public User getOwner() {
        return owner;
    }

    public UserFaction getNorthernRealms() {
        return northernRealms;
    }

    public UserFaction getNilfgaardEmpire() {
        return nilfgaardEmpire;
    }

    public UserFaction getMonsters() {
        return monsters;
    }

    public UserFaction getScoiaTael() {
        return scoiaTael;
    }

    public UserFaction getSkellige() {
        return skellige;
    }

    public UserFaction getSevenKingdoms() {
        return sevenKingdoms;
    }

    public UserFaction getCurrentFaction() {
        return currentFaction;
    }

    public void setNorthernRealms(UserFaction northernRealms) {
        this.northernRealms = northernRealms;
    }

    public void setNilfgaardEmpire(UserFaction nilfgaardEmpire) {
        this.nilfgaardEmpire = nilfgaardEmpire;
    }

    public void setMonsters(UserFaction monsters) {
        this.monsters = monsters;
    }

    public void setScoiaTael(UserFaction scoiaTael) {
        this.scoiaTael = scoiaTael;
    }

    public void setSkellige(UserFaction skellige) {
        this.skellige = skellige;
    }

    public void setSevenKingdoms(UserFaction sevenKingdoms) {
        this.sevenKingdoms = sevenKingdoms;
    }

    public void setCurrentFaction(UserFaction currentFaction) {
        this.currentFaction = currentFaction;
    }
}

