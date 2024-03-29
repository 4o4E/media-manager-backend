package net.sf.log4jdbc

import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.LineNumberReader
import java.io.StringReader
import java.util.*

/**
 * Delegates JDBC spy logging events to the the Simple Logging Facade for Java
 * (slf4j).
 *
 * @author Arthur Blake
 */
class Slf4jSpyLogDelegator : SpyLogDelegator {
    // logs for sql and jdbc
    /**
     * Logger that shows all JDBC calls on INFO level (exception ResultSet calls)
     */
    private val jdbcLogger = LoggerFactory.getLogger("jdbc.audit")

    /**
     * Logger that shows JDBC calls for ResultSet operations
     */
    private val resultSetLogger = LoggerFactory.getLogger("jdbc.resultset")

    /**
     * Logger that shows only the SQL that is occuring
     */
    private val sqlOnlyLogger = LoggerFactory.getLogger("jdbc.sqlonly")

    /**
     * Logger that shows the SQL timing, post execution
     */
    private val sqlTimingLogger = LoggerFactory.getLogger("jdbc.sqltiming")

    /**
     * Logger that shows connection open and close events as well as current
     * number of open connections.
     */
    private val connectionLogger = LoggerFactory.getLogger("jdbc.connection")

    // admin/setup logging for log4jdbc.
    /**
     * Logger just for debugging things within log4jdbc itself (admin, setup,
     * etc.)
     */
    private val debugLogger = LoggerFactory.getLogger("log4jdbc.debug")

    override val isJdbcLoggingEnabled: Boolean
        /**
         * Determine if any of the 5 log4jdbc spy loggers are turned on (jdbc.audit |
         * jdbc.resultset | jdbc.sqlonly | jdbc.sqltiming | jdbc.connection)
         *
         * @return true if any of the 5 spy jdbc/sql loggers are enabled at debug info
         * or error level.
         */
        get() = jdbcLogger.isErrorEnabled || resultSetLogger.isErrorEnabled ||
                sqlOnlyLogger.isErrorEnabled || sqlTimingLogger.isErrorEnabled ||
                connectionLogger.isErrorEnabled

    /**
     * Called when a jdbc method throws an Exception.
     *
     * @param spy the Spy wrapping the class that threw an Exception.
     * @param methodCall a description of the name and call parameters of the
     * method generated the Exception.
     * @param e the Exception that was thrown.
     * @param sqlString optional sql that occured just before the exception occured.
     * @param execTime optional amount of time that passed before an exception was
     * thrown when sql was being executed. caller should pass -1 if not
     * used
     */
    override fun exceptionOccured(
        spy: Spy, methodCall: String, e: Exception?,
        sqlString: String?, execTime: Long
    ) {
        var sql = sqlString
        val classType = spy.classType
        val spyNo = spy.connectionNumber
        val header = "$spyNo. $classType.$methodCall"
        if (sql == null) {
            jdbcLogger.error(header, e)
            sqlOnlyLogger.error(header, e)
            sqlTimingLogger.error(header, e)
        } else {
            sql = processSql(sql)
            jdbcLogger.error("$header $sql", e)

            // if at debug level, display debug info to error log
            if (sqlOnlyLogger.isDebugEnabled) {
                sqlOnlyLogger.error("$debugInfo$nl$spyNo. $sql", e)
            } else {
                sqlOnlyLogger.error("$header $sql", e)
            }

            // if at debug level, display debug info to error log
            if (sqlTimingLogger.isDebugEnabled) {
                sqlTimingLogger.error("$debugInfo$nl$spyNo. $sql {FAILED after $execTime msec}", e)
            } else {
                sqlTimingLogger.error("$header FAILED! $sql {FAILED after $execTime msec}", e)
            }
        }
    }

    /**
     * Called when a JDBC method from a Connection, Statement, PreparedStatement,
     * CallableStatement or ResultSet returns.
     *
     * @param spy the Spy wrapping the class that called the method that returned.
     * @param methodCall a description of the name and call parameters of the
     * method that returned.
     * @param returnMsg return value converted to a String for integral types, or
     * String representation for Object. Return types this will be null for
     * void return types.
     */
    override fun methodReturned(spy: Spy, methodCall: String, returnMsg: String) {
        val classType = spy.classType
        val logger = if (ResultSetSpy.CLASS_TYPE == classType) resultSetLogger
        else jdbcLogger
        if (logger.isInfoEnabled) {
            val header = spy.connectionNumber.toString() + ". " + classType + "." +
                    methodCall + " returned " + returnMsg
            if (logger.isDebugEnabled) {
                logger.debug("$header $debugInfo")
            } else {
                logger.info(header)
            }
        }
    }

