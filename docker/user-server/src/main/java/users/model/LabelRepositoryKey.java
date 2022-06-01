package users.model;

import java.io.Serializable;

public class LabelRepositoryKey implements Serializable {
    public int userId;
    public String name;

    public LabelRepositoryKey(int id, String stockName) {
        this.userId = id;
        this.name = stockName;
    }

    public LabelRepositoryKey() {
    }
}
