package Model.Package;

import java.io.Serializable;

public class ServerPackage implements Serializable {

    public String command;
    public Object object;

    public ServerPackage(String command, Object object) {
        this.command = command;
        this.object = object;
    }
}
