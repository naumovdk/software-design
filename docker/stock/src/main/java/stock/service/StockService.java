package stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import stock.model.Stock;
import stock.repo.StockRepo;

@Service
public class StockService {
    private final StockRepo repo;

    public StockService(StockRepo repo) {
        this.repo = repo;
    }

    public void save(Stock s) {
        this.repo.save(s);
    }

    public Stock getInfo(String name) {
        return this.repo.findById(name).orElseThrow();
    }

    public void buy(String name, int quantity) {
        var prev = repo.findById(name).orElseThrow();
        if (prev.quantity - quantity < 0) {
            throw new IllegalArgumentException();
        }
        prev.quantity -= quantity;
        this.repo.save(prev);
    }

    public List<Stock> getAll() {
        return (List<Stock>) this.repo.findAll();
    }
}
