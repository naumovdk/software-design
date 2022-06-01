package application.service

import application.Product
import application.ProductCrudRepository
import application.User
import application.UserCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class UserService(
    private val userRepository: UserCrudRepository,
    private val productRepository: ProductCrudRepository,
) {
    fun add(user: User): Mono<User> = userRepository.insert(user)

    operator fun get(id: Long): Mono<User?> = userRepository.findById(id)

    fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    fun getProducts(id: Long): Flux<Product> =
        userRepository.findById(id)
            .flatMapMany { user -> productRepository.findAll().map { it!!.convertCurrency(user!!.currency) } }
}