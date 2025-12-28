package com.mika.newcalculator.domain

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for CalculatorLogic.
 * Verifies mathematical operations, formatting, and error handling.
 */
class CalculatorLogicTest {

    private val logic = CalculatorLogic()

    @Test
    fun `Basic addition`() {
        val result = logic.calculate("2 + 3")
        assertEquals("5", result)
    }

    @Test
    fun `Basic subtraction`() {
        val result = logic.calculate("10 - 4")
        assertEquals("6", result)
    }

    @Test
    fun `Multiplication priority`() {
        // Multiplication should happen before addition (2 + 12 = 14)
        val result = logic.calculate("2 + 3 * 4")
        assertEquals("14", result)
    }

    @Test
    fun `Division with decimals`() {
        val result = logic.calculate("10 / 4")
        assertEquals("2.5", result)
    }

    @Test
    fun `Formatting with commas`() {
        // Should add commas for thousands
        val result = logic.calculate("1000 + 2000")
        assertEquals("3,000", result)
    }

    @Test
    fun `Division by zero returns Error`() {
        val result = logic.calculate("5 / 0")
        assertEquals("Error", result)
    }

    @Test
    fun `Complex expression`() {
        // (4 + 6) * 2 = 20
        // Without parenthesis it would be 4 + 12 = 16.
        // Note: Our parser supports parenthesis if the UI passes them.
        // Let's test standard priority without parenthesis for now based on UI buttons.
        val result = logic.calculate("4 + 6 * 2")
        assertEquals("16", result)
    }

    @Test
    fun `Decimal addition`() {
        val result = logic.calculate("1.5 + 2.5")
        assertEquals("4", result)
    }
}