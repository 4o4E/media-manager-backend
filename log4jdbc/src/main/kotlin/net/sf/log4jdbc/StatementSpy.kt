package net.sf.log4jdbc

import java.sql.*

/**
 * Wraps a Statement and reports method calls, returns and exceptions.
 *
 *
 * jdbc 4 version
 *
 * @author Arthur Blake
 */
open class StatementSpy(connectionSpy: ConnectionSpy?, realStatement: Statement?) : Statement, Spy {
    protected val log = SpyLogFactory.spyLogDelegator

    /**
     * The Connection that created this Statement.
     */
    protected var connectionSpy: ConnectionSpy

    /**
     * The real statement that this StatementSpy wraps.
     * -- GETTER --
     * Get the real Statement that this StatementSpy wraps.
     *
     */
    protected var realStatement: Statement

    override val classType: String
        get() = "Statement"

    override val connectionNumber: Int?
        get() = connectionSpy.connectionNumber

    /**
     * Report an exception to be logged which includes timing data on a sql failure.
     *
     * @param methodCall description of method call and arguments passed to it that generated the exception.
     * @param exception  exception that was generated
     * @param sql        SQL associated with the call.
     * @param execTime   amount of time that the jdbc driver was chugging on the SQL before it threw an exception.
     */
    protected fun reportException(methodCall: String, exception: SQLException?, sql: String?, execTime: Long) {
        log.exceptionOccured(this, methodCall, exception, sql, execTime)
    }

    /**
     * Report an exception to be logged.
     *
     * @param methodCall description of method call and arguments passed to it that generated the exception.
     * @param exception  exception that was generated
     * @param sql        SQL associated with the call.
     */
    protected fun reportException(methodCall: String, exception: SQLException?, sql: String?) {
        log.exceptionOccured(this, methodCall, exception, sql, -1L)
    }

    /**
     * Report an exception to be logged.
     *
     * @param methodCall description of method call and arguments passed to it that generated the exception.
     * @param exception  exception that was generated
     */
    protected fun reportException(methodCall: String, exception: SQLException?) {
        log.exceptionOccured(this, methodCall, exception, null, -1L)
    }

    /**
     * Report (for logging) that a method returned.  All the other reportReturn methods are conveniance methods that call this method.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param msg        description of what the return value that was returned.  may be an empty String for void return types.
     */
    protected open fun reportAllReturns(methodCall: String, msg: String) {
        log.methodReturned(this, methodCall, msg)
    }

