package users.exceptions;

import org.springframework.http.HttpStatus;

public class StockNotFound extends BaseException {
    public StockNotFound() {
        super(new BaseError(HttpStatus.NOT_FOUND, "stock not found"));
    }
}
