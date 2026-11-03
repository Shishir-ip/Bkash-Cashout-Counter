package com.shishir.cashoutcounter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

/** "Balance → Cash" widget: how much cash you receive from your balance. */
class BalanceWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            WidgetViews.renderPublic(context, appWidgetManager, id, WidgetContract.MODE_BALANCE)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        WidgetPrefs.clear(context, appWidgetIds)
    }
}
