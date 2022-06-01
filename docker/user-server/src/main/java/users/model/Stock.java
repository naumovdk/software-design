package users.model;

import javax.persistence.Id;

public class Stock {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stock)) {
            return false;
        }
        var otherStock = (Stock) obj;
        return name.equals(otherStock.name) && price.equals(otherStock.price) && quantity.equals(otherStock.quantity);
    }
}
