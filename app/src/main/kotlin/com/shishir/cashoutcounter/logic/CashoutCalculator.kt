package com.shishir.cashoutcounter.logic

import kotlin.math.roundToLong

/**
 * Pure calculation logic, ported 1:1 from the original
 * [Bkash Cashout Balance Counter web app](https://github.com/Shishir-ip/Bkash-Cashout-Balance-Counter).
 */
object CashoutCalculator {

    /** Charges are expressed per 1,000৳. */
    const val RATE_UNIT = 1_000.0

    /** Result for the "Balance" calculator. */
    data class BalanceResult(
        val balance: Double,
        val charge: Double,
        val received: Double,
    )

    /** Result for the "Charge" calculator. */
    data class ChargeResult(
        val amount: Double,
        val charge: Double,
        val totalDeduction: Double,
    )

    /**
     * "I have this much balance — how much cash do I get after the charge?"
     * charge   = (balance / 1000) × rate
     * received = balance − charge
     */
    fun fromBalance(balance: Double, ratePer1000: Double): BalanceResult {
        val charge = balance / RATE_UNIT * ratePer1000
        return BalanceResult(balance = balance, charge = charge, received = balance - charge)
    }

    /**
     * "I want to hand over this much cash — what is deducted from my account?"
     * charge = (amount / 1000) × rate
     * total  = amount + charge
     */
    fun forAmount(amount: Double, ratePer1000: Double): ChargeResult {
        val charge = amount / RATE_UNIT * ratePer1000
        return ChargeResult(amount = amount, charge = charge, totalDeduction = amount + charge)
    }
}

/**
 * Formats money the way Bangladesh reads it: lakh / crore grouping with 2 decimals,
 * e.g. 1234567.891 → "12,34,567.89" (matches the web app's en-BD locale output).
 */
object TakaFormatter {

    fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return format(0.0)
        val bd = java.math.BigDecimal.valueOf(value)
            .setScale(2, java.math.RoundingMode.HALF_UP)
            .abs()
        val negative = value < 0
        val whole = bd.toBigInteger().toString()
        val fraction = bd.toPlainString().substringAfter('.', "00").padEnd(2, '0')
        val grouped = groupIndian(whole)
        val sign = if (negative) "-" else ""
        return "$sign$grouped.$fraction"
    }

    /** Groups digits Indian style: last group of 3, every earlier group of 2. */
    internal fun groupIndian(digits: String): String {
        if (digits.length <= 3) return digits
        val head = digits.dropLast(3)
        val tail = digits.takeLast(3)
        val groups = mutableListOf<String>()
        var index = head.length
        while (index > 2) {
            groups.add(head.substring(index - 2, index))
            index -= 2
        }
        if (index > 0) groups.add(head.substring(0, index))
        return groups.asReversed().joinToString(",") + "," + tail
    }
}