    /**
     * Called when a spied upon object is constructed.
     *
     * @param spy the Spy wrapping the class that called the method that returned.
     * @param constructionInfo information about the object construction
     */
    override fun constructorReturned(spy: Spy?, constructionInfo: String?) {
        // not used in this implementation -- yet
    }

    /**
     * Determine if the given sql should be logged or not based on the various
     * DumpSqlXXXXXX flags.
     *
     * @param sql SQL to test.
     * @return true if the SQL should be logged, false if not.
     */
    private fun shouldSqlBeLogged(sql: String): Boolean {
        var sql = sql
        sql = sql.trim { it <= ' ' }

        if (sql.length < 6) {
            return false
        }
        sql = sql.substring(0, 6).lowercase(Locale.getDefault())
        return (DriverSpy.DumpSqlSelect && "select" == sql) ||
                (DriverSpy.DumpSqlInsert && "insert" == sql) ||
                (DriverSpy.DumpSqlUpdate && "update" == sql) ||
                (DriverSpy.DumpSqlDelete && "delete" == sql) ||
                (DriverSpy.DumpSqlCreate && "create" == sql)
    }

    /**
     * Special call that is called only for JDBC method calls that contain SQL.
     *
     * @param spy the Spy wrapping the class where the SQL occured.
     * @param methodCall a description of the name and call parameters of the
     * method that generated the SQL.
     * @param sql sql that occured.
     */
    override fun sqlOccured(spy: Spy, methodCall: String?, sql: String) {
        if (!DriverSpy.DumpSqlFilteringOn || shouldSqlBeLogged(sql)) {
            if (sqlOnlyLogger.isDebugEnabled) {
                sqlOnlyLogger.debug(
                    debugInfo + nl + spy.connectionNumber +
                            ". " + processSql(sql)
                )
            } else if (sqlOnlyLogger.isInfoEnabled) {
                sqlOnlyLogger.info(processSql(sql))
            }
        }
    }

