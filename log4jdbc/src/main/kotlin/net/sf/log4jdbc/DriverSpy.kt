package net.sf.log4jdbc

import java.io.IOException
import java.sql.*
import java.util.*
import java.util.logging.Logger

/**
 * A JDBC driver which is a facade that delegates to one or more real underlying
 * JDBC drivers. The driver will spy on any other JDBC driver that is loaded,
 * simply by prepending `jdbc:log4` to the normal jdbc driver URL
 * used by any other JDBC driver. The driver, by default, also loads a bunch of
 * well known drivers at class load time, so that this driver can be
 * "dropped in" to any Java program that uses these drivers without making any
 * code changes.
 *
 *
 * The well known driver classes that are loaded are:
 *
 *
 *
 *
 * `
 *
 *
 *  * oracle.jdbc.driver.OracleDriver
 *  * oracle.jdbc.OracleDriver
 *  * com.sybase.jdbc2.jdbc.SybDriver
 *  * net.sourceforge.jtds.jdbc.Driver
 *  * com.microsoft.jdbc.sqlserver.SQLServerDriver
 *  * com.microsoft.sqlserver.jdbc.SQLServerDriver
 *  * weblogic.jdbc.sqlserver.SQLServerDriver
 *  * com.informix.jdbc.IfxDriver
 *  * org.apache.derby.jdbc.ClientDriver
 *  * org.apache.derby.jdbc.EmbeddedDriver
 *  * com.mysql.jdbc.Driver
 *  * com.mysql.cj.jdbc.Driver
 *  * org.mariadb.jdbc.Driver
 *  * org.postgresql.Driver
 *  * org.hsqldb.jdbcDriver
 *  * org.h2.Driver
 *
 *
` *
 *
 *
 *
 *
 * Additional drivers can be set via a property: **log4jdbc.drivers** This
 * can be either a single driver class name or a list of comma separated driver
 * class names.
 *
 *
 * The autoloading behavior can be disabled by setting a property:
 * **log4jdbc.auto.load.popular.drivers** to false. If that is done, then the
 * only drivers that log4jdbc will attempt to load are the ones specified in
 * **log4jdbc.drivers**.
 *
 *
 * If any of the above driver classes cannot be loaded, the driver continues on
 * without failing.
 *
 *
 * Note that the `getMajorVersion`, `getMinorVersion` and
 * `jdbcCompliant` method calls attempt to delegate to the last
 * underlying driver requested through any other call that accepts a JDBC URL.
 *
 *
 * This can cause unexpected behavior in certain circumstances. For example, if
 * one of these 3 methods is called before any underlying driver has been
 * established, then they will return default values that might not be correct
 * in all situations. Similarly, if this spy driver is used to spy on more than
 * one underlying driver concurrently, the values returned by these 3 method
 * calls may change depending on what the last underlying driver used was at the
 * time. This will not usually be a problem, since the driver is retrieved by
 * it's URL from the DriverManager in the first place (thus establishing an
 * underlying real driver), and in most applications their is only one database.
 *
 * @author Arthur Blake
 */
class DriverSpy : Driver {
    /**
     * The last actual, underlying driver that was requested via a URL.
     */
    private var lastUnderlyingDriverRequested: Driver? = null

    /**
     * Get the major version of the driver. This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 1 will be returned.)
     *
     * @return the major version of the JDBC driver.
     */
    override fun getMajorVersion() =
        if (lastUnderlyingDriverRequested == null) 1 else lastUnderlyingDriverRequested!!.majorVersion

    /**
     * Get the minor version of the driver. This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 0 will be returned.)
     *
     * @return the minor version of the JDBC driver.
     */
    override fun getMinorVersion() =
        if (lastUnderlyingDriverRequested == null) 0 else lastUnderlyingDriverRequested!!.minorVersion

    /**
     * Report whether the underlying driver is JDBC compliant. If there is no
     * underlying driver, false will be returned, because the driver cannot
     * actually do any work without an underlying driver.
     *
     * @return `true` if the underlying driver is JDBC Compliant;
     * `false` otherwise.
     */
    override fun jdbcCompliant() =
        lastUnderlyingDriverRequested != null && lastUnderlyingDriverRequested!!.jdbcCompliant()

