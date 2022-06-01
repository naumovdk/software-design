package users.model;

import java.util.Objects;

public class UserInfo {
    private int id;
    private double money;

    public UserInfo(int id, double money) {
        this.id = id;
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    public UserInfo setMoney(double money) {
        this.money = money;
        return this;
    }

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id && Double.compare(userInfo.money, money) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money);
    }
}
