package net.sf.log4jdbc

/**
 * Delegates Spy events to a logger.
 * This interface is used for all logging activity used by log4jdbc and hides the specific implementation
 * of any given logging system from log4jdbc.
 *
 * @author Arthur Blake
 */
interface SpyLogDelegator {
    /**
     * Determine if any of the jdbc or sql loggers are turned on.
     *
     * @return true if any of the jdbc or sql loggers are enabled at error level or higher.
     */
    val isJdbcLoggingEnabled: Boolean

    /**
     * Called when a spied upon method throws an Exception.
     *
     * @param spy        the Spy wrapping the class that threw an Exception.
     * @param methodCall a description of the name and call parameters of the method generated the Exception.
     * @param e          the Exception that was thrown.
     * @param sql        optional sql that occured just before the exception occured.
     * @param execTime   optional amount of time that passed before an exception was thrown when sql was being executed.
     * caller should pass -1 if not used
     */
    fun exceptionOccured(spy: Spy, methodCall: String, e: Exception?, sql: String?, execTime: Long)

    /**
     * Called when spied upon method call returns.
     *
     * @param spy        the Spy wrapping the class that called the method that returned.
     * @param methodCall a description of the name and call parameters of the method that returned.
     * @param returnMsg  return value converted to a String for integral types, or String representation for Object
     * return types this will be null for void return types.
     */
    fun methodReturned(spy: Spy, methodCall: String, returnMsg: String)

    /**
     * Called when a spied upon object is constructed.
     *
     * @param spy              the Spy wrapping the class that called the method that returned.
     * @param constructionInfo information about the object construction
     */
    fun constructorReturned(spy: Spy?, constructionInfo: String?)

    /**
     * Special call that is called only for JDBC method calls that contain SQL.
     *
     * @param spy        the Spy wrapping the class where the SQL occured.
     * @param methodCall a description of the name and call parameters of the method that generated the SQL.
     * @param sql        sql that occured.
     */
    fun sqlOccured(spy: Spy, methodCall: String?, sql: String)

    /**
     * Similar to sqlOccured, but reported after SQL executes and used to report timing stats on the SQL
     *
     * @param spy the    Spy wrapping the class where the SQL occured.
     * @param execTime   how long it took the sql to run, in msec.
     * @param methodCall a description of the name and call parameters of the method that generated the SQL.
     * @param sql        sql that occured.
     */
    fun sqlTimingOccured(spy: Spy, execTime: Long, methodCall: String?, sql: String)

    /**
     * Called whenever a new connection spy is created.
     *
     * @param spy ConnectionSpy that was created.
     */
    fun connectionOpened(spy: Spy)

    /**
     * Called whenever a connection spy is closed.
     *
     * @param spy ConnectionSpy that was closed.
     */
    fun connectionClosed(spy: Spy)

    /**
     * Log a Setup and/or administrative log message for log4jdbc.
     *
     * @param msg message to log.
     */
    fun debug(msg: String?)
}