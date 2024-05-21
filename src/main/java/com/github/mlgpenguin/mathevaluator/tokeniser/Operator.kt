package com.github.mlgpenguin.mathevaluator.tokeniser

import kotlin.math.pow

enum class Operator(private val operation: (Double, Double) -> Double) {
    PLUS({a, b -> a + b}),
    MINUS({a, b -> a - b}),
    MULTIPLY({a, b -> a * b}),
    DIVIDE({a, b -> if (b == 0.0) 0.0 else a / b}),
    EXPONENT({a, b -> a.pow(b) }),
    MODULO({a, b -> a % b})
    ;

    fun execute(a: Double, b: Double) = operation.invoke(a, b)

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