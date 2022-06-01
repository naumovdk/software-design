package users.exceptions;

import org.springframework.http.HttpStatus;

public class StockBadRequest extends BaseException{

    public StockBadRequest(String message) {
        super(new BaseError(HttpStatus.BAD_REQUEST,message));
    }
}
