package com.github.mlgpenguin.mathevaluator.tokeniser

import com.github.mlgpenguin.mathevaluator.Util
import com.github.mlgpenguin.mathevaluator.Value
import java.util.*

object Tokeniser {

    class InvalidParserOperationException(error: String): RuntimeException("Unsupported operation: $error")

    abstract class Token() {
        abstract val value: Double
    }

    fun <T> Stack<T>.peekOrNull() = if (this.isEmpty()) null else this.peek()

    class ValueToken(override val value: Double) : Token() {

        companion object {
            fun read(stack: Stack<Char>): ValueToken {
                var numString = ""
                while (true) {
                    val next = stack.peekOrNull() ?: break
                    if ( next in '0'..'9'
                        || (next == '.' && !numString.contains('.'))
                        || (next == '-' && numString.isEmpty()) ) numString += stack.pop()
                    else break
                }
                if (numString == "-") throw InvalidParserOperationException("Found negative (-) symbol in unexpected location")
                // TODO: Check that it doesn't exceed precision / size restraints
                return ValueToken(numString.toDouble())
            }
        }

    }

    class OperatorToken(val operation: Operator): Token() {
        override val value: Double
            get() = throw InvalidParserOperationException("Attempted to find value of operator")

        companion object {
            fun byTopSymbol(stack: Stack<Char>) = OperatorToken(Operator.bySymbol(stack.pop()))
        }
    }

    private val precedences = listOf(
        listOf(Operator.EXPONENT),
        listOf(Operator.MODULO),
        listOf(Operator.MULTIPLY, Operator.DIVIDE),
        listOf(Operator.PLUS, Operator.MINUS)
    )

    class NestedToken(val tokens: List<Token>): Token() {
        override val value get() = sumTokens()

        private fun sumTokens(): Double {
            if (tokens.isEmpty()) return 0.0
            if (tokens.size == 1) return tokens[0].value

            val operators = mutableListOf<OperatorToken>()
            val other = mutableListOf<Token>()

            for (token in tokens) {
                if (token is OperatorToken) operators.add(token)
                else other.add(token)
            }

            // Precendence finding >>// Can use a BST in future
            while (operators.isNotEmpty()) {
                // Find highest precedence operator closest to left
                var op: IndexedValue<OperatorToken>? = null
                for (precedence in precedences) {
                    val found = operators.withIndex().firstOrNull { it.value.operation in precedence } ?: continue
                    op = found
                    break
                }
                val i = op!!.index
                val result = op.value.operation.execute(other[i].value, other[i + 1].value)
                other[i] = ValueToken(result)
                other.removeAt(i + 1)
                operators.removeAt(i)
            }
            return other[0].value
        }

        companion object {
            /**
             * Reads any set of tokens inside brackets, i.e. ( 3 + 4 ) recursively
             * - If given a set of tokens without surrounding brackets, will return an empty Nested Token
             */
            fun read(stack: Stack<Char>): NestedToken {
                val builder = StringBuilder()
                var opened = 0
                while (true) {
                    val car = stack.pop()

                    if (car == '(') opened++
                    else if (car == ')') opened--

                    builder.append(car)
                    if (opened == 0) break
                }
                return NestedToken(tokenise(builder.toString().substring(1, builder.length-1))) // tokenise the inside without brackets
            }
        }

    }

    class PrefixFunctionToken(val function: (Double) -> Double, val innerTokens: NestedToken): Token() {
        override val value: Double = function.invoke((innerTokens.value)).takeIf { !it.isNaN() } ?: throw InvalidParserOperationException("Unexpected input $innerTokens on $function")


        companion object {
            fun read(stack: Stack<Char>): PrefixFunctionToken {
                val builder = StringBuilder()
                while (stack.peek() in 'a'..'z') builder.append(stack.pop())
                val functionName = builder.toString().takeIf { it.isNotEmpty() } ?: throw InvalidParserOperationException("Attempted to parse prefix function with no name")
                val function = Util.getPrefixFunctionByName(functionName) ?: throw InvalidParserOperationException("Unknown prefix function $functionName")
                return PrefixFunctionToken(function, NestedToken.read(stack))
            }
        }

    }

    private fun tokenise(expression: String): List<Token> {
        val stack = Stack<Char>()
        expression.reversed().forEach(stack::push)
        val tokens = mutableListOf<Token>()

        if (stack.peek() == '-') tokens.add(ValueToken.read(stack))

        while (stack.isNotEmpty()) {
            val next = stack.peek()
            if ( next in '0'..'9' || (next == '-' && tokens.last() is OperatorToken) ) tokens += ValueToken.read(stack)
            else if ( next in Operator.symbols ) tokens += OperatorToken.byTopSymbol(stack)
            else if ( next == '(' ) tokens += NestedToken.read(stack)
            else if ( next in 'a'..'z' ) tokens += PrefixFunctionToken.read(stack)
            else throw InvalidParserOperationException("Unexpected character in stack! ($next)")
        }
        return tokens
    }

    fun evaluate(expression: String): Double {
        val exp = Util.sanitise(expression)
        if (!Util.isValidSyntax(exp)) throw Util.InvalidSyntaxException()
        return NestedToken(tokenise(exp)).value
    }

    fun evaluateValue(expression: String) = Value(evaluate(expression))
}