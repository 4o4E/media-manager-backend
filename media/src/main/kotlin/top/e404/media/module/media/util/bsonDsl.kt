@file:Suppress("UNUSED")

package top.e404.media.module.media.util

import org.bson.*
import kotlin.reflect.KClass

// 创建bson基本类型
fun bson(value: String) = BsonString(value)
fun bson(value: Int) = BsonInt32(value)
fun bson(value: Long) = BsonInt64(value)
fun bson(value: Float) = BsonDouble(value.toDouble())
fun bson(value: Double) = BsonDouble(value)

// 创建bson对象
fun bson(key: String, value: BsonValue) = BsonDocument(key, value)
fun bson(key: String, value: String) = BsonDocument(key, bson(value))
fun bson(key: String, value: Int) = BsonDocument(key, bson(value))
fun bson(key: String, value: Long) = BsonDocument(key, bson(value))
fun bson(key: String, value: Float) = BsonDocument(key, bson(value))
fun bson(key: String, value: Double) = BsonDocument(key, bson(value))

// 创建复杂bson对象
fun buildBson(block: BsonDocument.() -> Unit) = BsonDocument().apply(block)
// 创建复杂bson对象
fun bson(vararg elements: Pair<String, BsonValue>) = buildBson {
    for ((k, v) in elements) {
        append(k, v)
    }
}

// 通过路径设置bson对象的对应bson元素
fun BsonDocument.deepPut(vararg path: String, value: BsonValue) = apply {
    var document: BsonDocument = this
    for ((index, key) in path.withIndex()) {
        if (index == path.size - 1) {
            document[key] = value
            break
        }
        val v = document.getOrPut(key) { bson() }
        require(v is BsonDocument)
        document = v
    }
}

// 构建bson数组
fun bsonArray(vararg elements: BsonValue) = BsonArray(elements.toList())
fun bsonArray(vararg list: String) = BsonArray(list.map(::BsonString))
inline fun <reified T : Any> bsonArray(elements: Collection<T>): BsonArray = bsonArray(elements, T::class)

@PublishedApi
internal fun <T : Any> bsonArray(elements: Collection<T>, cls: KClass<T>): BsonArray {
    if (elements.isEmpty()) return BsonArray()
    @Suppress("UNCHECKED_CAST")
    return when (cls) {
        String::class -> (elements as Collection<String>).map(::BsonString)
        Int::class -> (elements as Collection<Int>).map(::BsonInt32)
        Long::class -> (elements as Collection<Long>).map(::BsonInt64)
        Float::class -> (elements as Collection<Float>).map { BsonDouble(it.toDouble()) }
        Double::class -> (elements as Collection<Double>).map(::BsonDouble)
        else -> throw IllegalArgumentException("cannot cast ${cls.qualifiedName} to BsonValue")
    }.let(::BsonArray)
}

fun bsonArray(vararg list: Int) = BsonArray(list.map(::BsonInt32))
fun bsonArray(vararg list: Long) = BsonArray(list.map(::BsonInt64))
fun bsonArray(vararg list: Float) = BsonArray(list.map { BsonDouble(it.toDouble()) })
fun bsonArray(vararg list: Double) = BsonArray(list.map(::BsonDouble))

// 构建mongodb操作符对应的bson

// 查询包含任意元素的内容
fun bsonIn(element: BsonValue) = BsonDocument("\$in", element)
fun bsonIn(key: String, value: BsonValue) = BsonDocument("\$in", bson(key, value))
// 查询包含全部元素的内容
fun bsonAll(element: BsonValue) = BsonDocument("\$all", element)
fun bsonAll(key: String, value: BsonValue) = BsonDocument("\$all", bson(key, value))
// 设置操作
fun bsonSet(element: BsonValue) = BsonDocument("\$set", element)
fun bsonSet(key: String, value: BsonValue) = BsonDocument("\$set", bson(key, value))
// 遍历操作
fun bsonEach(elements: BsonArray) = BsonDocument("\$each", elements)
fun bsonEach(key: String, elements: BsonArray) = BsonDocument("\$each", bson(key, elements))
// 添加操作 无序不重复
fun bsonAddToSet(document: BsonDocument) = BsonDocument("\$addToSet", document)
fun bsonAddToSet(key: String, value: BsonValue) = BsonDocument("\$addToSet", bson(key, value))
// 移除操作
fun bsonPull(document: BsonDocument) = BsonDocument("\$pull", document)
fun bsonPull(key: String, value: BsonValue) = BsonDocument("\$pull", bson(key, value))
// 添加操作 有序可重复
fun bsonPush(document: BsonDocument) = BsonDocument("\$push", document)
fun bsonPush(key: String, value: BsonValue) = BsonDocument("\$push", bson(key, value))
// +=
fun bsonInc(document: BsonDocument) = BsonDocument("\$inc", document)
fun bsonInc(key: String, value: BsonValue) = BsonDocument("\$inc", bson(key, value))
// 匹配操作
fun bsonMatch(document: BsonDocument) = BsonDocument("\$match", document)
fun bsonMatch(key: String, value: BsonValue) = BsonDocument("\$match", bson(key, value))
// 选择操作
fun bsonProject(document: BsonDocument) = BsonDocument("\$project", document)
fun bsonProject(key: String, value: BsonValue) = BsonDocument("\$project", bson(key, value))
// 切片操作
fun bsonSlice(array: BsonArray) = BsonDocument("\$slice", array)
fun bsonSlice(vararg values: BsonValue) = BsonDocument("\$slice", bsonArray(*values))
// 随机选择
fun bsonSample(count: Long) = BsonDocument("\$sample", bson("size", count))
// 排序
fun bsonSort(field: String, desc: Boolean = false) = bson("\$sort", bson(field, if (desc) -1 else 1))
// 跳过
fun bsonSkip(count: Long) = bson("\$skip", count)
// 限制大小
fun bsonLimit(count: Long) = bson("\$limit", count)