package application.service

import application.Product
import application.ProductCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProductService(private val productRepository: ProductCrudRepository) {

    fun add(product: Product): Mono<Product> = productRepository.insert(product)

    operator fun get(id: Long): Mono<Product?> = productRepository.findById(id)

    fun delete(id: Long) {
        productRepository.deleteById(id)
    }
}