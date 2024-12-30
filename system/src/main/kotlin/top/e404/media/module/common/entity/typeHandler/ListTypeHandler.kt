package top.e404.media.module.common.entity.typeHandler

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import top.e404.media.module.common.util.log
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


abstract class ListTypeHandler<T : Any>(
    private val tJdbcType: String
) : BaseTypeHandler<List<T>>() {
    private val logger = log()

    abstract fun List<T>.toArray(): Array<T>

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: List<T>?, jdbcType: JdbcType?) {
        val array = ps.connection.createArrayOf(tJdbcType, parameter?.toArray())
        ps.setArray(i, array)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String?): List<T>? {
        return rs.getArray(columnName).toList()
    }


    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): List<T>? {
        return rs.getArray(columnIndex).toList()
    }


    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): List<T>? {
        return cs.getArray(columnIndex).toList()
    }

    private fun java.sql.Array?.toList(): List<T>? {
        if (this == null) return null
        try {
            return (this.array as? Array<T>)?.toList()
        } catch (e: SQLException) {
            logger.warn("ArrayTypeHandler getArray SQLException", e)
            return null
        }
    }
}

class TextListTypeHandler : ListTypeHandler<String>("text") {
    override fun List<String>.toArray() = this.toTypedArray()
}
class VarcharListTypeHandler : ListTypeHandler<String>("varchar") {
    override fun List<String>.toArray() = this.toTypedArray()
}
class IntListTypeHandler : ListTypeHandler<Int>("int") {
    override fun List<Int>.toArray() = this.toTypedArray()
}
class LongListTypeHandler : ListTypeHandler<Long>("bigint") {
    override fun List<Long>.toArray() = this.toTypedArray()
}