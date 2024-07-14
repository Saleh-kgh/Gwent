package Enum;

public enum Emojis {


    MY_EMOJI_X(1500),
    MY_EMOJI_Y(800),
    EMOJI_WIDTH(80),
    EMOJI_HEIGHT(100),
    OPPONENT_EMOJI_X(1500),
    OPPONENT_EMOJI_Y(200),
    EMOJI_TIME(7),
    ADDRESS1("/Pics/Emojis/Emoji1.jpg"),
    ADDRESS2("/Pics/Emojis/Emoji2.jpg"),
    ADDRESS3("/Pics/Emojis/Emoji3.jpg"),
    ADDRESS4("/Pics/Emojis/Emoji4.jpg"),
    LABEL_MESSAGE_WIDTH(200),
    LABEL_MESSAGE_HEIGHT(40),
    LABEL_MESSAGE_FONT_SIZE(25),
    LABEL_MESSAGE1("Good game"),
    LABEL_MESSAGE2("It was smart"),
    LABEL_MESSAGE3("Damn you"),
    ;
    private final double value;
    private final String stringValue;
    Emojis(double value) {
        this.value = value;
        stringValue = null;
    }
    Emojis(String value) {
        this.value = 0;
        stringValue = value;
    }
    public double getValue() {
        return value;
    }
    public String getStringValue() {
        return stringValue;
    }
}
