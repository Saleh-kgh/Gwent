package Enum;

public enum UniversalNumbers {
    CARDS_COUNT(176),
    TRANSITION_DURATION(1500),
    NOTIFICATION_DURATION(2000);

    private double value;

    UniversalNumbers(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
