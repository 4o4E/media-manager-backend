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
 * Wraps a PreparedStatement and reports method calls, returns and exceptions.
 *
 * @author Arthur Blake
 */
open class PreparedStatementSpy(
    private val sql: String,
    connectionSpy: ConnectionSpy,
    /**
     * The real PreparedStatement that this PreparedStatementSpy wraps.
     * -- GETTER --
     * Get the real PreparedStatement that this PreparedStatementSpy wraps.
     *
     * @return the real PreparedStatement that this PreparedStatementSpy wraps.
     */
    protected var realPreparedStatement: PreparedStatement
) : StatementSpy(connectionSpy, realPreparedStatement), PreparedStatement {
    /**
     * holds list of bind variables for tracing
     */
    protected val argTrace: MutableList<String?> = ArrayList()

    /**
     * Store an argument (bind variable) into the argTrace list (above) for later dumping.
     *
     * @param i          index of argument being set.
     * @param typeHelper optional additional info about the type that is being set in the arg
     * @param arg        argument being bound.
     */
    protected fun argTraceSet(i: Int, typeHelper: String?, arg: Any?) {
        var j = i
        var tracedArg: String?
        try {
            tracedArg = rdbmsSpecifics!!.formatParameterObject(arg)
        } catch (t: Throwable) {
            // rdbmsSpecifics should NEVER EVER throw an exception!!
            // but just in case it does, we trap it.
            log.debug(
                "rdbmsSpecifics threw an exception while trying to format a " +
                        "parameter object [" + arg + "] this is very bad!!! (" +
                        t.message + ")"
            )

            // backup - so that at least we won't harm the application using us
            tracedArg = arg?.toString() ?: "null"
        }

        j-- // make the index 0 based
        synchronized(argTrace) {
            // if an object is being inserted out of sequence, fill up missing values with null...
            while (j >= argTrace.size) {
                argTrace.add(argTrace.size, null)
            }
            if (!showTypeHelp || typeHelper == null) {
                argTrace.set(j, tracedArg)
            } else {
                argTrace.set(j, typeHelper + tracedArg)
            }
        }
    }

    protected fun dumpedSql(): String {
        val dumpSql = StringBuffer()
        var lastPos = 0
        var Qpos = sql.indexOf('?', lastPos) // find position of first question mark
        var argIdx = 0
        var arg: String?

        while (Qpos != -1) {
            // get stored argument
            synchronized(argTrace) {
                arg = try {
                    argTrace[argIdx]
                } catch (e: IndexOutOfBoundsException) {
                    "?"
                }
            }
            if (arg == null) {
                arg = "?"
            }

            argIdx++

            dumpSql.append(sql, lastPos, Qpos) // dump segment of sql up to question mark.
            lastPos = Qpos + 1
            Qpos = sql.indexOf('?', lastPos)
            dumpSql.append(arg)
        }
        if (lastPos < sql.length) {
            dumpSql.append(sql.substring(lastPos)) // dump last segment
        }

        return dumpSql.toString()
    }

    override fun reportAllReturns(methodCall: String, msg: String) {
        super.reportAllReturns(methodCall, msg)
    }

    /**
     * RdbmsSpecifics for formatting SQL for the given RDBMS.
     */
    protected var rdbmsSpecifics: RdbmsSpecifics? = connectionSpy.rdbmsSpecifics

    override val classType: String
        get() = "PreparedStatement"

    // forwarding methods
    @Throws(SQLException::class)
    override fun setTime(parameterIndex: Int, x: Time) {
        val methodCall = "setTime($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(Time)", x)
        try {
            realPreparedStatement.setTime(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTime(parameterIndex: Int, x: Time, cal: Calendar) {
        val methodCall = "setTime($parameterIndex, $x, $cal)"
        argTraceSet(parameterIndex, "(Time)", x)
        try {
            realPreparedStatement.setTime(parameterIndex, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterIndex: Int, reader: Reader, length: Int) {
        val methodCall = "setCharacterStream($parameterIndex, $reader, $length)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader of length $length>")
        try {
            realPreparedStatement.setCharacterStream(parameterIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNull(parameterIndex: Int, sqlType: Int) {
        val methodCall = "setNull($parameterIndex, $sqlType)"
        argTraceSet(parameterIndex, null, null)
        try {
            realPreparedStatement.setNull(parameterIndex, sqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNull(paramIndex: Int, sqlType: Int, typeName: String) {
        val methodCall = "setNull($paramIndex, $sqlType, $typeName)"
        argTraceSet(paramIndex, null, null)
        try {
            realPreparedStatement.setNull(paramIndex, sqlType, typeName)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setRef(i: Int, x: Ref) {
        val methodCall = "setRef($i, $x)"
        argTraceSet(i, "(Ref)", x)
        try {
            realPreparedStatement.setRef(i, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBoolean(parameterIndex: Int, x: Boolean) {
        val methodCall = "setBoolean($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(boolean)", if (x) java.lang.Boolean.TRUE else java.lang.Boolean.FALSE)
        try {
            realPreparedStatement.setBoolean(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBlob(i: Int, x: Blob?) {
        val methodCall = "setBlob($i, $x)"
        argTraceSet(
            i, "(Blob)",
            if (x == null) null else ("<Blob of size " + x.length() + ">")
        )
        try {
            realPreparedStatement.setBlob(i, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(i: Int, x: Clob?) {
        val methodCall = "setClob($i, $x)"
        argTraceSet(
            i, "(Clob)",
            if (x == null) null else ("<Clob of size " + x.length() + ">")
        )
        try {
            realPreparedStatement.setClob(i, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setArray(i: Int, x: Array) {
        val methodCall = "setArray($i, $x)"
        argTraceSet(i, "(Array)", "<Array>")
        try {
            realPreparedStatement.setArray(i, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setByte(parameterIndex: Int, x: Byte) {
        val methodCall = "setByte($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(byte)", x)
        try {
            realPreparedStatement.setByte(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun setUnicodeStream(parameterIndex: Int, x: InputStream, length: Int) {
        val methodCall = "setUnicodeStream($parameterIndex, $x, $length)"
        argTraceSet(parameterIndex, "(Unicode InputStream)", "<Unicode InputStream of length $length>")
        try {
            realPreparedStatement.setUnicodeStream(parameterIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setShort(parameterIndex: Int, x: Short) {
        val methodCall = "setShort($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(short)", x)
        try {
            realPreparedStatement.setShort(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun execute(): Boolean {
        val methodCall = "execute()"
        val dumpedSql = dumpedSql()
        reportSql(dumpedSql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result: Boolean = realPreparedStatement.execute()
            reportSqlTiming(System.currentTimeMillis() - tstart, dumpedSql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setInt(parameterIndex: Int, x: Int) {
        val methodCall = "setInt($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(int)", x)
        try {
            realPreparedStatement.setInt(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setLong(parameterIndex: Int, x: Long) {
        val methodCall = "setLong($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(long)", x)
        try {
            realPreparedStatement.setLong(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setFloat(parameterIndex: Int, x: Float) {
        val methodCall = "setFloat($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(float)", x)
        try {
            realPreparedStatement.setFloat(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setDouble(parameterIndex: Int, x: Double) {
        val methodCall = "setDouble($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(double)", x)
        try {
            realPreparedStatement.setDouble(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBigDecimal(parameterIndex: Int, x: BigDecimal) {
        val methodCall = "setBigDecimal($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(BigDecimal)", x)
        try {
            realPreparedStatement.setBigDecimal(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setURL(parameterIndex: Int, x: URL) {
        val methodCall = "setURL($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(URL)", x)

        try {
            realPreparedStatement.setURL(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setString(parameterIndex: Int, x: String) {
        val methodCall = "setString($parameterIndex, \"$x\")"
        argTraceSet(parameterIndex, "(String)", x)

        try {
            realPreparedStatement.setString(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBytes(parameterIndex: Int, x: ByteArray) {
        val methodCall = "setBytes($parameterIndex, $x)"

        val sb = StringBuilder()
        for (b in x) {
            sb.append(String.format("%02X", b))
        }
        argTraceSet(parameterIndex, "(byte[])", sb.toString())

        try {
            realPreparedStatement.setBytes(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setDate(parameterIndex: Int, x: Date) {
        val methodCall = "setDate($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(Date)", x)
        try {
            realPreparedStatement.setDate(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getParameterMetaData(): ParameterMetaData {
        val methodCall = "getParameterMetaData()"
        try {
            return reportReturn(methodCall, realPreparedStatement.getParameterMetaData()) as ParameterMetaData
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setRowId(parameterIndex: Int, x: RowId) {
        val methodCall = "setRowId($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(RowId)", x)
        try {
            realPreparedStatement.setRowId(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNString(parameterIndex: Int, value: String) {
        val methodCall = "setNString($parameterIndex, $value)"
        argTraceSet(parameterIndex, "(String)", value)
        try {
            realPreparedStatement.setNString(parameterIndex, value)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNCharacterStream(parameterIndex: Int, value: Reader, length: Long) {
        val methodCall = "setNCharacterStream($parameterIndex, $value, $length)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader of length $length>")
        try {
            realPreparedStatement.setNCharacterStream(parameterIndex, value, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterIndex: Int, value: NClob) {
        val methodCall = "setNClob($parameterIndex, $value)"
        argTraceSet(parameterIndex, "(NClob)", "<NClob>")
        try {
            realPreparedStatement.setNClob(parameterIndex, value)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(parameterIndex: Int, reader: Reader, length: Long) {
        val methodCall = "setClob($parameterIndex, $reader, $length)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader of length $length>")
        try {
            realPreparedStatement.setClob(parameterIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBlob(parameterIndex: Int, inputStream: InputStream, length: Long) {
        val methodCall = "setBlob($parameterIndex, $inputStream, $length)"
        argTraceSet(parameterIndex, "(InputStream)", "<InputStream of length $length>")
        try {
            realPreparedStatement.setBlob(parameterIndex, inputStream, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterIndex: Int, reader: Reader, length: Long) {
        val methodCall = "setNClob($parameterIndex, $reader, $length)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader of length $length>")
        try {
            realPreparedStatement.setNClob(parameterIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setSQLXML(parameterIndex: Int, xmlObject: SQLXML) {
        val methodCall = "setSQLXML($parameterIndex, $xmlObject)"
        argTraceSet(parameterIndex, "(SQLXML)", xmlObject)
        try {
            realPreparedStatement.setSQLXML(parameterIndex, xmlObject)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setDate(parameterIndex: Int, x: Date, cal: Calendar) {
        val methodCall = "setDate($parameterIndex, $x, $cal)"
        argTraceSet(parameterIndex, "(Date)", x)

        try {
            realPreparedStatement.setDate(parameterIndex, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun executeQuery(): ResultSet {
        val methodCall = "executeQuery()"
        val dumpedSql = dumpedSql()
        reportSql(dumpedSql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val r: ResultSet = realPreparedStatement.executeQuery()
            reportSqlTiming(System.currentTimeMillis() - tstart, dumpedSql, methodCall)
            val rsp = ResultSetSpy(this, r)
            return reportReturn(methodCall, rsp) as ResultSet
        } catch (s: SQLException) {
            reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    private fun getTypeHelp(x: Any?): String {
        return if (x == null) {
            "(null)"
        } else {
            "(" + x.javaClass.name + ")"
        }
    }

    @Throws(SQLException::class)
    override fun setObject(parameterIndex: Int, x: Any, targetSqlType: Int, scale: Int) {
        val methodCall = "setObject($parameterIndex, $x, $targetSqlType, $scale)"
        argTraceSet(parameterIndex, getTypeHelp(x), x)

        try {
            realPreparedStatement.setObject(parameterIndex, x, targetSqlType, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large ASCII value is input to a `LONGVARCHAR`
     * parameter, it may be more practical to send it via a
     * `java.io.InputStream`. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     *
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x              the Java input stream that contains the ASCII parameter value
     * @param length         the number of bytes in the stream
     * @throws java.sql.SQLException if parameterIndex does not correspond to a parameter
     * marker in the SQL statement; if a database access error occurs or
     * this method is called on a closed `PreparedStatement`
     * @since 1.6
    </P> */
    @Throws(SQLException::class)
    override fun setAsciiStream(parameterIndex: Int, x: InputStream, length: Long) {
        val methodCall = "setAsciiStream($parameterIndex, $x, $length)"
        argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream of length $length>")
        try {
            realPreparedStatement.setAsciiStream(parameterIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterIndex: Int, x: InputStream, length: Long) {
        val methodCall = "setBinaryStream($parameterIndex, $x, $length)"
        argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream of length $length>")
        try {
            realPreparedStatement.setBinaryStream(parameterIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterIndex: Int, reader: Reader, length: Long) {
        val methodCall = "setCharacterStream($parameterIndex, $reader, $length)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader of length $length>")
        try {
            realPreparedStatement.setCharacterStream(parameterIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(parameterIndex: Int, x: InputStream) {
        val methodCall = "setAsciiStream($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream>")
        try {
            realPreparedStatement.setAsciiStream(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterIndex: Int, x: InputStream) {
        val methodCall = "setBinaryStream($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream>")
        try {
            realPreparedStatement.setBinaryStream(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(parameterIndex: Int, reader: Reader) {
        val methodCall = "setCharacterStream($parameterIndex, $reader)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader>")
        try {
            realPreparedStatement.setCharacterStream(parameterIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNCharacterStream(parameterIndex: Int, reader: Reader) {
        val methodCall = "setNCharacterStream($parameterIndex, $reader)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader>")
        try {
            realPreparedStatement.setNCharacterStream(parameterIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setClob(parameterIndex: Int, reader: Reader) {
        val methodCall = "setClob($parameterIndex, $reader)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader>")
        try {
            realPreparedStatement.setClob(parameterIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBlob(parameterIndex: Int, inputStream: InputStream) {
        val methodCall = "setBlob($parameterIndex, $inputStream)"
        argTraceSet(parameterIndex, "(InputStream)", "<InputStream>")
        try {
            realPreparedStatement.setBlob(parameterIndex, inputStream)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNClob(parameterIndex: Int, reader: Reader) {
        val methodCall = "setNClob($parameterIndex, $reader)"
        argTraceSet(parameterIndex, "(Reader)", "<Reader>")
        try {
            realPreparedStatement.setNClob(parameterIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setObject(parameterIndex: Int, x: Any, targetSqlType: Int) {
        val methodCall = "setObject($parameterIndex, $x, $targetSqlType)"
        argTraceSet(parameterIndex, getTypeHelp(x), x)
        try {
            realPreparedStatement.setObject(parameterIndex, x, targetSqlType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setObject(parameterIndex: Int, x: Any) {
        val methodCall = "setObject($parameterIndex, $x)"
        argTraceSet(parameterIndex, getTypeHelp(x), x)
        try {
            realPreparedStatement.setObject(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTimestamp(parameterIndex: Int, x: Timestamp) {
        val methodCall = "setTimestamp($parameterIndex, $x)"
        argTraceSet(parameterIndex, "(Date)", x)
        try {
            realPreparedStatement.setTimestamp(parameterIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTimestamp(parameterIndex: Int, x: Timestamp, cal: Calendar) {
        val methodCall = "setTimestamp($parameterIndex, $x, $cal)"
        argTraceSet(parameterIndex, "(Timestamp)", x)
        try {
            realPreparedStatement.setTimestamp(parameterIndex, x, cal)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun executeUpdate(): Int {
        val methodCall = "executeUpdate()"
        val dumpedSql = dumpedSql()
        reportSql(dumpedSql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result: Int = realPreparedStatement.executeUpdate()
            reportSqlTiming(System.currentTimeMillis() - tstart, dumpedSql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, dumpedSql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(parameterIndex: Int, x: InputStream, length: Int) {
        val methodCall = "setAsciiStream($parameterIndex, $x, $length)"
        argTraceSet(parameterIndex, "(Ascii InputStream)", "<Ascii InputStream of length $length>")
        try {
            realPreparedStatement.setAsciiStream(parameterIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(parameterIndex: Int, x: InputStream, length: Int) {
        val methodCall = "setBinaryStream($parameterIndex, $x, $length)"
        argTraceSet(parameterIndex, "(Binary InputStream)", "<Binary InputStream of length $length>")
        try {
            realPreparedStatement.setBinaryStream(parameterIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun clearParameters() {
        val methodCall = "clearParameters()"

        synchronized(argTrace) {
            argTrace.clear()
        }

        try {
            realPreparedStatement.clearParameters()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getMetaData(): ResultSetMetaData {
        val methodCall = "getMetaData()"
        try {
            return reportReturn(methodCall, realPreparedStatement.getMetaData()) as ResultSetMetaData
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun addBatch() {
        val methodCall = "addBatch()"
        currentBatch.add(dumpedSql())
        try {
            realPreparedStatement.addBatch()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>?): T? {
        val methodCall = "unwrap(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            // todo: double check this logic
            // NOTE: could call super.isWrapperFor to simplify this logic, but it would result in extra log output
            // because the super classes would be invoked, thus executing their logging methods too...
            return reportReturn(
                methodCall,
                if (iface == PreparedStatement::class.java || iface == Statement::class.java || iface == Spy::class.java
                ) this
                else realPreparedStatement.unwrap<T>(iface)
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
                ((iface == PreparedStatement::class.java || iface == Statement::class.java || iface == Spy::class.java)) ||
                        realPreparedStatement.isWrapperFor(iface)
            )
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    companion object {
        // a way to turn on and off type help...
        // todo:  make this a configurable parameter
        // todo, debug arrays and streams in a more useful manner.... if possible
        private const val showTypeHelp = false
    }
}
