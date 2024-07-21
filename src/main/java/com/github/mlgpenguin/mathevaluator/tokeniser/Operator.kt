package com.github.mlgpenguin.mathevaluator.tokeniser

import java.math.BigDecimal
import kotlin.math.pow

enum class Operator(private val operation: (BigDecimal, BigDecimal) -> BigDecimal) {
    PLUS({a, b -> a + b}),
    MINUS({a, b -> a - b}),
    MULTIPLY({a, b -> a * b}),
    DIVIDE({a, b -> if (b == BigDecimal.ZERO) BigDecimal.ZERO else a.divide(b, 16, java.math.RoundingMode.HALF_UP) }),
    EXPONENT({a, b -> a.toDouble().pow(b.toDouble()).toBigDecimal()}), // there are better ways but this'll do
    MODULO({a, b -> a % b})
    ;

    fun execute(a: BigDecimal, b: BigDecimal) = operation.invoke(a, b)

    companion object {
        val symbols = "+-*/^%".toCharArray()

        fun bySymbol(symbol: Char) = when (symbol) {
            '+' -> PLUS
            '-' -> MINUS
            '*' -> MULTIPLY
            '/' -> DIVIDE
            '^' -> EXPONENT
            '%' -> MODULO
            else -> throw IllegalArgumentException("Unknown operator $symbol")
        }
    }

}