    /**
     * Break an SQL statement up into multiple lines in an attempt to make it more
     * readable
     *
     * @param sql SQL to break up.
     * @return SQL broken up into multiple lines
     */
    private fun processSql(sql: String): String {
        var sql = sql

        if (DriverSpy.TrimSql) {
            sql = sql.trim { it <= ' ' }
        }

        var output = StringBuilder()

        if (DriverSpy.DumpSqlMaxLineLength <= 0) {
            output.append(sql)
        } else {
            // insert line breaks into sql to make it more readable
            val st = StringTokenizer(sql)
            var token: String
            var linelength = 0

            while (st.hasMoreElements()) {
                token = st.nextElement() as String

                output.append(token)
                linelength += token.length
                output.append(" ")
                linelength++
                if (linelength > DriverSpy.DumpSqlMaxLineLength) {
                    output.append("\n")
                    linelength = 0
                }
            }
        }

        if (DriverSpy.DumpSqlAddSemicolon) {
            output.append(";")
        }

        var stringOutput = output.toString()

        if (DriverSpy.TrimExtraBlankLinesInSql) {
            val lineReader = LineNumberReader(
                StringReader(
                    stringOutput
                )
            )

            output = StringBuilder()

            var contiguousBlankLines = 0
            try {
                while (true) {
                    val line: String = lineReader.readLine() ?: break

                    // is this line blank?
                    if (line.trim { it <= ' ' }.isEmpty()) {
                        contiguousBlankLines++
                        // skip contiguous blank lines
                        if (contiguousBlankLines > 1) {
                            continue
                        }
                    } else {
                        contiguousBlankLines = 0
                        output.append(line)
                    }
                    output.append("\n")
                }
            } catch (e: IOException) {
                // since we are reading from a buffer, this isn't likely to happen,
                // but if it does we just ignore it and treat it like its the end of the
                // stream
            }
            stringOutput = output.toString()
        }

        // trim whitespace that is the same from the front of each line in the SQL
        if (DriverSpy.TrimSqlLines) {
            // the algorithm below is not the most efficient possible, but it
            // represents a reasonable trade off between performance and
            // maintainability as well as time to develop in the first place.

            // There are a lot of approaches that could be taken to make it run
            // faster if the need arises, but the size of the strings involved
            // shouldn't require that, at least at this time.

            // root line to use for comparison purposes.

            var rootLine: String? = null

            val lineReader = LineNumberReader(StringReader(stringOutput))

            // first make one pass to gather the lines into a List
            val linesList: MutableList<String> = ArrayList()
            try {
                while (true) {
                    var line: String = lineReader.readLine() ?: break

                    line = Utilities.rtrim(line)
                    if (rootLine == null && line.isNotEmpty()) {
                        rootLine = line
                    }

                    // any lines that are all whitespace get collapsed here to an empty
                    // string so we will know that we can
                    // skip those completely in the next stage
                    linesList.add(line)
                }
            } catch (e: IOException) {
                // since we are reading from a buffer, this isn't likely to happen,
                // but if it does we just ignore it and treat it like its the end of the
                // stream
            }

            // early termination... only one line, or no output!
            if (rootLine == null || linesList.size <= 1) {
                return stringOutput.trim { it <= ' ' }
            }

            // now make multiple passes comparing whitespace from each line until
            // a deviation occurs
            // then we know how much whitespace to consume from each line
            var whiteSpaceIndex = -1

            outer@ while (true) {
                whiteSpaceIndex++

                // walk each string until we find non whitespace or divergent types
                // of whitespace

                // we are safe from terminating this loop off the end of the string
                // because the previous step collapsed all white space strings to
                // an empty string and we skip empty strings here, so one of the strings
                // must necessarily terminate the loop via a non-whitespace char
                // (or earlier via diverging types of whitespace , like a space vs. a
                // tab)
                for (line in linesList) {
                    // completely blank lines are exempt from this check...
                    if (line.isEmpty()) {
                        continue
                    }

                    val ch = line[whiteSpaceIndex]

                    // whitespace must match identically to be considered for collapsing
                    if (!Character.isWhitespace(ch) ||
                        ch != rootLine[whiteSpaceIndex]
                    ) {
                        break@outer
                    }
                }
            }

            // now that we know how much we can trim from each string, do the trim
            if (whiteSpaceIndex > 0) {
                output = StringBuilder()
                for (line in linesList) {
                    if (line.isNotEmpty()) {
                        output.append(line.substring(whiteSpaceIndex))
                    }
                    output.append(nl)
                }
                stringOutput = output.toString()
            }
        }

        return stringOutput
    }

    /**
     * Special call that is called only for JDBC method calls that contain SQL.
     *
     * @param spy the Spy wrapping the class where the SQL occurred.
     *
     * @param execTime how long it took the SQL to run, in milliseconds.
     *
     * @param methodCall a description of the name and call parameters of the
     * method that generated the SQL.
     *
     * @param sql SQL that occurred.
     */
    override fun sqlTimingOccured(
        spy: Spy, execTime: Long, methodCall: String?,
        sql: String
    ) {
        if (sqlTimingLogger.isErrorEnabled &&
            (!DriverSpy.DumpSqlFilteringOn || shouldSqlBeLogged(sql))
        ) {
            if (DriverSpy.SqlTimingErrorThresholdEnabled &&
                execTime >= DriverSpy.SqlTimingErrorThresholdMsec
            ) {
                sqlTimingLogger.error(
                    buildSqlTimingDump(
                        spy, execTime,
                        sql, sqlTimingLogger.isDebugEnabled
                    )
                )
            } else if (sqlTimingLogger.isWarnEnabled) {
                if (DriverSpy.SqlTimingWarnThresholdEnabled &&
                    execTime >= DriverSpy.SqlTimingWarnThresholdMsec
                ) {
                    sqlTimingLogger.warn(
                        buildSqlTimingDump(
                            spy, execTime,
                            sql, sqlTimingLogger.isDebugEnabled
                        )
                    )
                } else if (sqlTimingLogger.isDebugEnabled) {
                    sqlTimingLogger.debug(
                        buildSqlTimingDump(
                            spy, execTime,
                            sql, true
                        )
                    )
                } else if (sqlTimingLogger.isInfoEnabled) {
                    sqlTimingLogger.info(
                        buildSqlTimingDump(
                            spy, execTime,
                            sql, false
                        )
                    )
                }
            }
        }
    }

