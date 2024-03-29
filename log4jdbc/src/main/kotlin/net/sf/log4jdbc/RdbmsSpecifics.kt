package net.sf.log4jdbc

import java.text.SimpleDateFormat
import java.util.*

/**
 * Encapsulate sql formatting details about a particular relational database management system so that
 * accurate, useable SQL can be composed for that RDMBS.
 *
 * @author Arthur Blake
 */
open class RdbmsSpecifics {
    /**
     * Format an Object that is being bound to a PreparedStatement parameter, for display. The goal is to reformat the
     * object in a format that can be re-run against the native SQL client of the particular RDBMS being used.  This
     * class should be extended to provide formatting instances that format objects correctly for different RDBMS
     * types.
     *
     * @param `object` jdbc object to be formatted.
     * @return formatted dump of the object.
     */
    open fun formatParameterObject(obj: Any?): String? {
        return when (obj) {
            null -> "NULL"
            is String -> "'${escapeString(obj)}'"
            is Date -> "'${SimpleDateFormat(DATE_FORMAT).format(obj)}'"
            is Boolean -> if (obj) "true" else "false"
            else -> obj.toString()
        }
    }

    /**
     * Make sure string is escaped properly so that it will run in a SQL query analyzer tool.
     * At this time all we do is double any single tick marks.
     * Do not call this with a null string or else an exception will occur.
     *
     * @return the input String, escaped.
     */
    private fun escapeString(input: String) = buildString {
        var i = 0
        val j = input.length
        while (i < j) {
            val c = input[i]
            if (c == '\'') {
                append(c)
            }
            append(c)
            i++
        }
    }

    companion object {
        protected const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    }
}
