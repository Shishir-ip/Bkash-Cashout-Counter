package com.shishir.cashoutcounter.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.shishir.cashoutcounter.R

/**
 * One-tap "add widget" via the system pin dialog (Android 8+).
 * Falls back to instructions on older versions.
 */
object WidgetPinner {

    fun requestPin(context: Context, mode: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            showFallbackToast(context)
            return
        }
        val manager = AppWidgetManager.getInstance(context)
        val provider = ComponentName(
            context,
            if (mode == WidgetContract.MODE_BALANCE) BalanceWidgetProvider::class.java
            else ChargeWidgetProvider::class.java
        )
        if (manager != null && manager.isRequestPinAppWidgetSupported) {
            manager.requestPinAppWidget(provider, null, null)
        } else {
            showFallbackToast(context)
        }
    }

    private fun showFallbackToast(context: Context) {
        Toast.makeText(
            context,
            "Long-press your home screen → Widgets → Cashout Counter",
            Toast.LENGTH_LONG,
        ).show()
    }
}
