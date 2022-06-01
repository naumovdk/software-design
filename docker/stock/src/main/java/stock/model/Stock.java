package stock.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {
    @Id
    public String name;
    public Double price;
    public Integer quantity;

    public Stock(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Stock() {

    }
}