    /**
     * Returns true if this is a `jdbc:log4` URL and if the URL is for
     * an underlying driver that this DriverSpy can spy on.
     *
     * @param url JDBC URL.
     * @return true if this Driver can handle the URL.
     * @throws SQLException if a database access error occurs
     */
    @Throws(SQLException::class)
    override fun acceptsURL(url: String): Boolean {
        val d = getUnderlyingDriver(url) ?: return false
        lastUnderlyingDriverRequested = d
        return true
    }

    /**
     * Given a `jdbc:log4` type URL, find the underlying real driver
     * that accepts the URL.
     *
     * @param u JDBC connection URL.
     * @return Underlying driver for the given URL. Null is returned if the URL is
     * not a `jdbc:log4` type URL or there is no underlying
     * driver that accepts the URL.
     * @throws SQLException if a database access error occurs.
     */
    @Throws(SQLException::class)
    private fun getUnderlyingDriver(u: String): Driver? {
        var url = u
        if (url.startsWith("jdbc:log4")) {
            url = url.substring(9)

            val e = DriverManager.getDrivers()

            while (e.hasMoreElements()) {
                val d = e.nextElement()

                if (d.acceptsURL(url)) {
                    return d
                }
            }
        }
        return null
    }

    /**
     * Get a Connection to the database from the underlying driver that this
     * DriverSpy is spying on. If logging is not enabled, an actual Connection to
     * the database returned. If logging is enabled, a ConnectionSpy object which
     * wraps the real Connection is returned.
     *
     * @param jdbcUrl  JDBC connection URL .
     * @param info a list of arbitrary string tag/value pairs as connection
     * arguments. Normally at least a "user" and "password" property should
     * be included.
     * @return a `Connection` object that represents a connection to
     * the URL.
     * @throws SQLException if a database access error occurs
     */
    @Throws(SQLException::class)
    override fun connect(jdbcUrl: String, info: Properties): Connection? {
        var url = jdbcUrl
        val driver = getUnderlyingDriver(url) ?: return null

        // get actual URL that the real driver expects
        // (strip off "jdbc:log4" from url)
        url = url.substring(9)

        lastUnderlyingDriverRequested = driver
        val connection = driver.connect(url, info) ?: throw SQLException("invalid or unknown driver url: $url")

        return if (log.isJdbcLoggingEnabled) {
            ConnectionSpy(connection, rdbmsSpecifics[driver.javaClass.name] ?: defaultRdbmsSpecifics)
        } else {
            connection
        }
    }

    /**
     * Gets information about the possible properties for the underlying driver.
     *
     * @param url  the URL of the database to which to connect
     * @param info a proposed list of tag/value pairs that will be sent on connect
     * open
     * @return an array of `DriverPropertyInfo` objects describing
     * possible properties. This array may be an empty array if no
     * properties are required.
     * @throws SQLException if a database access error occurs
     */
    @Throws(SQLException::class)
    override fun getPropertyInfo(url: String, info: Properties): Array<DriverPropertyInfo?> {
        val driver = getUnderlyingDriver(url) ?: return arrayOfNulls(0)

        lastUnderlyingDriverRequested = driver
        return driver.getPropertyInfo(url, info)
    }

    // TODO: not 100% sure this is right
    @Throws(SQLFeatureNotSupportedException::class)
    override fun getParentLogger(): Logger {
        if (lastUnderlyingDriverRequested != null) {
            return lastUnderlyingDriverRequested!!.parentLogger
        }
        throw SQLFeatureNotSupportedException("Not supported by log4jdbc")
    }

