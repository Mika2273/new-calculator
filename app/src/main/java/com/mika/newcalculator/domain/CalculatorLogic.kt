package com.mika.newcalculator.domain

import java.text.DecimalFormat

/**
 * Handles the core mathematical logic for the calculator.
 * Parses strings and evaluates expressions.
 */
class CalculatorLogic {

    /**
     * Takes a raw expression string, cleans it, evaluates it, and formats the result.
     */
    fun calculate(expression: String): String {
        return try {
            // Convert display symbols to evaluation symbols
            val evalExpr = expression.replace("÷", "/")
                .replace("×", "*")

            // Remove any whitespace
            val cleanExpr = evalExpr.replace("\\s".toRegex(), "")

            // Evaluate the expression
            val result = evaluateExpression(cleanExpr)

            // Format the result
            formatResult(result)

        } catch (e: Exception) {
            // Return "Error" to be displayed on the UI
            "Error"
        }
    }

    /**
     * Formats the calculation result.
     * Applies thousands separators and limits decimal places to 8.
     */
    private fun formatResult(result: Double): String {
        if (result.isNaN() || result.isInfinite()) return "Error"

        // Format: #,###.######## (comma separator, max 8 decimal places)
        val df = DecimalFormat("#,###.########")
        return df.format(result)
    }

    /**
     * Parses and evaluates a mathematical expression string using a Recursive Descent Parser.
     */
    private fun evaluateExpression(expression: String): Double {
        return if (expression.endsWith("%")) {
            // Handle percentage calculation
            val number = expression.dropLast(1).toDouble()
            number / 100
        } else {
            object : Any() {
                var pos = -1
                var ch = 0.toChar()

                fun nextChar() {
                    ch = if (++pos < expression.length) expression[pos] else (-1).toChar()
                }

                fun eat(charToEat: Char): Boolean {
                    while (ch == ' ') nextChar()
                    if (ch == charToEat) {
                        nextChar()
                        return true
                    }
                    return false
                }

                fun parse(): Double {
                    nextChar()
                    val x = parseExpression()
                    if (pos < expression.length) throw RuntimeException("Unexpected: " + ch)
                    return x
                }

                fun parseExpression(): Double {
                    var x = parseTerm()
                    while (true) {
                        when {
                            eat('+') -> x += parseTerm()
                            eat('-') -> x -= parseTerm()
                            else -> return x
                        }
                    }
                }

                fun parseTerm(): Double {
                    var x = parseFactor()
                    while (true) {
                        when {
                            eat('*') -> x *= parseFactor()
                            eat('/') -> x /= parseFactor()
                            else -> return x
                        }
                    }
                }

                fun parseFactor(): Double {
                    if (eat('+')) return parseFactor()
                    if (eat('-')) return -parseFactor()

                    var x: Double
                    val startPos = pos

                    if (eat('(')) {
                        x = parseExpression()
                        eat(')')
                    } else if (ch in '0'..'9' || ch == '.') {
                        while (ch in '0'..'9' || ch == '.') nextChar()
                        x = expression.substring(startPos, pos).toDouble()
                    } else {
                        throw RuntimeException("Unexpected: " + ch)
                    }

                    return x
                }
            }.parse()
        }
    }
}