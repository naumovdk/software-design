package application

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
object ApplicationConfiguration : AbstractReactiveMongoConfiguration() {

    private val configuration = ConfigurationProperties.fromResource("defaults.properties")

    val conversions = mapOf(
        Currency.USD to mapOf(
            Currency.RUB to 90.0,
            Currency.EUR to 1.0,
        ),
        Currency.EUR to mapOf(
            Currency.RUB to 90.0,
            Currency.USD to 1.0,
        ),
        Currency.RUB to mapOf(
            Currency.USD to 0.013,
            Currency.EUR to 0.013,
        ),
    )

    @Bean fun mongoClient(): MongoClient = MongoClients.create(configuration[Key("mongo.url", stringType)])

    override fun getDatabaseName() = configuration[Key("mongo.database.name", stringType)]

    override fun reactiveMongoClient() = mongoClient()
}