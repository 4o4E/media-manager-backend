package net.sf.log4jdbc

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

/**
 * Wraps a CallableStatement and reports method calls, returns and exceptions.
 *
 * @author Arthur Blake
 */
open class CallableStatementSpy(
    sql: String, connectionSpy: ConnectionSpy,
    /**
     * The real underlying CallableStatement that this CallableStatementSpy wraps.
     * -- GETTER --
     * Get the real underlying CallableStatement that this CallableStatementSpy wraps.
     *
     * @return the real underlying CallableStatement.
     */
    private val realCallableStatement: CallableStatement
) : PreparedStatementSpy(sql, connectionSpy, realCallableStatement), CallableStatement {

    override val classType = "CallableStatement"

    // forwarding methods
    @Throws(SQLException::class)
    override fun getDate(parameterIndex: Int): Date {
        val methodCall = "getDate($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDate(parameterIndex)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(parameterIndex: Int, cal: Calendar): Date {
        val methodCall = "getDate($parameterIndex, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDate(parameterIndex, cal)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getRef(parameterName: String): Ref {
        val methodCall = "getRef($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getRef(parameterName)) as Ref
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(parameterName: String): Time {
        val methodCall = "getTime($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTime(parameterName)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setTime(parameterName: String, x: Time) {
        val methodCall = "setTime($parameterName, $x)"
        try {
            realCallableStatement.setTime(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBlob(i: Int): Blob {
        val methodCall = "getBlob($i)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBlob(i)) as Blob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getClob(i: Int): Clob {
        val methodCall = "getClob($i)"
        try {
            return reportReturn(methodCall, realCallableStatement.getClob(i)) as Clob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getArray(i: Int): Array {
        val methodCall = "getArray($i)"
        try {
            return reportReturn(methodCall, realCallableStatement.getArray(i)) as Array
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBytes(parameterIndex: Int): ByteArray {
        val methodCall = "getBytes($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBytes(parameterIndex)) as ByteArray
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDouble(parameterIndex: Int): Double {
        val methodCall = "getDouble($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDouble(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getInt(parameterIndex: Int): Int {
        val methodCall = "getInt($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getInt(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun wasNull(): Boolean {
        val methodCall = "wasNull()"
        try {
            return reportReturn(methodCall, realCallableStatement.wasNull())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(parameterIndex: Int): Time {
        val methodCall = "getTime($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTime(parameterIndex)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(parameterIndex: Int, cal: Calendar): Time {
        val methodCall = "getTime($parameterIndex, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTime(parameterIndex, cal)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(parameterName: String): Timestamp {
        val methodCall = "getTimestamp($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTimestamp(parameterName)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setTimestamp(parameterName: String, x: Timestamp) {
        val methodCall = "setTimestamp($parameterName, $x)"
        try {
            realCallableStatement.setTimestamp(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getString(parameterIndex: Int): String {
        val methodCall = "getString($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getString(parameterIndex)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(parameterIndex: Int, sqlType: Int) {
        val methodCall = "registerOutParameter($parameterIndex, $sqlType)"
        argTraceSet(parameterIndex, null, "<OUT>")
        try {
            realCallableStatement.registerOutParameter(parameterIndex, sqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(parameterIndex: Int, sqlType: Int, scale: Int) {
        val methodCall = "registerOutParameter($parameterIndex, $sqlType, $scale)"
        argTraceSet(parameterIndex, null, "<OUT>")
        try {
            realCallableStatement.registerOutParameter(parameterIndex, sqlType, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(paramIndex: Int, sqlType: Int, typeName: String) {
        val methodCall = "registerOutParameter($paramIndex, $sqlType, $typeName)"
        argTraceSet(paramIndex, null, "<OUT>")
        try {
            realCallableStatement.registerOutParameter(paramIndex, sqlType, typeName)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getByte(parameterName: String): Byte {
        val methodCall = "getByte($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getByte(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDouble(parameterName: String): Double {
        val methodCall = "getDouble($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDouble(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getFloat(parameterName: String): Float {
        val methodCall = "getFloat($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getFloat(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getInt(parameterName: String): Int {
        val methodCall = "getInt($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getInt(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getLong(parameterName: String): Long {
        val methodCall = "getLong($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getLong(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getShort(parameterName: String): Short {
        val methodCall = "getShort($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getShort(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBoolean(parameterName: String): Boolean {
        val methodCall = "getBoolean($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBoolean(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBytes(parameterName: String): ByteArray {
        val methodCall = "getBytes($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBytes(parameterName)) as ByteArray
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setByte(parameterName: String, x: Byte) {
        val methodCall = "setByte($parameterName, $x)"
        try {
            realCallableStatement.setByte(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setDouble(parameterName: String, x: Double) {
        val methodCall = "setDouble($parameterName, $x)"
        try {
            realCallableStatement.setDouble(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setFloat(parameterName: String, x: Float) {
        val methodCall = "setFloat($parameterName, $x)"
        try {
            realCallableStatement.setFloat(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(parameterName: String, sqlType: Int) {
        val methodCall = "registerOutParameter($parameterName, $sqlType)"
        try {
            realCallableStatement.registerOutParameter(parameterName, sqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setInt(parameterName: String, x: Int) {
        val methodCall = "setInt($parameterName, $x)"
        try {
            realCallableStatement.setInt(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNull(parameterName: String, sqlType: Int) {
        val methodCall = "setNull($parameterName, $sqlType)"
        try {
            realCallableStatement.setNull(parameterName, sqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(parameterName: String, sqlType: Int, scale: Int) {
        val methodCall = "registerOutParameter($parameterName, $sqlType, $scale)"
        try {
            realCallableStatement.registerOutParameter(parameterName, sqlType, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setLong(parameterName: String, x: Long) {
        val methodCall = "setLong($parameterName, $x)"
        try {
            realCallableStatement.setLong(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setShort(parameterName: String, x: Short) {
        val methodCall = "setShort($parameterName, $x)"
        try {
            realCallableStatement.setShort(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBoolean(parameterName: String, x: Boolean) {
        val methodCall = "setBoolean($parameterName, $x)"
        try {
            realCallableStatement.setBoolean(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBytes(parameterName: String, x: ByteArray) {
        // todo: dump byte array?
        val methodCall = "setBytes($parameterName, $x)"
        try {
            realCallableStatement.setBytes(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBoolean(parameterIndex: Int): Boolean {
        val methodCall = "getBoolean($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBoolean(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(parameterIndex: Int): Timestamp {
        val methodCall = "getTimestamp($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTimestamp(parameterIndex)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(parameterName: String, x: InputStream, length: Int) {
        val methodCall = "setAsciiStream($parameterName, $x, $length)"
        try {
            realCallableStatement.setAsciiStream(parameterName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterName: String, x: InputStream, length: Int) {
        val methodCall = "setBinaryStream($parameterName, $x, $length)"
        try {
            realCallableStatement.setBinaryStream(parameterName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterName: String, reader: Reader, length: Int) {
        val methodCall = "setCharacterStream($parameterName, $reader, $length)"
        try {
            realCallableStatement.setCharacterStream(parameterName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getObject(parameterName: String): Any? {
        val methodCall = "getObject($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setObject(parameterName: String, x: Any) {
        val methodCall = "setObject($parameterName, $x)"
        try {
            realCallableStatement.setObject(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setObject(parameterName: String, x: Any, targetSqlType: Int) {
        val methodCall = "setObject($parameterName, $x, $targetSqlType)"
        try {
            realCallableStatement.setObject(parameterName, x, targetSqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setObject(parameterName: String, x: Any, targetSqlType: Int, scale: Int) {
        val methodCall = "setObject($parameterName, $x, $targetSqlType, $scale)"
        try {
            realCallableStatement.setObject(parameterName, x, targetSqlType, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getTimestamp(parameterIndex: Int, cal: Calendar): Timestamp {
        val methodCall = "getTimestamp($parameterIndex, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTimestamp(parameterIndex, cal)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(parameterName: String, cal: Calendar): Date {
        val methodCall = "getDate($parameterName, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDate(parameterName, cal)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(parameterName: String, cal: Calendar): Time {
        val methodCall = "getTime($parameterName, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTime(parameterName, cal)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(parameterName: String, cal: Calendar): Timestamp {
        val methodCall = "getTimestamp($parameterName, $cal)"
        try {
            return reportReturn(methodCall, realCallableStatement.getTimestamp(parameterName, cal)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setDate(parameterName: String, x: Date, cal: Calendar) {
        val methodCall = "setDate($parameterName, $x, $cal)"
        try {
            realCallableStatement.setDate(parameterName, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTime(parameterName: String, x: Time, cal: Calendar) {
        val methodCall = "setTime($parameterName, $x, $cal)"
        try {
            realCallableStatement.setTime(parameterName, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTimestamp(parameterName: String, x: Timestamp, cal: Calendar) {
        val methodCall = "setTimestamp($parameterName, $x, $cal)"
        try {
            realCallableStatement.setTimestamp(parameterName, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getShort(parameterIndex: Int): Short {
        val methodCall = "getShort($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getShort(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getLong(parameterIndex: Int): Long {
        val methodCall = "getLong($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getLong(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getFloat(parameterIndex: Int): Float {
        val methodCall = "getFloat($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getFloat(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getRef(i: Int): Ref {
        val methodCall = "getRef($i)"
        try {
            return reportReturn(methodCall, realCallableStatement.getRef(i)) as Ref
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun getBigDecimal(parameterIndex: Int, scale: Int): BigDecimal {
        val methodCall = "getBigDecimal($parameterIndex, $scale)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBigDecimal(parameterIndex, scale)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getURL(parameterIndex: Int): URL {
        val methodCall = "getURL($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getURL(parameterIndex)) as URL
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(parameterIndex: Int): BigDecimal {
        val methodCall = "getBigDecimal($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBigDecimal(parameterIndex)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getByte(parameterIndex: Int): Byte {
        val methodCall = "getByte($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getByte(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getObject(parameterIndex: Int): Any? {
        val methodCall = "getObject($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getObject(parameterIndex: Int, map: Map<String, Class<*>>): Any? {
        val methodCall = "getObject($parameterIndex, $map)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterIndex, map))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getString(parameterName: String): String {
        val methodCall = "getString($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getString(parameterName)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun registerOutParameter(parameterName: String, sqlType: Int, typeName: String) {
        val methodCall = "registerOutParameter($parameterName, $sqlType, $typeName)"
        try {
            realCallableStatement.registerOutParameter(parameterName, sqlType, typeName)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNull(parameterName: String, sqlType: Int, typeName: String) {
        val methodCall = "setNull($parameterName, $sqlType, $typeName)"
        try {
            realCallableStatement.setNull(parameterName, sqlType, typeName)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setString(parameterName: String, x: String) {
        val methodCall = "setString($parameterName, $x)"

        try {
            realCallableStatement.setString(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(parameterName: String): BigDecimal {
        val methodCall = "getBigDecimal($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBigDecimal(parameterName)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getObject(parameterName: String, map: Map<String?, Class<*>?>): Any? {
        val methodCall = "getObject($parameterName, $map)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterName, map))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setBigDecimal(parameterName: String, x: BigDecimal) {
        val methodCall = "setBigDecimal($parameterName, $x)"
        try {
            realCallableStatement.setBigDecimal(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getURL(parameterName: String): URL {
        val methodCall = "getURL($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getURL(parameterName)) as URL
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getRowId(parameterIndex: Int): RowId {
        val methodCall = "getRowId($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getRowId(parameterIndex)) as RowId
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getRowId(parameterName: String): RowId {
        val methodCall = "getRowId($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getRowId(parameterName)) as RowId
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setRowId(parameterName: String, x: RowId) {
        val methodCall = "setRowId($parameterName, $x)"
        try {
            realCallableStatement.setRowId(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNString(parameterName: String, value: String) {
        val methodCall = "setNString($parameterName, $value)"
        try {
            realCallableStatement.setNString(parameterName, value)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNCharacterStream(parameterName: String, reader: Reader, length: Long) {
        val methodCall = "setNCharacterStream($parameterName, $reader, $length)"
        try {
            realCallableStatement.setNCharacterStream(parameterName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterName: String, value: NClob) {
        val methodCall = "setNClob($parameterName, $value)"
        try {
            realCallableStatement.setNClob(parameterName, value)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(parameterName: String, reader: Reader, length: Long) {
        val methodCall = "setClob($parameterName, $reader, $length)"
        try {
            realCallableStatement.setClob(parameterName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBlob(parameterName: String, inputStream: InputStream, length: Long) {
        val methodCall = "setBlob($parameterName, $inputStream, $length)"
        try {
            realCallableStatement.setBlob(parameterName, inputStream, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterName: String, reader: Reader, length: Long) {
        val methodCall = "setNClob($parameterName, $reader, $length)"
        try {
            realCallableStatement.setNClob(parameterName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getNClob(parameterIndex: Int): NClob {
        val methodCall = "getNClob($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNClob(parameterIndex)) as NClob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNClob(parameterName: String): NClob {
        val methodCall = "getNClob($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNClob(parameterName)) as NClob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setSQLXML(parameterName: String, xmlObject: SQLXML) {
        val methodCall = "setSQLXML($parameterName, $xmlObject)"
        try {
            realCallableStatement.setSQLXML(parameterName, xmlObject)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getSQLXML(parameterIndex: Int): SQLXML {
        val methodCall = "getSQLXML($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getSQLXML(parameterIndex)) as SQLXML
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getSQLXML(parameterName: String): SQLXML {
        val methodCall = "getSQLXML($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getSQLXML(parameterName)) as SQLXML
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNString(parameterIndex: Int): String {
        val methodCall = "getNString($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNString(parameterIndex)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNString(parameterName: String): String {
        val methodCall = "getNString($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNString(parameterName)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(parameterIndex: Int): Reader {
        val methodCall = "getNCharacterStream($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNCharacterStream(parameterIndex)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(parameterName: String): Reader {
        val methodCall = "getNCharacterStream($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getNCharacterStream(parameterName)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(parameterIndex: Int): Reader {
        val methodCall = "getCharacterStream($parameterIndex)"
        try {
            return reportReturn(methodCall, realCallableStatement.getCharacterStream(parameterIndex)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(parameterName: String): Reader {
        val methodCall = "getCharacterStream($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getCharacterStream(parameterName)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setBlob(parameterName: String, x: Blob) {
        val methodCall = "setBlob($parameterName, $x)"
        try {
            realCallableStatement.setBlob(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(parameterName: String, x: Clob) {
        val methodCall = "setClob($parameterName, $x)"
        try {
            realCallableStatement.setClob(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(parameterName: String, x: InputStream, length: Long) {
        val methodCall = "setAsciiStream($parameterName, $x, $length)"
        try {
            realCallableStatement.setAsciiStream(parameterName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterName: String, x: InputStream, length: Long) {
        val methodCall = "setBinaryStream($parameterName, $x, $length)"
        try {
            realCallableStatement.setBinaryStream(parameterName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterName: String, reader: Reader, length: Long) {
        val methodCall = "setCharacterStream($parameterName, $reader, $length)"
        try {
            realCallableStatement.setCharacterStream(parameterName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(parameterName: String, x: InputStream) {
        val methodCall = "setAsciiStream($parameterName, $x)"
        try {
            realCallableStatement.setAsciiStream(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterName: String, x: InputStream) {
        val methodCall = "setBinaryStream($parameterName, $x)"
        try {
            realCallableStatement.setBinaryStream(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterName: String, reader: Reader) {
        val methodCall = "setCharacterStream($parameterName, $reader)"
        try {
            realCallableStatement.setCharacterStream(parameterName, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNCharacterStream(parameterName: String, reader: Reader) {
        val methodCall = "setNCharacterStream($parameterName, $reader)"
        try {
            realCallableStatement.setNCharacterStream(parameterName, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(parameterName: String, reader: Reader) {
        val methodCall = "setClob($parameterName, $reader)"
        try {
            realCallableStatement.setClob(parameterName, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBlob(parameterName: String, inputStream: InputStream) {
        val methodCall = "setBlob($parameterName, $inputStream)"
        try {
            realCallableStatement.setBlob(parameterName, inputStream)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterName: String, reader: Reader) {
        val methodCall = "setNClob($parameterName, $reader)"
        try {
            realCallableStatement.setNClob(parameterName, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setURL(parameterName: String, `val`: URL) {
        val methodCall = "setURL($parameterName, $`val`)"
        try {
            realCallableStatement.setURL(parameterName, `val`)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getArray(parameterName: String): Array {
        val methodCall = "getArray($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getArray(parameterName)) as Array
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBlob(parameterName: String): Blob {
        val methodCall = "getBlob($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getBlob(parameterName)) as Blob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getClob(parameterName: String): Clob {
        val methodCall = "getClob($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getClob(parameterName)) as Clob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(parameterName: String): Date {
        val methodCall = "getDate($parameterName)"
        try {
            return reportReturn(methodCall, realCallableStatement.getDate(parameterName)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setDate(parameterName: String, x: Date) {
        val methodCall = "setDate($parameterName, $x)"
        try {
            realCallableStatement.setDate(parameterName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>?): T {
        val methodCall = "unwrap(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            return reportReturn(
                methodCall,
                if ((iface != null
                            && (iface == CallableStatement::class.java
                            || iface == PreparedStatement::class.java
                            || iface == Statement::class.java
                            || iface == Spy::class.java))
                ) this
                else realCallableStatement.unwrap(iface)
            ) as T
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        val methodCall = "isWrapperFor(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            // NOTE: could call super.isWrapperFor to simplify this logic, but it would result in extra log output
            // when the super classes would be invoked..
            return reportReturn(
                methodCall,
                (iface != null && (iface == CallableStatement::class.java || iface == PreparedStatement::class.java || iface == Statement::class.java || iface == Spy::class.java)) ||
                        realCallableStatement.isWrapperFor(iface)
            )
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> getObject(parameterIndex: Int, type: Class<T>?): T {
        val name = if (type == null) "null" else type.name
        val methodCall = "getObject($parameterIndex, $name)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterIndex, type)) as T
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> getObject(parameterName: String, type: Class<T>?): T {
        val name = if (type == null) "null" else type.name
        val methodCall = "getObject($parameterName, $name)"
        try {
            return reportReturn(methodCall, realCallableStatement.getObject(parameterName, type)) as T
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }
}
