package Enum;

public enum FactionName {
    NorthernRealms("deck_back_realms", "deck_shield_realms"),
    NilfgaardianEmpire("deck_back_nilfgaard", "deck_shield_nilfgaard"),
    Monsters("deck_back_monsters", "deck_shield_monsters"),
    ScoiaTael("deck_back_scoiatael", "deck_shield_scoiatael"),
    Skellige("deck_back_skellige", "deck_shield_skellige"),
    SevenKingdoms("deck_back_sevenKingdoms", "deck_shield_sevenKingdoms"),
    Neutrals("maybe later", "maybe later"),
    Specials("maybe later", "maybe later");

    private final String factionBack;
    private final String factionShield;

    FactionName(String factionBack, String factionShield) {
        this.factionBack = factionBack;
        this.factionShield = factionShield;
    }

    public String getFactionBack() {
        return factionBack;
    }

    public String getFactionShield() {
        return factionShield;
    }

    public String giveFactionBackAddress() {
        return "/Pics/Icons/FactionIcons/" + getFactionBack() + ".jpg";
    }

    public String giveFactionShieldAddress() {
        return "/Pics/Icons/FactionIcons/" + getFactionShield() + ".png";
    }
}
