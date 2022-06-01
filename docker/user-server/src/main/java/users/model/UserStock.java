package users.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

@Entity(name = "userStock")
@IdClass(LabelRepositoryKey.class)
public class UserStock {
    @Id
    public int userId;
    @Id
    public String name;
    public int quantity;
    @Transient
    public Double price;

    public UserStock(int userId, String stockName, int amount) {
        this.userId = userId;
        this.name = stockName;
        this.quantity = amount;
    }

    public UserStock() {

    }

    public UserStock(int userId, String stockName, int amount, double price) {
        this.userId = userId;
        this.name = stockName;
        this.quantity = amount;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserStock userStock = (UserStock) o;
        return userId == userStock.userId && quantity == userStock.quantity && name.equals(userStock.name) && price.equals(userStock.price);
    }
}

