package com.shishir.cashoutcounter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

/** "Cash Out → Total" widget: total deduction for the amount you want to cash out. */
class ChargeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            WidgetViews.renderPublic(context, appWidgetManager, id, WidgetContract.MODE_CHARGE)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        WidgetPrefs.clear(context, appWidgetIds)
    }
}
