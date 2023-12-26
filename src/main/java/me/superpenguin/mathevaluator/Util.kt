package me.superpenguin.mathevaluator

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

internal object Util {
    private const val OPERATORS = "-\\+*/\\^"
    private const val NUMBER = "-?[0-9]+\\.?[0-9]*"

    private val NUMBER_REGEX = Regex(NUMBER)
    // letters, consecutive operators, operators closing brackets and ending strings
    private val INVALID_FORMAT = Regex("(?i)[^\\d$OPERATORS().]|[.+*/]{2,}|[$OPERATORS]$|[$OPERATORS][)]|^[.+*/)]|-{3,}")

    fun isValidSyntax(expression: String): Boolean {
        val withoutPrefixes = prefixFunctions.keys.fold(expression) { acc, value -> acc.replace(value, "") }
        val sanitised = sanitise(withoutPrefixes)
        return !INVALID_FORMAT.containsMatchIn(sanitised) && sanitised.count { it == '(' } == sanitised.count { it == ')' }
    }

    private val NUMBER_BEFORE_EXPRESSION_REGEX = Regex("(?i)($NUMBER_REGEX)([a-z(])")
    private val EXACTLY_TWO_DASHES = Regex("(?<!-)--(?!-)")

    private val Int.factorial get() = if (this == 0) 1 else (1..this).fold(1) { acc, value -> acc * value }

    fun sanitise(expression: String) = expression
        .lowercase()
        .replace(" ", "")
        .replace(EXACTLY_TWO_DASHES, "+")
        .replace(NUMBER_BEFORE_EXPRESSION_REGEX, "$1*$2")
        .replace("+-", "-")
        .replace("pi", Math.PI.toString())


    // Cannot contain "pi"
    private val prefixFunctions = HashMap<String, (Double) -> Double>().apply {
        put("radians", Math::toRadians)
        put("sin", ::sin)
        put("cos", ::cos)
        put("tan", ::tan)
        put("sqrt", ::sqrt)
    }

    fun parsePrefixFunction(function: String, value: Double): Double? = prefixFunctions[function]?.invoke(value)
}
