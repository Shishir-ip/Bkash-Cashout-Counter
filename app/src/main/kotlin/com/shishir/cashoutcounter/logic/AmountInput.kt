package com.shishir.cashoutcounter.logic

import java.util.Locale

/**
 * Shared helpers for the money/rate text fields.
 */
object AmountInput {

    /**
     * Keeps a money input clean: digits and a single dot, at most 2 decimals,
     * no leading zeros like "012".
     */
    fun sanitizeAmount(input: String): String {
        var cleaned = input.filter { it.isDigit() || it == '.' }
        val firstDot = cleaned.indexOf('.')
        if (firstDot != -1) {
            cleaned = cleaned.substring(0, firstDot + 1) +
                cleaned.substring(firstDot + 1).replace(".", "")
        }
        val dot = cleaned.indexOf('.')
        if (dot != -1 && cleaned.length - dot - 1 > 2) {
            cleaned = cleaned.substring(0, dot + 3)
        }
        if (cleaned.length > 1 && cleaned[0] == '0' && cleaned[1] != '.') {
            cleaned = cleaned.substring(1)
        }
        return cleaned.take(14)
    }

    /** Renders a rate for editing: 15.0 → "15", 13.95 → "13.95". */
    fun rateToString(rate: Double): String =
        if (rate == rate.toLong().toDouble()) rate.toLong().toString()
        else String.format(Locale.US, "%.2f", rate)
}
