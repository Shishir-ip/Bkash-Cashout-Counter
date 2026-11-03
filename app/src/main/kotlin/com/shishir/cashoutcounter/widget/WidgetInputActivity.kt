package com.shishir.cashoutcounter.widget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.view.HapticFeedbackConstants
import com.shishir.cashoutcounter.data.RateStore
import com.shishir.cashoutcounter.logic.AmountInput
import com.shishir.cashoutcounter.logic.CashoutCalculator
import com.shishir.cashoutcounter.logic.TakaFormatter
import com.shishir.cashoutcounter.ui.components.IosFilledButton
import com.shishir.cashoutcounter.ui.components.MoneyField
import com.shishir.cashoutcounter.ui.components.clickableNoIndication
import com.shishir.cashoutcounter.ui.theme.BkashPinkLight
import com.shishir.cashoutcounter.ui.theme.CardDark
import com.shishir.cashoutcounter.ui.theme.CardInner
import com.shishir.cashoutcounter.ui.theme.CashoutCounterTheme
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.LabelTertiary

/**
 * Translucent bottom-sheet style dialog: tap a widget, type an amount,
 * pick a rate, save — the widget updates instantly. No app navigation.
 */
class WidgetInputActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appWidgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        val mode = intent?.getStringExtra(WidgetContract.EXTRA_MODE) ?: WidgetContract.MODE_BALANCE

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val rates = RateStore(this).load()
        val saved = WidgetPrefs.load(this, appWidgetId)

        setContent {
            CashoutCounterTheme {
                WidgetInputSheet(
                    mode = mode,
                    rates = rates,
                    initialAmount = saved.amount,
                    initialRateId = saved.rateId,
                    onCancel = { finish() },
                    onSave = { amount, rateId ->
                        WidgetPrefs.save(this, appWidgetId, amount, rateId)
                        WidgetViews.updateOne(this, appWidgetId, mode)
                        finish()
                    },
                )
            }
        }
    }
}

@Composable
private fun WidgetInputSheet(
    mode: String,
    rates: List<com.shishir.cashoutcounter.model.ChargeRate>,
    initialAmount: String,
    initialRateId: String?,
    onCancel: () -> Unit,
    onSave: (amount: String, rateId: String) -> Unit,
) {
    var amount by remember { mutableStateOf(initialAmount) }
    var rateId by remember { mutableStateOf(initialRateId) }
    val selectedRate = rates.firstOrNull { it.id == rateId } ?: rates.firstOrNull()
    val view = LocalView.current

    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val previewText = if (amountValue > 0.0 && selectedRate != null) {
        if (mode == WidgetContract.MODE_BALANCE) {
            val r = CashoutCalculator.fromBalance(amountValue, selectedRate.rate)
            "You will receive ৳${TakaFormatter.format(r.received)}"
        } else {
            val r = CashoutCalculator.forAmount(amountValue, selectedRate.rate)
            "Total deduction ৳${TakaFormatter.format(r.totalDeduction)}"
        }
    } else {
        if (mode == WidgetContract.MODE_BALANCE) "You will receive ৳0.00" else "Total deduction ৳0.00"
    }

    fun cycleRate(direction: Int) {
        if (rates.isEmpty()) return
        val index = rates.indexOfFirst { it.id == rateId }.takeIf { it >= 0 } ?: 0
        val next = (index + direction + rates.size) % rates.size
        rateId = rates[next].id
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    fun quickAdd(value: Long) {
        val current = amount.toDoubleOrNull() ?: 0.0
        amount = AmountInput.sanitizeAmount((current + value).toString())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickableNoIndication(onCancel),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                .background(CardDark)
                .clickableNoIndication { /* absorb taps so the scrim doesn't dismiss */ }
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            Box(
                Modifier
                    .padding(top = 10.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF48484A))
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = if (mode == WidgetContract.MODE_BALANCE) "Balance Widget" else "Charge Widget",
                style = MaterialTheme.typography.titleLarge,
                color = LabelPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = previewText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = BkashPinkLight,
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "AMOUNT",
                style = MaterialTheme.typography.labelMedium,
                color = LabelTertiary,
            )
            Spacer(Modifier.height(6.dp))
            MoneyField(
                value = amount,
                onValueChange = { amount = AmountInput.sanitizeAmount(it) },
                placeholder = "Enter amount",
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = "CHARGE RATE",
                style = MaterialTheme.typography.labelMedium,
                color = LabelTertiary,
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardInner),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RateArrow(Icons.Filled.ChevronLeft) { cycleRate(-1) }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = selectedRate?.name ?: "No rates yet",
                        style = MaterialTheme.typography.titleSmall,
                        color = LabelPrimary,
                        maxLines = 1,
                    )
                    if (selectedRate != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${selectedRate.displayValue()} per 1,000",
                            style = MaterialTheme.typography.bodySmall,
                            color = LabelSecondary,
                        )
                    }
                }
                RateArrow(Icons.Filled.ChevronRight) { cycleRate(+1) }
            }

            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickChip("+100", Modifier.weight(1f)) { quickAdd(100) }
                QuickChip("+500", Modifier.weight(1f)) { quickAdd(500) }
                QuickChip("+1,000", Modifier.weight(1f)) { quickAdd(1_000) }
                QuickChip("Clear", Modifier.weight(1f)) { amount = "" }
            }

            Spacer(Modifier.height(18.dp))
            IosFilledButton(
                text = "Save to Widget",
                enabled = amountValue > 0.0 && selectedRate != null,
                onClick = { if (amountValue > 0.0 && selectedRate != null) onSave(amount, selectedRate.id) },
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RateArrow(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = LabelSecondary,
        )
    }
}

@Composable
private fun QuickChip(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CardInner)
            .clickableNoIndication(onClick)
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = LabelPrimary,
        )
    }
}
