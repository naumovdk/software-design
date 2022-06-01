package users.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import users.exceptions.BaseError;
import users.exceptions.BaseException;
import users.exceptions.UserNotFound;
import users.model.UserInfo;
import users.model.UserStock;
import users.model.User;
import users.services.UserService;

@RestController
@RequestMapping("/user")
@ControllerAdvice
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    @ResponseBody
    public int register(@RequestParam int id) {
        return userService.register(new User(id, 0));
    }

    @GetMapping("/buy")
    public void buy(@RequestParam int id, @RequestParam String stock, @RequestParam int amount) throws UserNotFound {
        this.userService.buy(id, stock, amount);
    }

    @GetMapping("/sell")
    public void sell(@RequestParam int id, @RequestParam String stock, @RequestParam int amount) throws UserNotFound {
        this.userService.sell(id, stock, amount);
    }

    @GetMapping("/add-money")
    public void addMoney(@RequestParam int id, @RequestParam int amount) throws UserNotFound {
        this.userService.addMoney(id, amount);
    }

    @GetMapping("/stocks")
    public List<UserStock> getStocks(@RequestParam int id) {
        return this.userService.getStocks(id);
    }

    @GetMapping("/")
    public UserInfo get(@RequestParam int id) {
        return userService.get(id);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseError> handleException(BaseException e) {
        return new ResponseEntity<>(e.getError(), HttpStatus.BAD_REQUEST);
    }
}
