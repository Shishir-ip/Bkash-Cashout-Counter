package com.shishir.cashoutcounter.widget

import android.content.Context

/** Shared constants for the two home-screen widgets. */
object WidgetContract {
    const val EXTRA_MODE = "com.shishir.cashoutcounter.widget.MODE"
    const val MODE_BALANCE = "balance"
    const val MODE_CHARGE = "charge"
}

/**
 * Persists each widget instance's amount and selected rate separately,
 * so two widgets on the same home screen can track different calculations.
 */
object WidgetPrefs {

    private const val PREFS_NAME = "cashout_widget_prefs"

    data class State(val amount: String, val rateId: String?)

    fun load(context: Context, appWidgetId: Int): State {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return State(
            amount = prefs.getString(amountKey(appWidgetId), "") ?: "",
            rateId = prefs.getString(rateKey(appWidgetId), null),
        )
    }

    fun save(context: Context, appWidgetId: Int, amount: String, rateId: String?) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(amountKey(appWidgetId), amount)
            .putString(rateKey(appWidgetId), rateId)
            .apply()
    }

    fun clear(context: Context, appWidgetIds: IntArray) {
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        appWidgetIds.forEach { id ->
            editor.remove(amountKey(id)).remove(rateKey(id))
        }
        editor.apply()
    }

    private fun amountKey(appWidgetId: Int) = "widget_${appWidgetId}_amount"
    private fun rateKey(appWidgetId: Int) = "widget_${appWidgetId}_rate"
}
