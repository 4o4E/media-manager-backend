package net.sf.log4jdbc

/**
 * Static utility methods for use throughout the project.
 */
object Utilities {
    /**
     * Right justify a field within a certain number of spaces.
     * @param fieldSize field size to right justify field within.
     * @param f contents to right justify within field.
     * @return the field, right justified within the requested size.
     */
    fun rightJustify(fieldSize: Int, f: String?): String {
        var field = f
        if (field == null) {
            field = ""
        }
        val output = StringBuilder()
        var i = 0
        val j = fieldSize - field.length
        while (i < j) {
            output.append(' ')
            i++
        }
        output.append(field)
        return output.toString()
    }

    /**
     * Trim whitespace off the right of a string.
     * @param s input String to trim.
     * @return output trimmed string.
     */
    fun rtrim(s: String): String {
        var i = s.length - 1
        while (i >= 0 && Character.isWhitespace(s[i])) {
            i--
        }
        return s.substring(0, i + 1)
    }
}