    companion object {
        /**
         * Maps driver class names to RdbmsSpecifics objects for each kind of
         * database.
         */
        private val rdbmsSpecifics: MutableMap<String, RdbmsSpecifics>

        val log = SpyLogFactory.spyLogDelegator

        /**
         * Optional package prefix to use for finding application generating point of
         * SQL.
         */
        var DebugStackPrefix: String?

        /**
         * Flag to indicate debug trace info should be from the calling application
         * point of view (true if DebugStackPrefix is set.)
         */
        var TraceFromApplication: Boolean

        /**
         * Flag to indicate if a warning should be shown if SQL takes more than
         * SqlTimingWarnThresholdMsec milliseconds to run. See below.
         */
        var SqlTimingWarnThresholdEnabled: Boolean

        /**
         * An amount of time in milliseconds for which SQL that executed taking this
         * long or more to run shall cause a warning message to be generated on the
         * SQL timing logger.
         *
         *
         * This threshold will *ONLY* be used if SqlTimingWarnThresholdEnabled is
         * true.
         */
        var SqlTimingWarnThresholdMsec: Long = 0

        /**
         * Flag to indicate if an error should be shown if SQL takes more than
         * SqlTimingErrorThresholdMsec milliseconds to run. See below.
         */
        var SqlTimingErrorThresholdEnabled: Boolean

        /**
         * An amount of time in milliseconds for which SQL that executed taking this
         * long or more to run shall cause an error message to be generated on the SQL
         * timing logger.
         *
         *
         * This threshold will *ONLY* be used if SqlTimingErrorThresholdEnabled
         * is true.
         */
        var SqlTimingErrorThresholdMsec: Long = 0

        /**
         * When dumping boolean values, dump them as 'true' or 'false'. If this option
         * is not set, they will be dumped as 1 or 0 as many databases do not have a
         * boolean type, and this allows for more portable sql dumping.
         */
        var DumpBooleanAsTrueFalse: Boolean

        /**
         * When dumping SQL, if this is greater than 0, than the SQL will be broken up
         * into lines that are no longer than this value.
         */
        var DumpSqlMaxLineLength: Int

        /**
         * If this is true, display a special warning in the log along with the SQL
         * when the application uses a Statement (as opposed to a PreparedStatement.)
         * Using Statements for frequently used SQL can sometimes result in
         * performance and/or security problems.
         */
        var StatementUsageWarn: Boolean

        /**
         * Options to more finely control which types of SQL statements will be
         * dumped, when dumping SQL. By default all 5 of the following will be true.
         * If any one is set to false, then that particular type of SQL will not be
         * dumped.
         */
        var DumpSqlSelect: Boolean
        var DumpSqlInsert: Boolean
        var DumpSqlUpdate: Boolean
        var DumpSqlDelete: Boolean
        var DumpSqlCreate: Boolean

        // only true if one ore more of the above 4 flags are false.
        var DumpSqlFilteringOn: Boolean

        /**
         * If true, add a semilcolon to the end of each SQL dump.
         */
        var DumpSqlAddSemicolon: Boolean

        /**
         * If dumping in debug mode, dump the full stack trace. This will result in a
         * VERY voluminous output, but can be very useful under some circumstances.
         */
        var DumpFullDebugStackTrace: Boolean

        /**
         * Attempt to Automatically load a set of popular JDBC drivers?
         */
        var AutoLoadPopularDrivers: Boolean

        /**
         * Trim SQL before logging it?
         */
        var TrimSql: Boolean

        /**
         * Trim SQL line by line (for beginning of line, only trimming consistent
         * white space) If this option is selected, the TrimSql option will be
         * ignored.
         */
        var TrimSqlLines: Boolean

        /**
         * Remove extra Lines in the SQL that consist of only white space? Only when 2
         * or more lines in a row like this occur, will the extra lines (beyond 1) be
         * removed.
         */
        var TrimExtraBlankLinesInSql: Boolean

        /**
         * Coldfusion typically calls PreparedStatement.getGeneratedKeys() after every
         * SQL update call, even if it's not warranted. This typically produces an
         * exception that is ignored by Coldfusion. If this flag is true, then any
         * exception generated by this method is also ignored by log4jdbc.
         */
        var SuppressGetGeneratedKeysException: Boolean

        /**
         * Get a Long option from a property and log a debug message about this.
         *
         * @param props    Properties to get option from.
         * @param propName property key.
         * @return the value of that property key, converted to a Long. Or null if not
         * defined or is invalid.
         */
        private fun getLongOption(props: Properties, propName: String): Long? {
            val propValue = props.getProperty(propName)
            var longPropValue: Long? = null
            if (propValue == null) {
                log.debug("x $propName is not defined")
            } else {
                try {
                    longPropValue = propValue.toLong()
                    log.debug("  $propName = $longPropValue")
                } catch (n: NumberFormatException) {
                    log.debug(
                        "x " + propName + " \"" + propValue +
                                "\" is not a valid number"
                    )
                }
            }
            return longPropValue
        }

        /**
         * Get a Long option from a property and log a debug message about this.
         *
         * @param props    Properties to get option from.
         * @param propName property key.
         * @return the value of that property key, converted to a Long. Or null if not
         * defined or is invalid.
         */
        private fun getLongOption(props: Properties, propName: String, defaultValue: Long): Long {
            val propValue = props.getProperty(propName)
            var longPropValue: Long
            if (propValue == null) {
                log.debug("x $propName is not defined (using default of $defaultValue)")
                longPropValue = defaultValue
            } else {
                try {
                    longPropValue = propValue.toLong()
                    log.debug("  $propName = $longPropValue")
                } catch (n: NumberFormatException) {
                    log.debug("x $propName \"$propValue\" is not a valid number (using default of $defaultValue)")
                    longPropValue = defaultValue
                }
            }
            return longPropValue
        }

        /**
         * Get a String option from a property and log a debug message about this.
         *
         * @param props    Properties to get option from.
         * @param propName property key.
         * @return the value of that property key.
         */
        private fun getStringOption(props: Properties, propName: String): String? {
            var propValue = props.getProperty(propName)
            if (propValue == null || propValue.isEmpty()) {
                log.debug("x $propName is not defined")
                propValue = null // force to null, even if empty String
            } else {
                log.debug("  $propName = $propValue")
            }
            return propValue
        }

        /**
         * Get a boolean option from a property and log a debug message about this.
         *
         * @param props        Properties to get option from.
         * @param propName     property name to get.
         * @param defaultValue default value to use if undefined.
         * @return boolean value found in property, or defaultValue if no property
         * found.
         */
        private fun getBooleanOption(
            props: Properties, propName: String,
            defaultValue: Boolean
        ): Boolean {
            var propValue = props.getProperty(propName)
            val v: Boolean
            if (propValue == null) {
                log.debug("x $propName is not defined (using default value $defaultValue)")
                return defaultValue
            } else {
                propValue = propValue.trim { it <= ' ' }.lowercase(Locale.getDefault())
                v = if (propValue.isEmpty()) {
                    defaultValue
                } else {
                    "true" == propValue || "yes" == propValue || "on" == propValue
                }
            }
            log.debug("  $propName = $v")
            return v
        }

        init {
            log.debug("... log4jdbc initializing ...")

            val propStream = DriverSpy::class.java
                .getResourceAsStream("/log4jdbc.properties")

            val props = Properties(System.getProperties())
            if (propStream != null) {
                try {
                    props.load(propStream)
                } catch (e: IOException) {
                    log.debug(
                        "ERROR!  io exception loading " +
                                "log4jdbc.properties from classpath: " + e.message
                    )
                } finally {
                    try {
                        propStream.close()
                    } catch (e: IOException) {
                        log.debug(
                            "ERROR!  io exception closing property file stream: " +
                                    e.message
                        )
                    }
                }
                log.debug("  log4jdbc.properties loaded from classpath")
            } else {
                log.debug("  log4jdbc.properties not found on classpath")
            }

            // look for additional driver specified in properties
            DebugStackPrefix = getStringOption(props, "log4jdbc.debug.stack.prefix")
            TraceFromApplication = DebugStackPrefix != null

            var thresh = getLongOption(props, "log4jdbc.sqltiming.warn.threshold")
            SqlTimingWarnThresholdEnabled = (thresh != null)
            if (SqlTimingWarnThresholdEnabled) {
                SqlTimingWarnThresholdMsec = thresh!!
            }

            thresh = getLongOption(props, "log4jdbc.sqltiming.error.threshold")
            SqlTimingErrorThresholdEnabled = (thresh != null)
            if (SqlTimingErrorThresholdEnabled) {
                SqlTimingErrorThresholdMsec = thresh!!
            }

            DumpBooleanAsTrueFalse = getBooleanOption(props, "log4jdbc.dump.booleanastruefalse", false)

            DumpSqlMaxLineLength = getLongOption(props, "log4jdbc.dump.sql.maxlinelength", 90L).toInt()

            DumpFullDebugStackTrace = getBooleanOption(props, "log4jdbc.dump.fulldebugstacktrace", false)

            StatementUsageWarn = getBooleanOption(props, "log4jdbc.statement.warn", false)

            DumpSqlSelect = getBooleanOption(props, "log4jdbc.dump.sql.select", true)
            DumpSqlInsert = getBooleanOption(props, "log4jdbc.dump.sql.insert", true)
            DumpSqlUpdate = getBooleanOption(props, "log4jdbc.dump.sql.update", true)
            DumpSqlDelete = getBooleanOption(props, "log4jdbc.dump.sql.delete", true)
            DumpSqlCreate = getBooleanOption(props, "log4jdbc.dump.sql.create", true)

            DumpSqlFilteringOn = !(DumpSqlSelect && DumpSqlInsert && DumpSqlUpdate && DumpSqlDelete && DumpSqlCreate)

            DumpSqlAddSemicolon = getBooleanOption(props, "log4jdbc.dump.sql.addsemicolon", false)

            AutoLoadPopularDrivers = getBooleanOption(props, "log4jdbc.auto.load.popular.drivers", true)

            TrimSql = getBooleanOption(props, "log4jdbc.trim.sql", true)
            TrimSqlLines = getBooleanOption(props, "log4jdbc.trim.sql.lines", false)
            if (TrimSqlLines && TrimSql) {
                log.debug("NOTE, log4jdbc.trim.sql setting ignored because log4jdbc.trim.sql.lines is enabled.")
            }

            TrimExtraBlankLinesInSql = getBooleanOption(props, "log4jdbc.trim.sql.extrablanklines", true)

            SuppressGetGeneratedKeysException =
                getBooleanOption(props, "log4jdbc.suppress.generated.keys.exception", false)

            // The Set of drivers that the log4jdbc driver will preload at instantiation
            // time. The driver can spy on any driver type, it's just a little bit
            // easier to configure log4jdbc if it's one of these types!
            val subDrivers: MutableSet<String> = TreeSet()

            if (AutoLoadPopularDrivers) {
                subDrivers.add("com.mysql.cj.jdbc.Driver")
                subDrivers.add("org.mariadb.jdbc.Driver")
            }

            // look for additional driver specified in properties
            val moreDrivers = getStringOption(props, "log4jdbc.drivers")

            if (moreDrivers != null) {
                val moreDriversArr = moreDrivers.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                for (s in moreDriversArr) {
                    subDrivers.add(s)
                    log.debug("    will look for specific driver $s")
                }
            }

            try {
                DriverManager.registerDriver(DriverSpy())
            } catch (s: SQLException) {
                // this exception should never be thrown, JDBC just defines it
                // for completeness
                throw RuntimeException("could not register log4jdbc driver!", s)
            }

            // instantiate all the supported drivers and remove
            // those not found
            var driverClass: String
            val i = subDrivers.iterator()
            while (i.hasNext()) {
                driverClass = i.next()
                try {
                    Class.forName(driverClass)
                    log.debug("  FOUND DRIVER $driverClass")
                } catch (c: Throwable) {
                    i.remove()
                }
            }

            if (subDrivers.isEmpty()) {
                log.debug("WARNING!  log4jdbc couldn't find any underlying jdbc drivers.")
            }

            val mySql = MySqlRdbmsSpecifics()

            // create lookup Map for specific RDBMS formatters
            rdbmsSpecifics = mutableMapOf(
                "com.mysql.jdbc.Driver" to mySql,
                "com.mysql.cj.jdbc.Driver" to mySql,
                "org.mariadb.jdbc.Driver" to mySql,
            )

            log.debug("... log4jdbc initialized! ...")
        }

        var defaultRdbmsSpecifics = RdbmsSpecifics()

    }
}
