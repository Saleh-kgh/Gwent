package Model;

import java.io.Serializable;

public class Result implements Serializable {

    private String title;
    private final String message;
    private boolean isSuccessful;

    public Result(String title, String message, boolean isSuccessful) {
        this.title = title;
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public Result(String message, boolean isSuccessful) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String toString() {
        return message;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
