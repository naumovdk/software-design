package users.exceptions;

import org.springframework.http.HttpStatus;

public class UserStockNotFound extends BaseException {
    public UserStockNotFound() {
        super(new BaseError(HttpStatus.NOT_FOUND, "user do not have this stock"));
    }
}
