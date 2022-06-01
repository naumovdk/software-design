package users.repo;

import org.springframework.data.repository.CrudRepository;
import users.model.User;

public interface UserRepo extends CrudRepository<User, Integer> {
}
