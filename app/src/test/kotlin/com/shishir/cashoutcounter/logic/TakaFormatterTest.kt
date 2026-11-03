package com.shishir.cashoutcounter.logic

import org.junit.Assert.assertEquals
import org.junit.Test

class TakaFormatterTest {

    @Test
    fun `zero`() {
        assertEquals("0.00", TakaFormatter.format(0.0))
    }

    @Test
    fun `plain thousands use standard grouping`() {
        assertEquals("1,000.00", TakaFormatter.format(1000.0))
        assertEquals("9,860.50", TakaFormatter.format(9860.5))
    }

    @Test
    fun `lakh uses indian grouping`() {
        assertEquals("1,00,000.00", TakaFormatter.format(100000.0))
        assertEquals("12,34,567.89", TakaFormatter.format(1234567.891))
    }

    @Test
    fun `crore keeps stacking two-digit groups`() {
        assertEquals("1,00,00,000.00", TakaFormatter.format(10000000.0))
    }

    @Test
    fun `rounds half up to two decimals`() {
        assertEquals("18.50", TakaFormatter.format(18.5))
        assertEquals("18.35", TakaFormatter.format(18.345))
        assertEquals("139.50", TakaFormatter.format(139.496))
    }

    @Test
    fun `negative amounts keep sign`() {
        assertEquals("-500.00", TakaFormatter.format(-500.0))
    }

    @Test
    fun `small amounts`() {
        assertEquals("0.50", TakaFormatter.format(0.5))
        assertEquals("0.05", TakaFormatter.format(0.05))
    }

    @Test
    fun `indian grouping helper`() {
        assertEquals("123", TakaFormatter.groupIndian("123"))
        assertEquals("1,234", TakaFormatter.groupIndian("1234"))
        assertEquals("12,34,567", TakaFormatter.groupIndian("1234567"))
    }
}
