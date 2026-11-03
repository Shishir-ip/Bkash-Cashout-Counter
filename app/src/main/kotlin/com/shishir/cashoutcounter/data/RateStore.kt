package com.shishir.cashoutcounter.data

import android.content.Context
import com.shishir.cashoutcounter.model.ChargeRate
import org.json.JSONArray
import org.json.JSONObject

/**
 * Persists the user's charge rates as JSON in SharedPreferences.
 * Ships with the four rates from the original web app.
 */
class RateStore(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<ChargeRate> {
        val raw = prefs.getString(KEY_RATES, null) ?: return defaults()
        return runCatching { parse(raw) }.getOrDefault(defaults())
    }

    fun save(rates: List<ChargeRate>) {
        val array = JSONArray()
        rates.forEach { rate ->
            array.put(
                JSONObject()
                    .put("id", rate.id)
                    .put("name", rate.name)
                    .put("rate", rate.rate)
            )
        }
        prefs.edit().putString(KEY_RATES, array.toString()).apply()
    }

    private fun parse(raw: String): List<ChargeRate> {
        val array = JSONArray(raw)
        return (0 until array.length())
            .mapNotNull { i ->
                val obj = array.optJSONObject(i) ?: return@mapNotNull null
                val rate = obj.optDouble("rate", -1.0)
                if (obj.optString("name").isBlank() || rate <= 0.0) return@mapNotNull null
                ChargeRate(
                    id = obj.optString("id", "rate_${System.nanoTime()}"),
                    name = obj.getString("name"),
                    rate = rate
                )
            }
    }

    companion object {
        private const val PREFS_NAME = "cashout_counter_prefs"
        private const val KEY_RATES = "charge_rates"

        fun defaults(): List<ChargeRate> = listOf(
            ChargeRate(id = "bkash_atm", name = "bKash Priyo / ATM", rate = 13.95),
            ChargeRate(id = "bkash_agent", name = "bKash Regular (Agent)", rate = 18.50),
            ChargeRate(id = "nagad_regular", name = "Nagad Regular", rate = 12.99),
            ChargeRate(id = "nagad_islamic", name = "Nagad Islamic", rate = 15.00),
        )
    }
}
