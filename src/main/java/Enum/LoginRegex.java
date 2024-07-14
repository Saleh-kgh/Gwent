package Enum;

import java.util.regex.Pattern;

public enum LoginRegex {
    VALID_USERNAME("[a-zA-Z0-9-]+"),
    VALID_PASSWORD("[!@#$%^&*()_=+0-9a-zA-Z-]+"),
    STRONG_PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_=+-]).{8,}$"),
    VALID_EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$");

    private final Pattern pattern;

    LoginRegex(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }
}
