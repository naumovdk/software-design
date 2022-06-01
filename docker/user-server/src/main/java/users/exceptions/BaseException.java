package users.exceptions;


public class BaseException extends RuntimeException {
    private final BaseError error;

    public BaseException(BaseError error) {
        this.error = error;
    }

    public BaseError getError() {
        return error;
    }
}
