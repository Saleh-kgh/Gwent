package Model.Package;

import java.io.Serializable;

public class CheatPackage implements Serializable {

    private int cheatCode;

    public CheatPackage(int cheatCode) {
        this.cheatCode = cheatCode;
    }

    public int getCheatCode() {
        return cheatCode;
    }
}
