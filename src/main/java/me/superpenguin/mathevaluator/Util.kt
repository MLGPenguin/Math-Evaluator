package me.superpenguin.mathevaluator

internal object Util {
    private const val OPERATORS = "-\\+*/\\^"
    private const val NUMBER = "-?[0-9]+\\.?[0-9]*"

    private val NUMBER_REGEX = Regex(NUMBER)
    // letters, consecutive operators, operators closing brackets and ending strings
    private val INVALID_FORMAT = Regex("(?i)[^\\d$OPERATORS().]|[.+*/]{2,}|[$OPERATORS]$|[$OPERATORS][)]|^[.+*/)]")

    fun isValidSyntax(expression: String, sanitised: String = sanitise(expression)) = !INVALID_FORMAT.containsMatchIn(sanitised)
            && sanitised.count { it == '(' } == sanitised.count { it == ')' }

    private val NUMBER_BEFORE_BRACKET_REGEX = Regex("($NUMBER_REGEX)\\(")

    fun sanitise(expression: String) = expression
        .replace(" ", "")
        .replace("--", "+")
        .replace(NUMBER_BEFORE_BRACKET_REGEX, "$1*(")
        .replace("+-", "-")
}
