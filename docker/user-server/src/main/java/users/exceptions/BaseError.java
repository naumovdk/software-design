package users.exceptions;

import org.springframework.http.HttpStatus;

public class BaseError {
    private final HttpStatus status;
    private final String message;

    public BaseError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
