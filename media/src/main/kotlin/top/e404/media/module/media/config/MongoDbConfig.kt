package top.e404.media.module.media.config

import com.mongodb.client.MongoClients
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDbConfig {
    /**
     * mongodb url
     */
    @Value("\${application.media.mongodb.uri}")
    lateinit var url: String

    /**
     * mongodb 数据库名字
     */
    @Value("\${application.media.mongodb.database}")
    lateinit var database: String

    /**
     * mongodb 集合名字
     */
    @Value("\${application.media.mongodb.media}")
    lateinit var mediaCollection: String

    /**
     * mongodb 客户端
     */
    @get:Bean
    val client by lazy { MongoClients.create(url) }

    /**
     * mongodb 数据库
     */
    @get:Bean
    val db by lazy { client.getDatabase(database) }

    /**
     * mongodb 集合
     */
    @get:Bean
    val media by lazy { db.getCollection(mediaCollection, BsonDocument::class.java) }
}