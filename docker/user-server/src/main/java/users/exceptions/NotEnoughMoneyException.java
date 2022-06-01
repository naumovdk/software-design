package users.exceptions;


import org.springframework.http.HttpStatus;

public class NotEnoughMoneyException extends BaseException {

    public NotEnoughMoneyException() {
        super(new BaseError(HttpStatus.BAD_REQUEST, "not enough money"));
    }
}