    /**
     * Helper method to quickly build a SQL timing dump output String for logging.
     *
     * @param spy the Spy wrapping the class where the SQL occurred.
     *
     * @param execTime how long it took the SQL to run, in milliseconds.
     *
     * @param sql SQL that occurred.
     *
     * @param debugInfo if true, include debug info at the front of the output.
     *
     * @return a SQL timing dump String for logging.
     */
    private fun buildSqlTimingDump(
        spy: Spy, execTime: Long,
        sql: String, debugInfo: Boolean
    ): String {
        val out = StringBuilder()

        if (debugInfo) {
            out.append(Companion.debugInfo)
            out.append(nl)
            out.append(spy.connectionNumber)
            out.append(". ")
        }

        // NOTE: if both sql dump and sql timing dump are on, the processSql
        // algorithm will run TWICE once at the beginning and once at the end
        // this is not very efficient but usually
        // only one or the other dump should be on and not both.

        out.append(processSql(sql))
        out.append(" {executed in ")
        out.append(execTime)
        out.append(" msec}")

        return out.toString()
    }

    /**
     * Log a Setup and/or administrative log message for log4jdbc.
     *
     * @param msg message to log.
     */
    override fun debug(msg: String?) {
        debugLogger.debug(msg)
    }

    /**
     * Called whenever a new connection spy is created.
     *
     * @param spy ConnectionSpy that was created.
     */
    override fun connectionOpened(spy: Spy) {
        if (connectionLogger.isDebugEnabled) {
            connectionLogger.info("${spy.connectionNumber.toString()}. Connection opened $debugInfo")
            connectionLogger.debug(ConnectionSpy.openConnectionsDump)
        } else {
            connectionLogger.info(spy.connectionNumber.toString() + ". Connection opened")
        }
    }

    /**
     * Called whenever a connection spy is closed.
     *
     * @param spy ConnectionSpy that was closed.
     */
    override fun connectionClosed(spy: Spy) {
        if (connectionLogger.isDebugEnabled) {
            connectionLogger.info(
                spy.connectionNumber.toString() + ". Connection closed " +
                        debugInfo
            )
            connectionLogger.debug(ConnectionSpy.openConnectionsDump)
        } else {
            connectionLogger.info(spy.connectionNumber.toString() + ". Connection closed")
        }
    }

    companion object {
        private val nl: String = System.lineSeparator()

        private val debugInfo: String?
            /**
             * Get debugging info - the module and line number that called the logger
             * version that prints the stack trace information from the point just before
             * we got it (net.sf.log4jdbc)
             * <br></br>
             * if the optional log4jdbc.debug.stack.prefix system property is defined then
             * the last call point from an application is shown in the debug trace output,
             * instead of the last direct caller into log4jdbc
             *
             * @return debugging info for whoever called into JDBC from within the
             * application.
             */
            get() {
                val t = Throwable()
                t.fillInStackTrace()

                val stackTrace = t.stackTrace ?: return null

                var className: String

                val dump = StringBuilder()

                /*
                 * The DumpFullDebugStackTrace option is useful in some situations when we
                 * want to see the full stack trace in the debug info- watch out though as
                 * this will make the logs HUGE!
                 */
                if (DriverSpy.DumpFullDebugStackTrace) {
                    var first = true
                    for (stackTraceElement in stackTrace) {
                        className = stackTraceElement.className
                        if (!className.startsWith("net.sf.log4jdbc")) {
                            if (first) {
                                first = false
                            } else {
                                dump.append("  ")
                            }
                            dump.append("at ")
                            dump.append(stackTraceElement)
                            dump.append(nl)
                        }
                    }
                } else {
                    dump.append(" ")
                    var firstLog4jdbcCall = 0
                    var lastApplicationCall = 0

                    for (i in stackTrace.indices) {
                        className = stackTrace[i].className
                        if (className.startsWith("net.sf.log4jdbc")) {
                            firstLog4jdbcCall = i
                        } else if (DriverSpy.TraceFromApplication &&
                            className.startsWith(DriverSpy.DebugStackPrefix!!)
                        ) {
                            lastApplicationCall = i
                            break
                        }
                    }
                    var j = lastApplicationCall

                    // if app not found, then use whoever was the last guy that
                    // called a log4jdbc class.
                    if (j == 0) {
                        j = 1 + firstLog4jdbcCall
                    }

                    dump.append(stackTrace[j].className).append(".")
                        .append(stackTrace[j].methodName).append("(")
                        .append(stackTrace[j].fileName).append(":")
                        .append(stackTrace[j].lineNumber).append(")")
                }

                return dump.toString()
            }
    }
}
