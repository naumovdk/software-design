package users.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFound extends BaseException {

    public UserNotFound() {
        super(new BaseError(HttpStatus.NOT_FOUND, "User not found"));
    }
}
