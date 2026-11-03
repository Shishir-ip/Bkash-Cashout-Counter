package com.shishir.cashoutcounter.model

import java.util.Locale

/**
 * A cash-out charge rate expressed in BDT per 1,000 withdrawn.
 * e.g. rate = 13.95 means 13.95৳ is charged for every 1,000৳ cashed out.
 */
data class ChargeRate(
    val id: String,
    val name: String,
    val rate: Double,
) {
    /** Rate rendered the way the web app shows it: whole numbers stay whole, e.g. "15৳" / "13.95৳". */
    fun displayValue(): String =
        if (rate == rate.toLong().toDouble()) "${rate.toLong()}৳"
        else String.format(Locale.US, "%.2f৳", rate)
}
