package net.sf.log4jdbc

import java.io.*
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

/**
 * Wraps a ResultSet and reports method calls, returns and exceptions.
 *
 * JDBC 4 version.
 *
 * @author Arthur Blake
 */
class ResultSetSpy(private val parent: StatementSpy, private val realResultSet: ResultSet) : ResultSet, Spy {
    init {
        reportReturn("new ResultSet")
    }


    /**
     * Report an exception to be logged.
     *
     * @param methodCall description of method call and arguments passed to it that generated the exception.
     * @param exception exception that was generated
     */
    protected fun reportException(methodCall: String, exception: SQLException?) {
        log.exceptionOccured(this, methodCall, exception, null, -1L)
    }

    /**
     * Report (for logging) that a method returned.  All the other reportReturn methods are conveniance methods that call
     * this method.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param msg description of what the return value that was returned.  may be an empty String for void return types.
     */
    protected fun reportAllReturns(methodCall: String, msg: String) {
        log.methodReturned(this, methodCall, msg)
    }

    override val connectionNumber get() = parent.connectionNumber

    /**
     * Conveniance method to report (for logging) that a method returned a boolean value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value boolean return value.
     * @return the boolean return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Boolean): Boolean {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a byte value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value byte return value.
     * @return the byte return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Byte): Byte {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a int value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value int return value.
     * @return the int return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Int): Int {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a double value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value double return value.
     * @return the double return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Double): Double {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a short value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value short return value.
     * @return the short return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Short): Short {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a long value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value long return value.
     * @return the long return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Long): Long {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned a float value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value float return value.
     * @return the float return value as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Float): Float {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned an Object.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value return Object.
     * @return the return Object as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Any): Any {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    /**
     * Conveniance method to report (for logging) that a method returned (void return type).
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     */
    protected fun reportReturn(methodCall: String) {
        reportAllReturns(methodCall, "")
    }

