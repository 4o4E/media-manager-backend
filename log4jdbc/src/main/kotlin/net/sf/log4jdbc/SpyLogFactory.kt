package net.sf.log4jdbc

/**
 * A provider for a SpyLogDelegator.  This allows a single switch point to abstract
 * away which logging system to use for spying on JDBC calls.
 *
 * The SLF4J logging facade is used, which is a very good general purpose facade for plugging into
 * numerous java logging systems, simply and easily.
 *
 * @author Arthur Blake
 */
object SpyLogFactory {
    /**
     * Get the default SpyLogDelegator for logging to the logger.
     *
     * @return the default SpyLogDelegator for logging to the logger.
     */
    /**
     * The logging system of choice.
     */
    val spyLogDelegator = Slf4jSpyLogDelegator()
}
