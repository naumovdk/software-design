package application

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository interface ProductCrudRepository : ReactiveMongoRepository<Product, Long>

@Repository interface UserCrudRepository : ReactiveMongoRepository<User, Long>