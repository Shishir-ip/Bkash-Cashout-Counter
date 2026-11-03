package com.shishir.cashoutcounter.logic

import org.junit.Assert.assertEquals
import org.junit.Test

class CashoutCalculatorTest {

    // Reference values ported from the original web app
    // https://github.com/Shishir-ip/Bkash-Cashout-Balance-Counter

    @Test
    fun `balance mode - 10000 at priyo ATM rate`() {
        val r = CashoutCalculator.fromBalance(10000.0, 13.95)
        assertEquals(139.50, r.charge, 1e-9)
        assertEquals(9860.50, r.received, 1e-9)
    }

    @Test
    fun `balance mode - 1000 at agent rate`() {
        val r = CashoutCalculator.fromBalance(1000.0, 18.50)
        assertEquals(18.50, r.charge, 1e-9)
        assertEquals(981.50, r.received, 1e-9)
    }

    @Test
    fun `charge mode - 1000 at agent rate`() {
        val r = CashoutCalculator.forAmount(1000.0, 18.50)
        assertEquals(18.50, r.charge, 1e-9)
        assertEquals(1018.50, r.totalDeduction, 1e-9)
    }

    @Test
    fun `charge mode - 500 at nagad islamic rate`() {
        val r = CashoutCalculator.forAmount(500.0, 15.0)
        assertEquals(7.50, r.charge, 1e-9)
        assertEquals(507.50, r.totalDeduction, 1e-9)
    }

    @Test
    fun `charge mode - decimal amount at nagad regular rate`() {
        val r = CashoutCalculator.forAmount(250.25, 12.99)
        assertEquals(3.2507475, r.charge, 1e-7)
        assertEquals(253.5007475, r.totalDeduction, 1e-7)
    }
}
