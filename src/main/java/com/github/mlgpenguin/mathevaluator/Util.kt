package com.github.mlgpenguin.mathevaluator

import kotlin.math.*

object Util {
    private const val OPERATORS = "\\-\\+*\\/\\^%"
    private const val NUMBER = "-?[0-9]+\\.?[0-9]*"

    private val NUMBER_REGEX = Regex(NUMBER)
    private val FACTORIAL_REGEX = Regex("([0-9]+)!")
    // letters, consecutive operators, operators closing brackets and ending strings
    private val INVALID_FORMAT = Regex("(?i)[^\\d$OPERATORS().]|[.+*/]{2,}|[$OPERATORS]$|[$OPERATORS][)]|^[.+*/)]|-{3,}")

    fun isValidSyntax(expression: String): Boolean {
        val withoutPrefixes = prefixFunctions.keys.fold(expression.lowercase()) { acc, value -> acc.replace(value, "") }
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
        .replace(Regex("log\\(10\\^($NUMBER_REGEX)\\)"), "$1")
        .let { exp -> FACTORIAL_REGEX.replace(exp) { it.groupValues[1].toInt().factorial.toString() } }


    // Cannot contain "pi"
    // Make sure abbreviations are posted in descending order such that the smallest match is lower.
    private val prefixFunctions: Map<String, (Double) -> Double> = mapOf(
        "radians" to Math::toRadians,
        "rad" to Math::toRadians,
        "degrees" to Math::toDegrees,
        "deg" to Math::toDegrees,
        "sin" to ::sin,
        "cos" to ::cos,
        "tan" to ::tan,
        "sqrt" to ::sqrt,
        "floor" to ::floor,
        "ceil" to ::ceil,
        "absolute" to ::abs,
        "abs" to ::abs,
        "round" to ::round,
        "log" to ::log10
    )

    fun getPrefixFunctionByName(name: String) = prefixFunctions[name]



    class InvalidSyntaxException : RuntimeException {
        constructor(msg: String?) : super(msg)
        constructor() : super("")
    }

}
