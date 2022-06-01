package users.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import users.model.LabelRepositoryKey;
import users.model.UserStock;

public interface StockRepo extends CrudRepository<UserStock, LabelRepositoryKey> {
    public List<UserStock> findAllByUserId(int userId);
}
