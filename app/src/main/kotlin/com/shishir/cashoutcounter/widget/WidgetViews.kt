package com.shishir.cashoutcounter.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.shishir.cashoutcounter.R
import com.shishir.cashoutcounter.data.RateStore
import com.shishir.cashoutcounter.logic.CashoutCalculator
import com.shishir.cashoutcounter.logic.TakaFormatter

/**
 * Renders widget RemoteViews from the user's saved amount + the current rate list.
 * Both widgets share this single rendering path so they always look consistent.
 */
object WidgetViews {

    fun updateOne(context: Context, appWidgetId: Int, mode: String) {
        val manager = AppWidgetManager.getInstance(context) ?: return
        render(context, manager, appWidgetId, mode)
    }

    /** Entry point used by the providers on system update broadcasts. */
    fun renderPublic(context: Context, manager: AppWidgetManager, appWidgetId: Int, mode: String) {
        render(context, manager, appWidgetId, mode)
    }

    /** Refreshes every Balance and Charge widget (e.g. after rates change in the app). */
    fun updateAll(context: Context) {
        val manager = AppWidgetManager.getInstance(context) ?: return
        listOf(
            ComponentName(context, BalanceWidgetProvider::class.java) to WidgetContract.MODE_BALANCE,
            ComponentName(context, ChargeWidgetProvider::class.java) to WidgetContract.MODE_CHARGE,
        ).forEach { (component, mode) ->
            manager.getAppWidgetIds(component).forEach { id ->
                render(context, manager, id, mode)
            }
        }
    }

    private fun render(
        context: Context,
        manager: AppWidgetManager,
        appWidgetId: Int,
        mode: String,
    ) {
        val state = WidgetPrefs.load(context, appWidgetId)
        val rates = RateStore(context).load()
        val rate = rates.firstOrNull { it.id == state.rateId } ?: rates.firstOrNull()
        val amount = state.amount.toDoubleOrNull() ?: 0.0
        val valid = amount > 0.0 && rate != null

        val layout = if (mode == WidgetContract.MODE_BALANCE) {
            R.layout.widget_balance
        } else {
            R.layout.widget_charge
        }
        val views = RemoteViews(context.packageName, layout)

        if (valid && rate != null) {
            if (mode == WidgetContract.MODE_BALANCE) {
                val result = CashoutCalculator.fromBalance(amount, rate.rate)
                views.setTextViewText(
                    R.id.widget_result,
                    "৳" + TakaFormatter.format(result.received),
                )
                views.setTextViewText(
                    R.id.widget_footer,
                    "Balance ৳${TakaFormatter.format(amount)} · ${rate.displayValue()}/1000",
                )
            } else {
                val result = CashoutCalculator.forAmount(amount, rate.rate)
                views.setTextViewText(
                    R.id.widget_result,
                    "৳" + TakaFormatter.format(result.totalDeduction),
                )
                views.setTextViewText(
                    R.id.widget_footer,
                    "Cash out ৳${TakaFormatter.format(amount)} · ${rate.displayValue()}/1000 · charge ৳${TakaFormatter.format(result.charge)}",
                )
            }
        } else if (rates.isEmpty()) {
            views.setTextViewText(R.id.widget_result, "৳0.00")
            views.setTextViewText(R.id.widget_footer, "Add a rate in the app first")
        } else {
            views.setTextViewText(R.id.widget_result, "৳0.00")
            views.setTextViewText(R.id.widget_footer, "Tap to enter amount")
        }

        val pending = inputPendingIntent(context, appWidgetId, mode)
        views.setOnClickPendingIntent(R.id.widget_root, pending)
        views.setOnClickPendingIntent(R.id.widget_edit, pending)

        manager.updateAppWidget(appWidgetId, views)
    }

    private fun inputPendingIntent(context: Context, appWidgetId: Int, mode: String): PendingIntent {
        val intent = Intent(context, WidgetInputActivity::class.java)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            .putExtra(WidgetContract.EXTRA_MODE, mode)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // requestCode keyed by widget id keeps each widget's PendingIntent distinct
        return PendingIntent.getActivity(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
