package me.superpenguin.mathevaluator

internal object Util {
    private const val OPERATORS = "-\\+*/\\^"
    private const val NUMBER = "-?[0-9]+\\.?[0-9]*"

    private val NUMBER_REGEX = Regex(NUMBER)
    // letters, consecutive operators, operators closing brackets and ending strings
    private val INVALID_FORMAT = Regex("(?i)[^\\d$OPERATORS().]|[.+*/]{2,}|[$OPERATORS]$|[$OPERATORS][)]|^[.+*/)]")

    fun isValidSyntax(expression: String) = !INVALID_FORMAT.containsMatchIn(expression)
            && expression.count { it == '(' } == expression.count { it == ')' }
}
