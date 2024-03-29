package net.sf.log4jdbc

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.sql.Date as SqlDate

/**
 * RDBMS specifics for the MySql db.
 *
 * @author Arthur Blake
 */
internal class MySqlRdbmsSpecifics : RdbmsSpecifics() {
    override fun formatParameterObject(obj: Any?) = when (obj) {
        is Time -> "'${SimpleDateFormat("HH:mm:ss").format(obj)}'"
        is SqlDate -> "'${SimpleDateFormat("yyyy-MM-dd").format(obj)}'"
        is Date -> "'${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj)}'"
        else -> super.formatParameterObject(obj)
    }
}