    /**
     * Conveniance method to report (for logging) that a method returned a boolean value.
     *
     * @param methodCall description of method call and arguments passed to it that returned.
     * @param value      boolean return value.
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
     * @param value      byte return value.
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
     * @param value      int return value.
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
     * @param value      double return value.
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
     * @param value      short return value.
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
     * @param value      long return value.
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
     * @param value      float return value.
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
     * @param value      return Object.
     * @return the return Object as passed in.
     */
    protected fun reportReturn(methodCall: String, value: Any?): Any? {
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

    /**
     * Report SQL for logging with a warning that it was generated from a statement.
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected fun reportStatementSql(sql: String, methodCall: String) {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _reportSql(
            (if (DriverSpy.StatementUsageWarn) StatementSqlWarning else "") +
                    sql, methodCall
        )
    }

    /**
     * Report SQL for logging with a warning that it was generated from a statement.
     *
     * @param execTime   execution time in msec.
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected fun reportStatementSqlTiming(execTime: Long, sql: String, methodCall: String) {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _reportSqlTiming(
            execTime, (if (DriverSpy.StatementUsageWarn) StatementSqlWarning else "") +
                    sql, methodCall
        )
    }

    /**
     * Report SQL for logging.
     *
     * @param execTime   execution time in msec.
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected fun reportSqlTiming(execTime: Long, sql: String, methodCall: String) {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _reportSqlTiming(execTime, sql, methodCall)
    }

    /**
     * Report SQL for logging.
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected fun reportSql(sql: String, methodCall: String) {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _reportSql(sql, methodCall)
    }

    private fun _reportSql(sql: String, methodCall: String) {
        log.sqlOccured(this, methodCall, sql)
    }

    private fun _reportSqlTiming(execTime: Long, sql: String, methodCall: String) {
        log.sqlTimingOccured(this, execTime, methodCall, sql)
    }

    // implementation of interface methods
    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning? {
        val methodCall = "getWarnings()"
        try {
            return reportReturn(methodCall, realStatement.warnings) as SQLWarning?
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun executeUpdate(sql: String, columnNames: Array<String>): Int {
        val methodCall = "executeUpdate(" + sql + ", " + columnNames.contentToString() + ")"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.executeUpdate(sql, columnNames)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun execute(sql: String, columnNames: Array<String>): Boolean {
        val methodCall = "execute(" + sql + ", " + columnNames.contentToString() + ")"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.execute(sql, columnNames)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setMaxRows(max: Int) {
        val methodCall = "setMaxRows($max)"
        try {
            realStatement.maxRows = max
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getMoreResults(): Boolean {
        val methodCall = "getMoreResults()"

        try {
            return reportReturn(methodCall, realStatement.moreResults)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun clearWarnings() {
        val methodCall = "clearWarnings()"
        try {
            realStatement.clearWarnings()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    /**
     * Tracking of current batch (see addBatch, clearBatch and executeBatch)
     * //todo: should access to this List be synchronized?
     */
    protected var currentBatch: MutableList<String> = ArrayList()

    init {
        requireNotNull(realStatement) { "Must pass in a non null real Statement" }
        requireNotNull(connectionSpy) { "Must pass in a non null ConnectionSpy" }
        this.realStatement = realStatement
        this.connectionSpy = connectionSpy

        when (realStatement) {
            is CallableStatement -> reportReturn("new CallableStatement")
            is PreparedStatement -> reportReturn("new PreparedStatement")
            else -> reportReturn("new Statement")
        }
    }

    @Throws(SQLException::class)
    override fun addBatch(sql: String) {
        val methodCall = "addBatch($sql)"

        currentBatch.add(StatementSqlWarning + sql)
        try {
            realStatement.addBatch(sql)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getResultSetType(): Int {
        val methodCall = "getResultSetType()"
        try {
            return reportReturn(methodCall, realStatement.resultSetType)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun clearBatch() {
        val methodCall = "clearBatch()"
        try {
            realStatement.clearBatch()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        currentBatch.clear()
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setFetchDirection(direction: Int) {
        val methodCall = "setFetchDirection($direction)"
        try {
            realStatement.fetchDirection = direction
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun executeBatch(): IntArray {
        val methodCall = "executeBatch()"

        val j = currentBatch.size
        val batchReport = StringBuilder("batching $j statements:")

        val fieldSize = ("" + j).length

        var sql: String
        var i = 0
        while (i < j) {
            sql = currentBatch[i]
            batchReport.append("\n")
            batchReport.append(Utilities.rightJustify(fieldSize, "" + (++i)))
            batchReport.append(":  ")
            batchReport.append(sql)
        }

        sql = batchReport.toString()
        reportSql(sql, methodCall)
        val tstart = System.currentTimeMillis()

        val updateResults: IntArray
        try {
            updateResults = realStatement.executeBatch()
            reportSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
        currentBatch.clear()
        return reportReturn(methodCall, updateResults) as IntArray
    }

    @Throws(SQLException::class)
    override fun setFetchSize(rows: Int) {
        val methodCall = "setFetchSize($rows)"
        try {
            realStatement.fetchSize = rows
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getQueryTimeout(): Int {
        val methodCall = "getQueryTimeout()"
        try {
            return reportReturn(methodCall, realStatement.queryTimeout)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    override fun getConnection(): Connection {
        val methodCall = "getConnection()"
        return reportReturn(methodCall, connectionSpy) as Connection
    }

    @Throws(SQLException::class)
    override fun getGeneratedKeys(): ResultSet {
        val methodCall = "getGeneratedKeys()"
        try {
            val r: ResultSet? = realStatement.generatedKeys
            return if (r == null) {
                reportReturn(methodCall, r) as ResultSet
            } else {
                reportReturn(methodCall, ResultSetSpy(this, r)) as ResultSet
            }
        } catch (s: SQLException) {
            if (!DriverSpy.SuppressGetGeneratedKeysException) {
                reportException(methodCall, s)
            }
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setEscapeProcessing(enable: Boolean) {
        val methodCall = "setEscapeProcessing($enable)"
        try {
            realStatement.setEscapeProcessing(enable)
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
            return reportReturn(methodCall, realStatement.fetchDirection)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setQueryTimeout(seconds: Int) {
        val methodCall = "setQueryTimeout($seconds)"
        try {
            realStatement.queryTimeout = seconds
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getMoreResults(current: Int): Boolean {
        val methodCall = "getMoreResults($current)"

        try {
            return reportReturn(methodCall, realStatement.getMoreResults(current))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun executeQuery(sql: String): ResultSet {
        val methodCall = "executeQuery($sql)"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result: ResultSet = realStatement.executeQuery(sql)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            val r = ResultSetSpy(this, result)
            return reportReturn(methodCall, r) as ResultSet
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getMaxFieldSize(): Int {
        val methodCall = "getMaxFieldSize()"
        try {
            return reportReturn(methodCall, realStatement.maxFieldSize)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun executeUpdate(sql: String): Int {
        val methodCall = "executeUpdate($sql)"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.executeUpdate(sql)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun cancel() {
        val methodCall = "cancel()"
        try {
            realStatement.cancel()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setCursorName(name: String) {
        val methodCall = "setCursorName($name)"
        try {
            realStatement.setCursorName(name)
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
            return reportReturn(methodCall, realStatement.fetchSize)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getResultSetConcurrency(): Int {
        val methodCall = "getResultSetConcurrency()"
        try {
            return reportReturn(methodCall, realStatement.resultSetConcurrency)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getResultSetHoldability(): Int {
        val methodCall = "getResultSetHoldability()"
        try {
            return reportReturn(methodCall, realStatement.resultSetHoldability)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isClosed(): Boolean {
        val methodCall = "isClosed()"
        try {
            return reportReturn(methodCall, realStatement.isClosed)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setPoolable(poolable: Boolean) {
        val methodCall = "setPoolable($poolable)"
        try {
            realStatement.isPoolable = poolable
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun isPoolable(): Boolean {
        val methodCall = "isPoolable()"
        try {
            return reportReturn(methodCall, realStatement.isPoolable)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setMaxFieldSize(max: Int) {
        val methodCall = "setMaxFieldSize($max)"
        try {
            realStatement.maxFieldSize = max
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun execute(sql: String): Boolean {
        val methodCall = "execute($sql)"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.execute(sql)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun executeUpdate(sql: String, autoGeneratedKeys: Int): Int {
        val methodCall = "executeUpdate($sql, $autoGeneratedKeys)"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.executeUpdate(sql, autoGeneratedKeys)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun execute(sql: String, autoGeneratedKeys: Int): Boolean {
        val methodCall = "execute($sql, $autoGeneratedKeys)"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.execute(sql, autoGeneratedKeys)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun executeUpdate(sql: String, columnIndexes: IntArray): Int {
        val methodCall = "executeUpdate(" + sql + ", " + columnIndexes.contentToString() + ")"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.executeUpdate(sql, columnIndexes)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun execute(sql: String, columnIndexes: IntArray): Boolean {
        val methodCall = "execute(" + sql + ", " + columnIndexes.contentToString() + ")"
        reportStatementSql(sql, methodCall)
        val tstart = System.currentTimeMillis()
        try {
            val result = realStatement.execute(sql, columnIndexes)
            reportStatementSqlTiming(System.currentTimeMillis() - tstart, sql, methodCall)
            return reportReturn(methodCall, result)
        } catch (s: SQLException) {
            reportException(methodCall, s, sql, System.currentTimeMillis() - tstart)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getResultSet(): ResultSet {
        val methodCall = "getResultSet()"
        try {
            val r: ResultSet? = realStatement.resultSet
            return if (r == null) {
                reportReturn(methodCall, null) as ResultSet
            } else {
                reportReturn(methodCall, ResultSetSpy(this, r)) as ResultSet
            }
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getMaxRows(): Int {
        val methodCall = "getMaxRows()"
        try {
            return reportReturn(methodCall, realStatement.maxRows)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun close() {
        val methodCall = "close()"
        try {
            realStatement.close()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun closeOnCompletion() {
        val methodCall = "closeOnCompletion()"
        try {
            realStatement.closeOnCompletion()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun isCloseOnCompletion(): Boolean {
        val result: Boolean
        val methodCall = "isCloseOnCompletion()"
        try {
            result = realStatement.isCloseOnCompletion
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
        return result
    }

    @Throws(SQLException::class)
    override fun getUpdateCount(): Int {
        val methodCall = "getUpdateCount()"
        try {
            return reportReturn(methodCall, realStatement.updateCount)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>?): T? {
        val methodCall = "unwrap(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            // todo: double check this logic
            @Suppress("UNCHECKED_CAST")
            return reportReturn(
                methodCall, (if (iface == Connection::class.java || iface == Spy::class.java
                ) this
                else realStatement.unwrap(iface))!!
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
            return reportReturn(
                methodCall, ((iface == Statement::class.java || iface == Spy::class.java)) ||
                        realStatement.isWrapperFor(iface)
            )
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    companion object {
        /**
         * Running one-off statement sql is generally inefficient and a bad idea for various reasons,
         * so give a warning when this is done.
         */
        private const val StatementSqlWarning = "{WARNING: Statement used to run SQL} "
    }
}
