package Model.Package;

import java.io.Serializable;

public class WeatherAbilityPackage implements Serializable {
    private String animationName;

    public WeatherAbilityPackage(String animationName) {
        this.animationName = animationName;
    }

    public String getAnimationName() {
        return animationName;
    }
}
