package Enum;

import java.util.regex.Pattern;

public enum RequestDataExtraction {
    LOGIN("(?<username>.*)|(?<password>.*)"),
    REGISTER("(?<username>.*)|(?<password>.*)|(?<nickname>.*)|(?<email>.*)");

    private final Pattern pattern;

    RequestDataExtraction(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }
    }
