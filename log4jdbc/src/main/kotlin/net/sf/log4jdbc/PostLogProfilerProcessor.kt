package net.sf.log4jdbc

import java.io.FileReader
import java.io.LineNumberReader
import java.io.PrintStream
import java.lang.Exception
import java.util.*

/**
 * Post processes an existing sqltiming log, and creates a profiling report from it.
 * Name of log file is passed in on the command line as the only argument.
 *
 * Assumptions:
 *
 * 1. Each sql statement in the log is separated by a blank line.
 * 2. Each sql statement is terminated with the timing string "{executed in N msec}" where N is the number of
 * milliseconds that the sql executed in.
 *
 */
class PostLogProfilerProcessor(filename: String?, out: PrintStream) {
    /**
     * Total number of sql statements processed.
     */
    private var totalSql = 0L

    /**
     * Number of lines processed.
     */
    private var lineNo = 0L

    /**
     * Total number of milliseconds that all processed sql took to run.
     */
    private var totalMsec = 0L

    /**
     * Milliseconds of the worst single offending sql statement.
     */
    private var maxMsec = 0L

    /**
     * Total combined milliseconds of all flagged sql statements.
     */
    private var flaggedSqlTotalMsec = 0L

    /**
     * Threshold at which sql is deemed to be running slow enough to be flagged.
     */
    private val threshold = 100L

    /**
     * How many top offender sql statements to display in final report
     */
    private var topOffenderCount = 1000L

    /**
     * Collection of all sql that took longer than "threshold" msec to run.
     */
    private val flaggedSql: MutableList<Any?> = LinkedList()

    /**
     * Process given filename, and produce sql profiling report to given PrintStream.
     *
     * @param filename sqltiming log to process.
     * @param out PrintStream to write profiling report to.
     * @throws Exception if reading error occurs.
     */
    init {
        val f = FileReader(filename)
        val l = LineNumberReader(f)

        var line: String?
        var blankLine: Boolean

        var sql = StringBuffer()

        do {
            line = l.readLine()

            if (line != null) {
                blankLine = line.length == 0
                lineNo++
                /*
        if (lineNo%100000L==0L)
        {
          out.println("" + lineNo + " lines...");
        }
*/
                if (blankLine) {
                    processSql(sql)
                    sql = StringBuffer()
                } else {
                    sql.append(line)
                }
            }
        } while (line != null)

        out.println("processed $lineNo lines.")

        f.close()

        // display report to stdout
        out.println("Number of sql statements:  $totalSql")
        out.println("Total number of msec    :  $totalMsec")
        if (totalMsec > 0) {
            out.println("Average msec/statement  :  " + totalSql / totalMsec)
        }

        val flaggedSqlStmts = flaggedSql.size

        if (flaggedSqlStmts > 0) {
            out.println("Sql statements that took more than $threshold msec were flagged.")
            out.println("Flagged sql statements              :  $flaggedSqlStmts")
            out.println("Flagged sql Total number of msec    :  $flaggedSqlTotalMsec")
            out.println("Flagged sql Average msec/statement  :  " + flaggedSqlTotalMsec / flaggedSqlStmts)

            out.println("sorting...")

            val flaggedSqlArray: Array<Any?> = flaggedSql.toTypedArray()
            Arrays.sort(flaggedSqlArray)

            val execTimeSize = ("" + maxMsec).length


            if (topOffenderCount > flaggedSqlArray.size) {
                topOffenderCount = flaggedSqlArray.size.toLong()
            }

            out.println("top " + topOffenderCount + " offender" + (if (topOffenderCount == 1L) "" else "s") + ":")

            var p: ProfiledSql

            for (i in 0 until topOffenderCount) {
                p = flaggedSqlArray[i.toInt()] as ProfiledSql
                out.println(Utilities.rightJustify(execTimeSize, "" + p.execTime) + " " + p.sql)
            }
        }
    }


    private fun processSql(sql: StringBuffer) {
        if (sql.length > 0) {
            totalSql++
            val sqlStr = sql.toString()
            if (sqlStr.endsWith("msec}")) {
                val executedIn = sqlStr.indexOf("{executed in ")
                if (executedIn == -1) {
                    System.err.println("WARNING:  sql w/o timing info found at line $lineNo")
                    return
                }

                // todo: proper error handling for parse
                val msecStr = sqlStr.substring(executedIn + 13, sqlStr.length - 6)
                val msec = msecStr.toLong()
                totalMsec += msec
                if (msec > maxMsec) {
                    maxMsec = msec
                }

                if (msec > threshold) {
                    flagSql(msec, sqlStr)
                    flaggedSqlTotalMsec += msec
                }
            } else {
                System.err.println("WARNING:  sql w/o timing info found at line $lineNo")
            }
        }
    }

    private fun flagSql(msec: Long, sql: String) {
        flaggedSql.add(ProfiledSql(msec, sql))
    }

    private inner class ProfiledSql(val execTime: Long, val sql: String) : Comparable<Any?> {
        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         *
         *
         * In this case the comparison is used to sort flagged sql in descending order.
         * @param other ProfiledSql Object to compare to this ProfiledSql.  Must not be null.
         */
        override fun compareTo(other: Any?): Int {
            return (other as ProfiledSql?)!!.execTime.compareTo(execTime)
        }

        override fun toString(): String {
            return execTime.toString() + " msec:  " + this.sql
        }
    }
}
