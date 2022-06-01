package users.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "users")
public class User {
    @Id
    public int id;
    public long money;

    public User(int id, int i) {
        this.id = id;
        this.money = i;
    }

    public User() {

    }
}
