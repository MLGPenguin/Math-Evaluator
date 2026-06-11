package com.github.mlgpenguin.mathevaluator.tokeniser

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

private fun BigDecimal.toBigIntegerForBitwise(): BigInteger {
    val remainder = this.remainder(BigDecimal.ONE).abs()
    val epsilon = BigDecimal("1e-9")
    return if (remainder <= epsilon || remainder >= BigDecimal.ONE - epsilon)
        this.setScale(0, RoundingMode.HALF_UP).toBigIntegerExact()
    else
        throw ArithmeticException("Value $this is not an integer; cannot use bitwise operator")
}

enum class Operator(private val operation: (BigDecimal, BigDecimal) -> BigDecimal) {
    PLUS({a, b -> a + b}),
    MINUS({a, b -> a - b}),
    MULTIPLY({a, b -> a * b}),
    DIVIDE({a, b -> if (b == BigDecimal.ZERO) BigDecimal.ZERO else a.divide(b, 16, java.math.RoundingMode.HALF_UP) }),
    EXPONENT({a, b -> a.toDouble().pow(b.toDouble()).toBigDecimal()}), // there are better ways but this'll do
    MODULO({a, b -> a % b}),
    BINARY_OR({a, b -> a.toBigIntegerForBitwise().or(b.toBigIntegerForBitwise()).toBigDecimal() }),
    BINARY_AND({a, b -> a.toBigIntegerForBitwise().and(b.toBigIntegerForBitwise()).toBigDecimal() }),
    RIGHT_SHIFT({a, b -> (a.toBigIntegerForBitwise() shr b.toBigIntegerForBitwise().toInt()).toBigDecimal() }),
    LEFT_SHIFT({a, b -> (a.toBigIntegerForBitwise() shl b.toBigIntegerForBitwise().toInt()).toBigDecimal() }),
    ;

    fun execute(a: BigDecimal, b: BigDecimal) = operation.invoke(a, b)

    companion object {
        val symbols: Set<String> = setOf("+", "-", "*", "/", "^", "%", "|", "&", ">>", "<<")
        val firstChars: Set<Char> = symbols.map { it[0] }.toSet()

        fun bySymbol(symbol: String) = when (symbol) {
            "+"  -> PLUS
            "-"  -> MINUS
            "*"  -> MULTIPLY
            "/"  -> DIVIDE
            "^"  -> EXPONENT
            "%"  -> MODULO
            "|"  -> BINARY_OR
            "&"  -> BINARY_AND
            ">>" -> RIGHT_SHIFT
            "<<" -> LEFT_SHIFT
            else -> throw IllegalArgumentException("Unknown operator $symbol")
        }
    }

}
