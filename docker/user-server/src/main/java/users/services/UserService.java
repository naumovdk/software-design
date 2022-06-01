package users.services;

import java.util.List;
import java.util.stream.DoubleStream;

import org.springframework.stereotype.Service;
import users.clients.StockClient;
import users.exceptions.UserNotFound;
import users.exceptions.UserStockNotFound;
import users.model.LabelRepositoryKey;
import users.model.UserInfo;
import users.model.UserStock;
import users.model.User;
import users.repo.StockRepo;
import users.repo.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final StockRepo stockRepo;
    private final StockClient stock;

    public UserService(UserRepo userRepo, StockRepo stockRepo) {
        this.userRepo = userRepo;
        this.stockRepo = stockRepo;
        this.stock = new StockClient("localhost:8080/stock/");
    }

    public int register(User user) {
        return userRepo.save(user).id;
    }

    public void addMoney(int id, int amount) {
        var user = userRepo.findById(id).orElseThrow(UserNotFound::new);
        user.money += amount;
        userRepo.save(user);
    }

    public void buy(int userId, String stockName, int amount) {
        var user = userRepo.findById(userId).orElseThrow(UserNotFound::new);
        var s = stock.getStock(stockName);
        stock.buy(stockName, amount);
        stockRepo.save(new UserStock(userId, stockName, amount));
        user.money -= amount * s.price;
        userRepo.save(user);
    }

    public List<UserStock> getStocks(int id) {
        var user = userRepo.findById(id).orElseThrow(UserNotFound::new);
        var stocks = stockRepo.findAllByUserId(id);
        for (var s : stocks) {
            s.price = stock.getStock(s.name).price;
        }
        return stocks;
    }

    public UserInfo get(int id) {
        var user = getUser(id);
        var stocks = getStocks(id);
        double money = stocks.stream().flatMapToDouble(it -> DoubleStream.of(it.price * it.quantity)).sum();
        return new UserInfo(id, money + user.money);
    }

    private User getUser(int id) {
        return userRepo.findById(id).orElseThrow(UserNotFound::new);
    }

    public void sell(int id, String stockName, int amount) {
        var user = getUser(id);
        var s = stock.getStock(stockName);
        var userStock = stockRepo.findById(new LabelRepositoryKey(id, stockName)).orElseThrow(UserStockNotFound::new);
        if (userStock.quantity < amount) {
            throw new UserStockNotFound();
        }
        stock.sell(stockName, amount);
        if (userStock.quantity - amount == 0) {
            stockRepo.delete(userStock);
        } else {
            stockRepo.save(new UserStock(id, stockName, userStock.quantity - amount));
        }
        user.money += amount * s.price;
        userRepo.save(user);
    }
}
