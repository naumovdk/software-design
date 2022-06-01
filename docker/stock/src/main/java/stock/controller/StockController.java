package stock.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.model.Stock;
import stock.service.StockService;

@RestController()
@RequestMapping("/stock")
public class StockController {
    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public void create(@RequestParam String name, @RequestParam double price, @RequestParam int quantity) {
        if (price < 0.0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        service.save(new Stock(name, price, quantity));
    }

    @GetMapping("/all")
    public List<Stock> getAll() {
        return service.getAll();
    }

    @GetMapping("/buy")
    public void buy(@RequestParam String name, @RequestParam int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        service.buy(name, quantity);
    }

    @GetMapping("/sell")
    public void sell(@RequestParam String name, @RequestParam int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        service.buy(name, -quantity);
    }

    @GetMapping("/")
    public Stock get(@RequestParam String name) {
        return service.getInfo(name);
    }
}
