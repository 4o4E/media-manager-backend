@file:Suppress("UNUSED")

package top.e404.media.util

import org.bson.*

fun bson(value: String) = BsonString(value)
fun bson(value: Int) = BsonInt32(value)
fun bson(value: Long) = BsonInt64(value)
fun bson(value: Float) = BsonDouble(value.toDouble())
fun bson(value: Double) = BsonDouble(value)

fun bson(key: String, value: BsonValue) = BsonDocument(key, value)
fun bson(key: String, value: String) = BsonDocument(key, bson(value))
fun bson(key: String, value: Int) = BsonDocument(key, bson(value))
fun bson(key: String, value: Long) = BsonDocument(key, bson(value))
fun bson(key: String, value: Float) = BsonDocument(key, bson(value))
fun bson(key: String, value: Double) = BsonDocument(key, bson(value))

fun buildBson(block: BsonDocument.() -> Unit) = BsonDocument().apply(block)
fun bson(vararg elements: Pair<String, BsonValue>) = buildBson {
    for ((k, v) in elements) {
        append(k, v)
    }
}
fun BsonDocument.deepPut(vararg path: String, value: BsonValue) = apply {
    var document: BsonDocument = this
    for ((index, key) in path.withIndex()) {
        if (index == path.size - 1) {
            document[key] = value
            break
        }
        val v = document.getOrPut(key) { bson() }
        require (v is BsonDocument)
        document = v
    }
}
fun bsonArray(vararg elements: BsonValue) = BsonArray(elements.toList())
fun bsonArray(vararg list: String) = BsonArray(list.map(::BsonString))
fun bsonArray(elements: Iterable<String>) = BsonArray(elements.map(::BsonString))
fun bsonArray(vararg list: Int) = BsonArray(list.map(::BsonInt32))
fun bsonArray(elements: Iterable<Int>) = BsonArray(elements.map(::BsonInt32))
fun bsonArray(vararg list: Long) = BsonArray(list.map(::BsonInt64))
fun bsonArray(elements: Iterable<Long>) = BsonArray(elements.map(::BsonInt64))
fun bsonArray(vararg list: Float) = BsonArray(list.map{ BsonDouble(it.toDouble()) })
fun bsonArray(elements: Iterable<Float>) = BsonArray(elements.map{ BsonDouble(it.toDouble()) })
fun bsonArray(vararg list: Double) = BsonArray(list.map(::BsonDouble))
fun bsonArray(elements: Iterable<Double>) = BsonArray(elements.map(::BsonDouble))

fun bsonIn(element: BsonValue) = BsonDocument("\$in", element)
fun bsonIn(key: String, value: BsonValue) = BsonDocument("\$in", bson(key, value))
fun bsonAny(element: BsonValue) = BsonDocument("\$any", element)
fun bsonAny(key: String, value: BsonValue) = BsonDocument("\$any", bson(key, value))
fun bsonSet(element: BsonValue) = BsonDocument("\$set", element)
fun bsonSet(key: String, value: BsonValue) = BsonDocument("\$set", bson(key, value))
fun bsonEach(elements: BsonArray) = BsonDocument("\$each", elements)
fun bsonEach(key: String, elements: BsonArray) = BsonDocument("\$each", bson(key, elements))
fun bsonAddToSet(document: BsonDocument) = BsonDocument("\$addToSet", document)
fun bsonAddToSet(key: String, value: BsonValue) = BsonDocument("\$addToSet", bson(key, value))
fun bsonPull(document: BsonDocument) = BsonDocument("\$pull", document)
fun bsonPull(key: String, value: BsonValue) = BsonDocument("\$pull", bson(key, value))
fun bsonPush(document: BsonDocument) = BsonDocument("\$push", document)
fun bsonPush(key: String, value: BsonValue) = BsonDocument("\$push", bson(key, value))
fun bsonInc(document: BsonDocument) = BsonDocument("\$inc", document)
fun bsonInc(key: String, value: BsonValue) = BsonDocument("\$inc", bson(key, value))
fun bsonMatch(document: BsonDocument) = BsonDocument("\$match", document)
fun bsonMatch(key: String, value: BsonValue) = BsonDocument("\$match", bson(key, value))
fun bsonProject(document: BsonDocument) = BsonDocument("\$project", document)
fun bsonProject(key: String, value: BsonValue) = BsonDocument("\$project", bson(key, value))
fun bsonSlice(array: BsonArray) = BsonDocument("\$slice", array)
fun bsonSlice(vararg values: BsonValue) = BsonDocument("\$slice", bsonArray(*values))