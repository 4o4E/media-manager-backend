package top.e404.media.module.media.typeHandler

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import top.e404.media.module.common.config.JsonConfig
import top.e404.media.module.media.entity.data.MediaElement
import top.e404.media.module.media.entity.mediaListSerializer
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List::class)
class MediaContentTypeHandler : BaseTypeHandler<List<MediaElement>>() {
    override fun setNonNullParameter(
        ps: PreparedStatement,
        i: Int,
        parameter: List<MediaElement>?,
        jdbcType: JdbcType?
    ) {
        ps.setString(
            i,
            if (parameter == null) null
            else JsonConfig.json.encodeToString(mediaListSerializer, parameter)
        )
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String?): List<MediaElement>? {
        return rs.getString(columnName).toList()
    }


    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): List<MediaElement>? {
        return rs.getString(columnIndex).toList()
    }


    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): List<MediaElement>? {
        return cs.getString(columnIndex).toList()
    }

    private fun String?.toList(): List<MediaElement>? {
        if (this == null) return null
        return JsonConfig.json.decodeFromString(mediaListSerializer, this)
    }
}