    // forwarding methods
    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Int) {
        val methodCall = "updateAsciiStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateAsciiStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnName: String, x: InputStream, length: Int) {
        val methodCall = "updateAsciiStream($columnName, $x, $length)"
        try {
            realResultSet.updateAsciiStream(columnName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getRow(): Int {
        val methodCall = "getRow()"
        try {
            return reportReturn(methodCall, realResultSet.row)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun cancelRowUpdates() {
        val methodCall = "cancelRowUpdates()"
        try {
            realResultSet.cancelRowUpdates()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int): Time {
        val methodCall = "getTime($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getTime(columnIndex)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(columnName: String): Time {
        val methodCall = "getTime($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getTime(columnName)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int, cal: Calendar): Time {
        val methodCall = "getTime($columnIndex, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getTime(columnIndex, cal)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTime(columnName: String, cal: Calendar): Time {
        val methodCall = "getTime($columnName, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getTime(columnName, cal)) as Time
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun absolute(row: Int): Boolean {
        val methodCall = "absolute($row)"
        try {
            return reportReturn(methodCall, realResultSet.absolute(row))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int): Timestamp {
        val methodCall = "getTimestamp($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getTimestamp(columnIndex)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnName: String): Timestamp {
        val methodCall = "getTimestamp($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getTimestamp(columnName)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int, cal: Calendar): Timestamp {
        val methodCall = "getTimestamp($columnIndex, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getTimestamp(columnIndex, cal)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnName: String, cal: Calendar): Timestamp {
        val methodCall = "getTimestamp($columnName, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getTimestamp(columnName, cal)) as Timestamp
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun moveToInsertRow() {
        val methodCall = "moveToInsertRow()"
        try {
            realResultSet.moveToInsertRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun relative(rows: Int): Boolean {
        val methodCall = "relative($rows)"
        try {
            return reportReturn(methodCall, realResultSet.relative(rows))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun previous(): Boolean {
        val methodCall = "previous()"
        try {
            return reportReturn(methodCall, realResultSet.previous())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun moveToCurrentRow() {
        val methodCall = "moveToCurrentRow()"
        try {
            realResultSet.moveToCurrentRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getRef(i: Int): Ref {
        val methodCall = "getRef($i)"
        try {
            return reportReturn(methodCall, realResultSet.getRef(i)) as Ref
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateRef(columnIndex: Int, x: Ref) {
        val methodCall = "updateRef($columnIndex, $x)"
        try {
            realResultSet.updateRef(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getRef(colName: String): Ref {
        val methodCall = "getRef($colName)"
        try {
            return reportReturn(methodCall, realResultSet.getRef(colName)) as Ref
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateRef(columnName: String, x: Ref) {
        val methodCall = "updateRef($columnName, $x)"
        try {
            realResultSet.updateRef(columnName, x)
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
            return reportReturn(methodCall, realResultSet.getBlob(i)) as Blob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, x: Blob) {
        val methodCall = "updateBlob($columnIndex, $x)"
        try {
            realResultSet.updateBlob(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBlob(colName: String): Blob {
        val methodCall = "getBlob($colName)"
        try {
            return reportReturn(methodCall, realResultSet.getBlob(colName)) as Blob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnName: String, x: Blob) {
        val methodCall = "updateBlob($columnName, $x)"
        try {
            realResultSet.updateBlob(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getClob(i: Int): Clob {
        val methodCall = "getClob($i)"
        try {
            return reportReturn(methodCall, realResultSet.getClob(i)) as Clob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, x: Clob) {
        val methodCall = "updateClob($columnIndex, $x)"
        try {
            realResultSet.updateClob(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getClob(colName: String): Clob {
        val methodCall = "getClob($colName)"
        try {
            return reportReturn(methodCall, realResultSet.getClob(colName)) as Clob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateClob(columnName: String, x: Clob) {
        val methodCall = "updateClob($columnName, $x)"
        try {
            realResultSet.updateClob(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBoolean(columnIndex: Int): Boolean {
        val methodCall = "getBoolean($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getBoolean(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBoolean(columnName: String): Boolean {
        val methodCall = "getBoolean($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getBoolean(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getArray(i: Int): Array {
        val methodCall = "getArray($i)"
        try {
            return reportReturn(methodCall, realResultSet.getArray(i)) as Array
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateArray(columnIndex: Int, x: Array) {
        val methodCall = "updateArray($columnIndex, $x)"
        try {
            realResultSet.updateArray(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getArray(colName: String): Array {
        val methodCall = "getArray($colName)"
        try {
            return reportReturn(methodCall, realResultSet.getArray(colName)) as Array
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateArray(columnName: String, x: Array) {
        val methodCall = "updateArray($columnName, $x)"
        try {
            realResultSet.updateArray(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getRowId(columnIndex: Int): RowId {
        val methodCall = "getRowId($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getRowId(columnIndex)) as RowId
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getRowId(columnLabel: String): RowId {
        val methodCall = "getRowId($columnLabel)"
        try {
            return reportReturn(methodCall, realResultSet.getRowId(columnLabel)) as RowId
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateRowId(columnIndex: Int, x: RowId) {
        val methodCall = "updateRowId($columnIndex, $x)"
        try {
            realResultSet.updateRowId(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateRowId(columnLabel: String, x: RowId) {
        val methodCall = "updateRowId($columnLabel, $x)"
        try {
            realResultSet.updateRowId(columnLabel, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getHoldability(): Int {
        val methodCall = "getHoldability()"
        try {
            return reportReturn(methodCall, realResultSet.holdability)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isClosed(): Boolean {
        val methodCall = "isClosed()"
        try {
            return reportReturn(methodCall, realResultSet.isClosed)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateNString(columnIndex: Int, nString: String) {
        val methodCall = "updateNString($columnIndex, $nString)"
        try {
            realResultSet.updateNString(columnIndex, nString)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNString(columnLabel: String, nString: String) {
        val methodCall = "updateNString($columnLabel, $nString)"
        try {
            realResultSet.updateNString(columnLabel, nString)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, nClob: NClob) {
        val methodCall = "updateNClob($columnIndex, $nClob)"
        try {
            realResultSet.updateNClob(columnIndex, nClob)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, nClob: NClob) {
        val methodCall = "updateNClob($columnLabel, $nClob)"
        try {
            realResultSet.updateNClob(columnLabel, nClob)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getNClob(columnIndex: Int): NClob {
        val methodCall = "getNClob($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getNClob(columnIndex)) as NClob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNClob(columnLabel: String): NClob {
        val methodCall = "getNClob($columnLabel)"
        try {
            return reportReturn(methodCall, realResultSet.getNClob(columnLabel)) as NClob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getSQLXML(columnIndex: Int): SQLXML {
        val methodCall = "getSQLXML($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getSQLXML(columnIndex)) as SQLXML
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getSQLXML(columnLabel: String): SQLXML {
        val methodCall = "getSQLXML($columnLabel)"
        try {
            return reportReturn(methodCall, realResultSet.getSQLXML(columnLabel)) as SQLXML
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML) {
        val methodCall = "updateSQLXML($columnIndex, $xmlObject)"
        try {
            realResultSet.updateSQLXML(columnIndex, xmlObject)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateSQLXML(columnLabel: String, xmlObject: SQLXML) {
        val methodCall = "updateSQLXML($columnLabel, $xmlObject)"
        try {
            realResultSet.updateSQLXML(columnLabel, xmlObject)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getNString(columnIndex: Int): String {
        val methodCall = "getNString($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getNString(columnIndex)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNString(columnLabel: String): String {
        val methodCall = "getNString($columnLabel)"
        try {
            return reportReturn(methodCall, realResultSet.getNString(columnLabel)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(columnIndex: Int): Reader {
        val methodCall = "getNCharacterStream($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getNCharacterStream(columnIndex)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(columnLabel: String): Reader {
        val methodCall = "getNCharacterStream($columnLabel)"
        try {
            return reportReturn(methodCall, realResultSet.getNCharacterStream(columnLabel)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        val methodCall = "updateNCharacterStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateNCharacterStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        val methodCall = "updateNCharacterStream($columnLabel, $reader, $length)"
        try {
            realResultSet.updateNCharacterStream(columnLabel, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) {
        val methodCall = "updateAsciiStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateAsciiStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) {
        val methodCall = "updateBinaryStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateBinaryStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        val methodCall = "updateCharacterStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateCharacterStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream, length: Long) {
        val methodCall = "updateAsciiStream($columnLabel, $x, $length)"
        try {
            realResultSet.updateAsciiStream(columnLabel, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream, length: Long) {
        val methodCall = "updateBinaryStream($columnLabel, $x, $length)"
        try {
            realResultSet.updateBinaryStream(columnLabel, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        val methodCall = "updateCharacterStream($columnLabel, $reader, $length)"
        try {
            realResultSet.updateCharacterStream(columnLabel, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) {
        val methodCall = "updateBlob($columnIndex, $inputStream, $length)"
        try {
            realResultSet.updateBlob(columnIndex, inputStream, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream, length: Long) {
        val methodCall = "updateBlob($columnLabel, $inputStream, $length)"
        try {
            realResultSet.updateBlob(columnLabel, inputStream, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader, length: Long) {
        val methodCall = "updateClob($columnIndex, $reader, $length)"
        try {
            realResultSet.updateClob(columnIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader, length: Long) {
        val methodCall = "updateClob($columnLabel, $reader, $length)"
        try {
            realResultSet.updateClob(columnLabel, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader, length: Long) {
        val methodCall = "updateNClob($columnIndex, $reader, $length)"
        try {
            realResultSet.updateNClob(columnIndex, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader, length: Long) {
        val methodCall = "updateNClob($columnLabel, $reader, $length)"
        try {
            realResultSet.updateNClob(columnLabel, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, reader: Reader) {
        val methodCall = "updateNCharacterStream($columnIndex, $reader)"
        try {
            realResultSet.updateNCharacterStream(columnIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, reader: Reader) {
        val methodCall = "updateNCharacterStream($columnLabel, $reader)"
        try {
            realResultSet.updateNCharacterStream(columnLabel, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream) {
        val methodCall = "updateAsciiStream($columnIndex, $x)"
        try {
            realResultSet.updateAsciiStream(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream) {
        val methodCall = "updateBinaryStream($columnIndex, $x)"
        try {
            realResultSet.updateBinaryStream(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader) {
        val methodCall = "updateCharacterStream($columnIndex, $x)"
        try {
            realResultSet.updateCharacterStream(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream) {
        val methodCall = "updateAsciiStream($columnLabel, $x)"
        try {
            realResultSet.updateAsciiStream(columnLabel, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream) {
        val methodCall = "updateBinaryStream($columnLabel, $x)"
        try {
            realResultSet.updateBinaryStream(columnLabel, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnLabel: String, reader: Reader) {
        val methodCall = "updateCharacterStream($columnLabel, $reader)"
        try {
            realResultSet.updateCharacterStream(columnLabel, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream) {
        val methodCall = "updateBlob($columnIndex, $inputStream)"
        try {
            realResultSet.updateBlob(columnIndex, inputStream)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream) {
        val methodCall = "updateBlob($columnLabel, $inputStream)"
        try {
            realResultSet.updateBlob(columnLabel, inputStream)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader) {
        val methodCall = "updateClob($columnIndex, $reader)"
        try {
            realResultSet.updateClob(columnIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader) {
        val methodCall = "updateClob($columnLabel, $reader)"
        try {
            realResultSet.updateClob(columnLabel, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader) {
        val methodCall = "updateNClob($columnIndex, $reader)"
        try {
            realResultSet.updateNClob(columnIndex, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader) {
        val methodCall = "updateNClob($columnLabel, $reader)"
        try {
            realResultSet.updateNClob(columnLabel, reader)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun isBeforeFirst(): Boolean {
        val methodCall = "isBeforeFirst()"
        try {
            return reportReturn(methodCall, realResultSet.isBeforeFirst)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getShort(columnIndex: Int): Short {
        val methodCall = "getShort($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getShort(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getShort(columnName: String): Short {
        val methodCall = "getShort($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getShort(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getInt(columnIndex: Int): Int {
        val methodCall = "getInt($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getInt(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getInt(columnName: String): Int {
        val methodCall = "getInt($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getInt(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun close() {
        val methodCall = "close()"
        try {
            realResultSet.close()
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
            return reportReturn(methodCall, realResultSet.metaData) as ResultSetMetaData
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getType(): Int {
        val methodCall = "getType()"
        try {
            return reportReturn(methodCall, realResultSet.type)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDouble(columnIndex: Int): Double {
        val methodCall = "getDouble($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getDouble(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDouble(columnName: String): Double {
        val methodCall = "getDouble($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getDouble(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun deleteRow() {
        val methodCall = "deleteRow()"
        try {
            realResultSet.deleteRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getConcurrency(): Int {
        val methodCall = "getConcurrency()"
        try {
            return reportReturn(methodCall, realResultSet.concurrency)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun rowUpdated(): Boolean {
        val methodCall = "rowUpdated()"
        try {
            return reportReturn(methodCall, realResultSet.rowUpdated())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int): Date {
        val methodCall = "getDate($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getDate(columnIndex)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(columnName: String): Date {
        val methodCall = "getDate($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getDate(columnName)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int, cal: Calendar): Date {
        val methodCall = "getDate($columnIndex, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getDate(columnIndex, cal)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getDate(columnName: String, cal: Calendar): Date {
        val methodCall = "getDate($columnName, $cal)"
        try {
            return reportReturn(methodCall, realResultSet.getDate(columnName, cal)) as Date
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun last(): Boolean {
        val methodCall = "last()"
        try {
            return reportReturn(methodCall, realResultSet.last())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun rowInserted(): Boolean {
        val methodCall = "rowInserted()"
        try {
            return reportReturn(methodCall, realResultSet.rowInserted())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun rowDeleted(): Boolean {
        val methodCall = "rowDeleted()"
        try {
            return reportReturn(methodCall, realResultSet.rowDeleted())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateNull(columnIndex: Int) {
        val methodCall = "updateNull($columnIndex)"
        try {
            realResultSet.updateNull(columnIndex)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateNull(columnName: String) {
        val methodCall = "updateNull($columnName)"
        try {
            realResultSet.updateNull(columnName)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateShort(columnIndex: Int, x: Short) {
        val methodCall = "updateShort($columnIndex, $x)"
        try {
            realResultSet.updateShort(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateShort(columnName: String, x: Short) {
        val methodCall = "updateShort($columnName, $x)"
        try {
            realResultSet.updateShort(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBoolean(columnIndex: Int, x: Boolean) {
        val methodCall = "updateBoolean($columnIndex, $x)"
        try {
            realResultSet.updateBoolean(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBoolean(columnName: String, x: Boolean) {
        val methodCall = "updateBoolean($columnName, $x)"
        try {
            realResultSet.updateBoolean(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateByte(columnIndex: Int, x: Byte) {
        val methodCall = "updateByte($columnIndex, $x)"
        try {
            realResultSet.updateByte(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateByte(columnName: String, x: Byte) {
        val methodCall = "updateByte($columnName, $x)"
        try {
            realResultSet.updateByte(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateInt(columnIndex: Int, x: Int) {
        val methodCall = "updateInt($columnIndex, $x)"
        try {
            realResultSet.updateInt(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateInt(columnName: String, x: Int) {
        val methodCall = "updateInt($columnName, $x)"
        try {
            realResultSet.updateInt(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getObject(columnIndex: Int): Any {
        val methodCall = "getObject($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getObject(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getObject(columnName: String): Any {
        val methodCall = "getObject($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getObject(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }


    @Throws(SQLException::class)
    override fun getObject(columnLabel: String?, map: MutableMap<String, Class<*>>?): Any {
        val methodCall = "getObject($columnLabel, $map)"
        try {
            return reportReturn(methodCall, realResultSet.getObject(columnLabel, map))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun next(): Boolean {
        val methodCall = "next()"
        try {
            return reportReturn(methodCall, realResultSet.next())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateLong(columnIndex: Int, x: Long) {
        val methodCall = "updateLong($columnIndex, $x)"
        try {
            realResultSet.updateLong(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateLong(columnName: String, x: Long) {
        val methodCall = "updateLong($columnName, $x)"
        try {
            realResultSet.updateLong(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateFloat(columnIndex: Int, x: Float) {
        val methodCall = "updateFloat($columnIndex, $x)"
        try {
            realResultSet.updateFloat(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateFloat(columnName: String, x: Float) {
        val methodCall = "updateFloat($columnName, $x)"
        try {
            realResultSet.updateFloat(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateDouble(columnIndex: Int, x: Double) {
        val methodCall = "updateDouble($columnIndex, $x)"
        try {
            realResultSet.updateDouble(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateDouble(columnName: String, x: Double) {
        val methodCall = "updateDouble($columnName, $x)"
        try {
            realResultSet.updateDouble(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getStatement(): Statement {
        val methodCall = "getStatement()"
        return reportReturn(methodCall, parent) as Statement
    }

    @Throws(SQLException::class)
    override fun getObject(columnIndex: Int, map: Map<String?, Class<*>?>): Any {
        val methodCall = "getObject($columnIndex, $map)"
        try {
            return reportReturn(methodCall, realResultSet.getObject(columnIndex, map))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateString(columnIndex: Int, x: String) {
        val methodCall = "updateString($columnIndex, $x)"
        try {
            realResultSet.updateString(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateString(columnName: String, x: String) {
        val methodCall = "updateString($columnName, $x)"
        try {
            realResultSet.updateString(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getAsciiStream(columnIndex: Int): InputStream {
        val methodCall = "getAsciiStream($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getAsciiStream(columnIndex)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getAsciiStream(columnName: String): InputStream {
        val methodCall = "getAsciiStream($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getAsciiStream(columnName)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal) {
        val methodCall = "updateBigDecimal($columnIndex, $x)"
        try {
            realResultSet.updateBigDecimal(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getURL(columnIndex: Int): URL {
        val methodCall = "getURL($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getURL(columnIndex)) as URL
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBigDecimal(columnName: String, x: BigDecimal) {
        val methodCall = "updateBigDecimal($columnName, $x)"
        try {
            realResultSet.updateBigDecimal(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getURL(columnName: String): URL {
        val methodCall = "getURL($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getURL(columnName)) as URL
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBytes(columnIndex: Int, x: ByteArray) {
        // todo: dump array?
        val methodCall = "updateBytes($columnIndex, $x)"
        try {
            realResultSet.updateBytes(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBytes(columnName: String, x: ByteArray) {
        // todo: dump array?
        val methodCall = "updateBytes($columnName, $x)"
        try {
            realResultSet.updateBytes(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun getUnicodeStream(columnIndex: Int): InputStream {
        val methodCall = "getUnicodeStream($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getUnicodeStream(columnIndex)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun getUnicodeStream(columnName: String): InputStream {
        val methodCall = "getUnicodeStream($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getUnicodeStream(columnName)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateDate(columnIndex: Int, x: Date) {
        val methodCall = "updateDate($columnIndex, $x)"
        try {
            realResultSet.updateDate(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateDate(columnName: String, x: Date) {
        val methodCall = "updateDate($columnName, $x)"
        try {
            realResultSet.updateDate(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getFetchSize(): Int {
        val methodCall = "getFetchSize()"
        try {
            return reportReturn(methodCall, realResultSet.fetchSize)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning {
        val methodCall = "getWarnings()"
        try {
            return reportReturn(methodCall, realResultSet.warnings) as SQLWarning
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBinaryStream(columnIndex: Int): InputStream {
        val methodCall = "getBinaryStream($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getBinaryStream(columnIndex)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBinaryStream(columnName: String): InputStream {
        val methodCall = "getBinaryStream($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getBinaryStream(columnName)) as InputStream
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun clearWarnings() {
        val methodCall = "clearWarnings()"
        try {
            realResultSet.clearWarnings()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateTimestamp(columnIndex: Int, x: Timestamp) {
        val methodCall = "updateTimestamp($columnIndex, $x)"
        try {
            realResultSet.updateTimestamp(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateTimestamp(columnName: String, x: Timestamp) {
        val methodCall = "updateTimestamp($columnName, $x)"
        try {
            realResultSet.updateTimestamp(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun first(): Boolean {
        val methodCall = "first()"
        try {
            return reportReturn(methodCall, realResultSet.first())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getCursorName(): String {
        val methodCall = "getCursorName()"
        try {
            return reportReturn(methodCall, realResultSet.cursorName) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun findColumn(columnName: String): Int {
        val methodCall = "findColumn($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.findColumn(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun wasNull(): Boolean {
        val methodCall = "wasNull()"
        try {
            return reportReturn(methodCall, realResultSet.wasNull())
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Int) {
        val methodCall = "updateBinaryStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateBinaryStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnName: String, x: InputStream, length: Int) {
        val methodCall = "updateBinaryStream($columnName, $x, $length)"
        try {
            realResultSet.updateBinaryStream(columnName, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getString(columnIndex: Int): String {
        val methodCall = "getString($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getString(columnIndex)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getString(columnName: String): String {
        val methodCall = "getString($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getString(columnName)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(columnIndex: Int): Reader {
        val methodCall = "getCharacterStream($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getCharacterStream(columnIndex)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(columnName: String): Reader {
        val methodCall = "getCharacterStream($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getCharacterStream(columnName)) as Reader
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setFetchDirection(direction: Int) {
        val methodCall = "setFetchDirection($direction)"
        try {
            realResultSet.fetchDirection = direction
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Int) {
        val methodCall = "updateCharacterStream($columnIndex, $x, $length)"
        try {
            realResultSet.updateCharacterStream(columnIndex, x, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnName: String, reader: Reader, length: Int) {
        val methodCall = "updateCharacterStream($columnName, $reader, $length)"
        try {
            realResultSet.updateCharacterStream(columnName, reader, length)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getByte(columnIndex: Int): Byte {
        val methodCall = "getByte($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getByte(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getByte(columnName: String): Byte {
        val methodCall = "getByte($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getByte(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateTime(columnIndex: Int, x: Time) {
        val methodCall = "updateTime($columnIndex, $x)"
        try {
            realResultSet.updateTime(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateTime(columnName: String, x: Time) {
        val methodCall = "updateTime($columnName, $x)"
        try {
            realResultSet.updateTime(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getBytes(columnIndex: Int): ByteArray {
        val methodCall = "getBytes($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getBytes(columnIndex)) as ByteArray
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBytes(columnName: String): ByteArray {
        val methodCall = "getBytes($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getBytes(columnName)) as ByteArray
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isAfterLast(): Boolean {
        val methodCall = "isAfterLast()"
        try {
            return reportReturn(methodCall, realResultSet.isAfterLast)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun updateObject(columnIndex: Int, x: Any, scale: Int) {
        val methodCall = "updateObject($columnIndex, $x, $scale)"
        try {
            realResultSet.updateObject(columnIndex, x, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateObject(columnIndex: Int, x: Any) {
        val methodCall = "updateObject($columnIndex, $x)"
        try {
            realResultSet.updateObject(columnIndex, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateObject(columnName: String, x: Any, scale: Int) {
        val methodCall = "updateObject($columnName, $x, $scale)"
        try {
            realResultSet.updateObject(columnName, x, scale)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateObject(columnName: String, x: Any) {
        val methodCall = "updateObject($columnName, $x)"
        try {
            realResultSet.updateObject(columnName, x)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getFetchDirection(): Int {
        val methodCall = "getFetchDirection()"
        try {
            return reportReturn(methodCall, realResultSet.fetchDirection)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getLong(columnIndex: Int): Long {
        val methodCall = "getLong($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getLong(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getLong(columnName: String): Long {
        val methodCall = "getLong($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getLong(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isFirst(): Boolean {
        val methodCall = "isFirst()"
        try {
            return reportReturn(methodCall, realResultSet.isFirst)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun insertRow() {
        val methodCall = "insertRow()"
        try {
            realResultSet.insertRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getFloat(columnIndex: Int): Float {
        val methodCall = "getFloat($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getFloat(columnIndex))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getFloat(columnName: String): Float {
        val methodCall = "getFloat($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getFloat(columnName))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isLast(): Boolean {
        val methodCall = "isLast()"
        try {
            return reportReturn(methodCall, realResultSet.isLast)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setFetchSize(rows: Int) {
        val methodCall = "setFetchSize($rows)"
        try {
            realResultSet.fetchSize = rows
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun updateRow() {
        val methodCall = "updateRow()"
        try {
            realResultSet.updateRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun beforeFirst() {
        val methodCall = "beforeFirst()"
        try {
            realResultSet.beforeFirst()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        val methodCall = "getBigDecimal($columnIndex, $scale)"
        try {
            return reportReturn(methodCall, realResultSet.getBigDecimal(columnIndex, scale)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }


    @Deprecated("")
    @Throws(SQLException::class)
    override fun getBigDecimal(columnName: String, scale: Int): BigDecimal {
        val methodCall = "getBigDecimal($columnName, $scale)"
        try {
            return reportReturn(methodCall, realResultSet.getBigDecimal(columnName, scale)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        val methodCall = "getBigDecimal($columnIndex)"
        try {
            return reportReturn(methodCall, realResultSet.getBigDecimal(columnIndex)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnName: String): BigDecimal {
        val methodCall = "getBigDecimal($columnName)"
        try {
            return reportReturn(methodCall, realResultSet.getBigDecimal(columnName)) as BigDecimal
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun afterLast() {
        val methodCall = "afterLast()"
        try {
            realResultSet.afterLast()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun refreshRow() {
        val methodCall = "refreshRow()"
        try {
            realResultSet.refreshRow()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T? {
        val name = iface.name
        val methodCall = "unwrap($name)"
        try {
            val result: T?
            result = if (iface == ResultSetSpy::class.java || iface == Spy::class.java) {
                this as T
            } else {
                realResultSet.unwrap(iface)
            }
            reportReturn(methodCall, (result as Any?)!!)
            return result
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        val methodCall = "isWrapperFor(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            return reportReturn(
                methodCall, (iface != null && (iface == ResultSet::class.java || iface == Spy::class.java)) ||
                        realResultSet.isWrapperFor(iface)
            )
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> getObject(columnIndex: Int, type: Class<T>): T {
        val typeName = type.name
        val methodCall = "getObject($columnIndex, $typeName)"
        try {
            val result = realResultSet.getObject(columnIndex, type)
            reportReturn(methodCall, result as Any)
            return result
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> getObject(columnLabel: String, type: Class<T>?): T {
        val typeName = if (type == null) "null" else type.name
        val methodCall = "getObject($columnLabel, $typeName)"
        try {
            val result = realResultSet.getObject(columnLabel, type)
            reportReturn(methodCall, result as Any)
            return result
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    override val classType = "ResultSet"

    companion object {
        /**
         * Description for ResultSet class type.
         */
        val CLASS_TYPE = "ResultSet"
        private val log = SpyLogFactory.spyLogDelegator
    }
}