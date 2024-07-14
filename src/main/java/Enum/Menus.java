package Enum;

public enum Menus {
    Login("Login"),
    Register("Register"),
    Main("Main"),
    Profile("Profile"),

    Game("Game"),
    LeaderBoard("LeaderBoard"),
    PreGame("PreGame");


    private String value;

    Menus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Menus fromValue(String value) {
        for (Menus menu : Menus.values()) {
            if (menu.getValue().equals(value)) {
                return menu;
            }
        }
        return null;
    }
}
