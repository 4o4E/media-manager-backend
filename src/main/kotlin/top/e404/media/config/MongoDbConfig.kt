package top.e404.media.config

import com.mongodb.client.MongoClients
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDbConfig {
    @Value("\${application.media.mongodb.uri}")
    lateinit var url: String

    @Value("\${application.media.mongodb.database}")
    lateinit var database: String

    @Value("\${application.media.mongodb.media}")
    lateinit var mediaCollection: String

    @get:Bean
    val client by lazy { MongoClients.create(url) }

    @get:Bean
    val db by lazy { client.getDatabase(database) }

    @get:Bean
    val media by lazy { db.getCollection(mediaCollection, BsonDocument::class.java) }
}