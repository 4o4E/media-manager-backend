package net.sf.log4jdbc

import java.sql.*
import java.util.*
import java.util.concurrent.Executor

/**
 * Wraps a JDBC Connection and reports method calls, returns and exceptions.
 *
 * This version is for jdbc 4.
 *
 * @author Arthur Blake
 */
class ConnectionSpy @JvmOverloads constructor(
    val realConnection: Connection,
    val rdbmsSpecifics: RdbmsSpecifics = DriverSpy.defaultRdbmsSpecifics
) : Connection, Spy {
    private val log = SpyLogFactory.spyLogDelegator

    override var connectionNumber: Int? = null

    init {
        synchronized(connectionTracker) {
            connectionNumber = ++lastConnectionNumber
            connectionTracker.put(connectionNumber, this)
        }
        log.connectionOpened(this)
        reportReturn("new Connection")
    }

    override val classType = "Connection"

    protected fun reportException(methodCall: String, exception: SQLException?, sql: String?) {
        log.exceptionOccured(this, methodCall, exception, sql, -1L)
    }

    protected fun reportException(methodCall: String, exception: SQLException?) {
        log.exceptionOccured(this, methodCall, exception, null, -1L)
    }

    protected fun reportAllReturns(methodCall: String, returnValue: String) {
        log.methodReturned(this, methodCall, returnValue)
    }

    private fun reportReturn(methodCall: String, value: Boolean): Boolean {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    private fun reportReturn(methodCall: String, value: Int): Int {
        reportAllReturns(methodCall, "" + value)
        return value
    }

    private fun <T> reportReturn(methodCall: String, value: T): T {
        reportAllReturns(methodCall, value.toString())
        return value
    }

    private fun reportReturn(methodCall: String) {
        reportAllReturns(methodCall, "")
    }

    // forwarding methods
    @Throws(SQLException::class)
    override fun isClosed(): Boolean {
        val methodCall = "isClosed()"
        try {
            return reportReturn(methodCall, (realConnection.isClosed))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning {
        val methodCall = "getWarnings()"
        try {
            return reportReturn(methodCall, realConnection.warnings) as SQLWarning
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setSavepoint(): Savepoint {
        val methodCall = "setSavepoint()"
        try {
            return reportReturn(methodCall, realConnection.setSavepoint()) as Savepoint
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun releaseSavepoint(savepoint: Savepoint) {
        val methodCall = "releaseSavepoint($savepoint)"
        try {
            realConnection.releaseSavepoint(savepoint)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun rollback(savepoint: Savepoint) {
        val methodCall = "rollback($savepoint)"
        try {
            realConnection.rollback(savepoint)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getMetaData(): DatabaseMetaData {
        val methodCall = "getMetaData()"
        try {
            return reportReturn(methodCall, realConnection.metaData) as DatabaseMetaData
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun clearWarnings() {
        val methodCall = "clearWarnings()"
        try {
            realConnection.clearWarnings()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun createStatement(): Statement {
        val methodCall = "createStatement()"
        try {
            val statement = realConnection.createStatement()
            return reportReturn(methodCall, StatementSpy(this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement {
        val methodCall = "createStatement($resultSetType, $resultSetConcurrency)"
        try {
            val statement = realConnection.createStatement(resultSetType, resultSetConcurrency)
            return reportReturn(methodCall, StatementSpy(this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement {
        val methodCall = "createStatement($resultSetType, $resultSetConcurrency, $resultSetHoldability)"
        try {
            val statement = realConnection.createStatement(
                resultSetType, resultSetConcurrency,
                resultSetHoldability
            )
            return reportReturn(methodCall, StatementSpy(this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setReadOnly(readOnly: Boolean) {
        val methodCall = "setReadOnly($readOnly)"
        try {
            realConnection.isReadOnly = readOnly
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String): PreparedStatement {
        val methodCall = "prepareStatement($sql)"
        try {
            val statement = realConnection.prepareStatement(sql)
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, autoGeneratedKeys: Int): PreparedStatement {
        val methodCall = "prepareStatement($sql, $autoGeneratedKeys)"
        try {
            val statement = realConnection.prepareStatement(sql, autoGeneratedKeys)
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement {
        val methodCall = "prepareStatement($sql, $resultSetType, $resultSetConcurrency)"
        try {
            val statement = realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency)
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareStatement(
        sql: String, resultSetType: Int, resultSetConcurrency: Int,
        resultSetHoldability: Int
    ): PreparedStatement {
        val methodCall = "prepareStatement($sql, $resultSetType, $resultSetConcurrency, $resultSetHoldability)"
        try {
            val statement = realConnection.prepareStatement(
                sql, resultSetType, resultSetConcurrency,
                resultSetHoldability
            )
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, columnIndexes: IntArray): PreparedStatement {
        // todo: dump the array here?
        val methodCall = "prepareStatement($sql, $columnIndexes)"
        try {
            val statement = realConnection.prepareStatement(sql, columnIndexes)
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setSavepoint(name: String): Savepoint {
        val methodCall = "setSavepoint($name)"
        try {
            return reportReturn(methodCall, realConnection.setSavepoint(name)) as Savepoint
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, columnNames: Array<String>): PreparedStatement {
        // todo: dump the array here?
        val methodCall = "prepareStatement($sql, $columnNames)"
        try {
            val statement = realConnection.prepareStatement(sql, columnNames)
            return reportReturn(methodCall, PreparedStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createClob(): Clob {
        val methodCall = "createClob()"
        try {
            return reportReturn(methodCall, realConnection.createClob()) as Clob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createBlob(): Blob {
        val methodCall = "createBlob()"
        try {
            return reportReturn(methodCall, realConnection.createBlob()) as Blob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createNClob(): NClob {
        val methodCall = "createNClob()"
        try {
            return reportReturn(methodCall, realConnection.createNClob()) as NClob
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createSQLXML(): SQLXML {
        val methodCall = "createSQLXML()"
        try {
            return reportReturn(methodCall, realConnection.createSQLXML()) as SQLXML
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isValid(timeout: Int): Boolean {
        val methodCall = "isValid($timeout)"
        try {
            return reportReturn(methodCall, realConnection.isValid(timeout))
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLClientInfoException::class)
    override fun setClientInfo(name: String, value: String) {
        val methodCall = "setClientInfo($name, $value)"
        try {
            realConnection.setClientInfo(name, value)
        } catch (s: SQLClientInfoException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLClientInfoException::class)
    override fun setClientInfo(properties: Properties) {
        // todo: dump properties?
        val methodCall = "setClientInfo($properties)"
        try {
            realConnection.clientInfo = properties
        } catch (s: SQLClientInfoException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getClientInfo(name: String): String {
        val methodCall = "getClientInfo($name)"
        try {
            return reportReturn(methodCall, realConnection.getClientInfo(name)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getClientInfo(): Properties {
        val methodCall = "getClientInfo()"
        try {
            return reportReturn(methodCall, realConnection.clientInfo) as Properties
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createArrayOf(typeName: String, elements: Array<Any>): java.sql.Array {
        // todo: dump elements?
        val methodCall = "createArrayOf($typeName, $elements)"
        try {
            return reportReturn(methodCall, realConnection.createArrayOf(typeName, elements)) as java.sql.Array
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun createStruct(typeName: String, attributes: Array<Any>): Struct {
        // todo: dump attributes?
        val methodCall = "createStruct($typeName, $attributes)"
        try {
            return reportReturn(methodCall, realConnection.createStruct(typeName, attributes)) as Struct
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun isReadOnly(): Boolean {
        val methodCall = "isReadOnly()"
        try {
            return reportReturn(methodCall, realConnection.isReadOnly)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setHoldability(holdability: Int) {
        val methodCall = "setHoldability($holdability)"
        try {
            realConnection.holdability = holdability
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun prepareCall(sql: String): CallableStatement {
        val methodCall = "prepareCall($sql)"
        try {
            val statement = realConnection.prepareCall(sql)
            return reportReturn(methodCall, CallableStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int): CallableStatement {
        val methodCall = "prepareCall($sql, $resultSetType, $resultSetConcurrency)"
        try {
            val statement = realConnection.prepareCall(sql, resultSetType, resultSetConcurrency)
            return reportReturn(methodCall, CallableStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun prepareCall(
        sql: String, resultSetType: Int, resultSetConcurrency: Int,
        resultSetHoldability: Int
    ): CallableStatement {
        val methodCall = "prepareCall($sql, $resultSetType, $resultSetConcurrency, $resultSetHoldability)"
        try {
            val statement = realConnection.prepareCall(
                sql, resultSetType, resultSetConcurrency,
                resultSetHoldability
            )
            return reportReturn(methodCall, CallableStatementSpy(sql, this, statement))
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setCatalog(catalog: String) {
        val methodCall = "setCatalog($catalog)"
        try {
            realConnection.catalog = catalog
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun nativeSQL(sql: String): String {
        val methodCall = "nativeSQL($sql)"
        try {
            return reportReturn(methodCall, realConnection.nativeSQL(sql)) as String
        } catch (s: SQLException) {
            reportException(methodCall, s, sql)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTypeMap(): Map<String, Class<*>> {
        val methodCall = "getTypeMap()"
        try {
            return reportReturn(methodCall, realConnection.typeMap) as Map<String, Class<*>>
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setAutoCommit(autoCommit: Boolean) {
        val methodCall = "setAutoCommit($autoCommit)"
        try {
            realConnection.autoCommit = autoCommit
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getCatalog(): String {
        val methodCall = "getCatalog()"
        try {
            return reportReturn(methodCall, realConnection.catalog) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setTypeMap(map: Map<String?, Class<*>?>) {
        // todo: dump map??
        val methodCall = "setTypeMap($map)"
        try {
            realConnection.typeMap = map
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setTransactionIsolation(level: Int) {
        val methodCall = "setTransactionIsolation($level)"
        try {
            realConnection.transactionIsolation = level
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getAutoCommit(): Boolean {
        val methodCall = "getAutoCommit()"
        try {
            return reportReturn(methodCall, realConnection.autoCommit)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getHoldability(): Int {
        val methodCall = "getHoldability()"
        try {
            return reportReturn(methodCall, realConnection.holdability)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun getTransactionIsolation(): Int {
        val methodCall = "getTransactionIsolation()"
        try {
            return reportReturn(methodCall, realConnection.transactionIsolation)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun commit() {
        val methodCall = "commit()"
        try {
            realConnection.commit()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun rollback() {
        val methodCall = "rollback()"
        try {
            realConnection.rollback()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun close() {
        val methodCall = "close()"
        try {
            realConnection.close()
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        } finally {
            synchronized(connectionTracker) {
                connectionTracker.remove(connectionNumber)
            }
            log.connectionClosed(this)
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>?): T? {
        val methodCall = "unwrap(" + (if (iface == null) "null" else iface.name) + ")"
        try {
            // todo: double check this logic
            return reportReturn(
                methodCall,
                if ((iface != null && (iface == Connection::class.java || iface == Spy::class.java))) this
                else realConnection.unwrap(iface)
            ) as? T
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
                methodCall, (iface != null && (iface == Connection::class.java || iface == Spy::class.java)) ||
                        realConnection.isWrapperFor(iface)
            )
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun setSchema(schema: String) {
        val methodCall = "setSchema($schema)"
        try {
            realConnection.schema = schema
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getSchema(): String {
        val methodCall = "getSchema()"
        try {
            return reportReturn(methodCall, realConnection.schema) as String
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    @Throws(SQLException::class)
    override fun abort(executor: Executor) {
        val methodCall = "abort(Executor)"
        try {
            realConnection.abort(executor)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun setNetworkTimeout(executor: Executor, milliseconds: Int) {
        val methodCall = "setNetworkTimeout(Executor, $milliseconds)"
        try {
            realConnection.setNetworkTimeout(executor, milliseconds)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
        reportReturn(methodCall)
    }

    @Throws(SQLException::class)
    override fun getNetworkTimeout(): Int {
        val methodCall = "getNetworkTimeout()"
        try {
            return reportReturn(methodCall, realConnection.networkTimeout)
        } catch (s: SQLException) {
            reportException(methodCall, s)
            throw s
        }
    }

    companion object {
        private var lastConnectionNumber = 0

        /**
         * Contains a Mapping of connectionNumber to currently open ConnectionSpy
         * objects.
         */
        private val connectionTracker: MutableMap<Int?, Connection> = HashMap()

        val openConnectionsDump: String
            /**
             * Get a dump of how many connections are open, and which connection numbers
             * are open.
             *
             * @return an open connection dump.
             */
            get() {
                val dump = StringBuffer()
                var size: Int
                var keysArr: Array<Int?>
                synchronized(connectionTracker) {
                    size = connectionTracker.size
                    if (size == 0) {
                        return "open connections:  none"
                    }
                    val keys: Set<Int?> = connectionTracker.keys
                    keysArr = keys.toTypedArray<Int?>()
                }

                Arrays.sort(keysArr)

                dump.append("open connections:  ")
                for (i in keysArr.indices) {
                    dump.append(keysArr[i])
                    dump.append(" ")
                }

                dump.append("(")
                dump.append(size)
                dump.append(")")
                return dump.toString()
            }
    }
}