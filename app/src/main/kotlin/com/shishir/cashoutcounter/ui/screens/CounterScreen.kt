package com.shishir.cashoutcounter.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Copy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishir.cashoutcounter.logic.AmountInput
import com.shishir.cashoutcounter.logic.CashoutCalculator
import com.shishir.cashoutcounter.logic.TakaFormatter
import com.shishir.cashoutcounter.model.ChargeRate
import com.shishir.cashoutcounter.ui.components.CardDivider
import com.shishir.cashoutcounter.ui.components.IosCard
import com.shishir.cashoutcounter.ui.components.IosSegmentedControl
import com.shishir.cashoutcounter.ui.components.MoneyField
import com.shishir.cashoutcounter.ui.components.RateBadge
import com.shishir.cashoutcounter.ui.components.RoundIconButton
import com.shishir.cashoutcounter.ui.components.SectionLabel
import com.shishir.cashoutcounter.ui.components.clickableNoIndication
import com.shishir.cashoutcounter.ui.theme.AppBackground
import com.shishir.cashoutcounter.ui.theme.BkashPink
import com.shishir.cashoutcounter.ui.theme.BkashPinkLight
import com.shishir.cashoutcounter.ui.theme.CardBorder
import com.shishir.cashoutcounter.ui.theme.CardDark
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.LabelTertiary
import kotlinx.coroutines.delay

private val HeaderGradient = Brush.verticalGradient(
    listOf(Color(0xFFE01A5C), Color(0xFFC41848))
)

private const val MODE_BALANCE = 0
private const val MODE_CHARGE = 1

@Composable
fun CounterScreen(
    rates: List<ChargeRate>,
    onOpenRates: () -> Unit,
) {
    var mode by rememberSaveable { mutableIntStateOf(MODE_BALANCE) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var selectedRateId by rememberSaveable { mutableStateOf<String?>(null) }

    // Keep the selection valid while rates are added / edited / deleted.
    LaunchedEffect(rates) {
        if (rates.none { it.id == selectedRateId }) {
            selectedRateId = rates.firstOrNull()?.id
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .imePadding(),
    ) {
        // ── Pink header with the segmented control, like the web app ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HeaderGradient)
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Cashout Counter",
                style = MaterialTheme.typography.displayMedium,
                color = LabelPrimary,
            )
            Text(
                text = "Calculate bKash Cash Out Charges",
                style = MaterialTheme.typography.bodySmall,
                color = LabelPrimary.copy(alpha = 0.85f),
            )
            Spacer(Modifier.height(16.dp))
            IosSegmentedControl(
                options = listOf("Balance", "Charge"),
                selectedIndex = mode,
                onSelected = { mode = it },
            )
            Spacer(Modifier.height(16.dp))
        }

        // ── Scrollable calculator body ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .imePadding(),
        ) {
            Spacer(Modifier.height(18.dp))

            SectionLabel(
                emoji = "💰",
                title = if (mode == MODE_BALANCE) "Your Balance" else "Cash Out Amount",
            )
            Spacer(Modifier.height(8.dp))
            MoneyField(
                value = amountText,
                onValueChange = { amountText = AmountInput.sanitizeAmount(it) },
                placeholder = "Enter amount",
            )

            Spacer(Modifier.height(20.dp))
            SectionLabel(emoji = "⚡", title = "Select Charge Rate")
            Spacer(Modifier.height(10.dp))
            RateGrid(
                rates = rates,
                selectedRateId = selectedRateId,
                onSelectRate = { selectedRateId = it },
                onAddRate = onOpenRates,
            )

            Spacer(Modifier.height(20.dp))
            ResultCard(
                mode = mode,
                amountText = amountText,
                rates = rates,
                selectedRateId = selectedRateId,
            )

            Spacer(Modifier.height(14.dp))
            QuickReferenceCard(rates)

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun RateGrid(
    rates: List<ChargeRate>,
    selectedRateId: String?,
    onSelectRate: (String) -> Unit,
    onAddRate: () -> Unit,
) {
    val view = LocalView.current
    val rows = rates.chunked(2)
    rows.forEachIndexed { rowIndex, rowRates ->
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            rowRates.forEach { rate ->
                RateChip(
                    rate = rate,
                    selected = rate.id == selectedRateId,
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                        onSelectRate(rate.id)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            if (rowRates.size == 1) Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
    }
    // "Add Rate" ghost chip, half width like the web app — jumps to the Rates tab.
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AddRateChip(modifier = Modifier.weight(1f), onClick = onAddRate)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun RateChip(
    rate: ChargeRate,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) BkashPink.copy(alpha = 0.14f) else CardDark)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                brush = SolidColor(if (selected) BkashPink else CardBorder),
                shape = RoundedCornerShape(14.dp),
            )
            .clickableNoIndication(onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = rate.displayValue(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LabelPrimary,
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = rate.name,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) BkashPinkLight else LabelSecondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun AddRateChip(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
            .clickableNoIndication(onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add rate",
            tint = BkashPinkLight,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Add Rate",
            style = MaterialTheme.typography.titleSmall,
            color = BkashPinkLight,
        )
    }
}

@Composable
private fun ResultCard(
    mode: Int,
    amountText: String,
    rates: List<ChargeRate>,
    selectedRateId: String?,
) {
    val clipboard = LocalClipboardManager.current
    var copied by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(1400)
            copied = false
        }
    }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val selectedRate = rates.firstOrNull { it.id == selectedRateId }
    val valid = amount > 0.0 && selectedRate != null

    val balanceResult = if (valid && mode == MODE_BALANCE) {
        CashoutCalculator.fromBalance(amount, selectedRate!!.rate)
    } else null
    val chargeResult = if (valid && mode == MODE_CHARGE) {
        CashoutCalculator.forAmount(amount, selectedRate!!.rate)
    } else null

    val headlineLabel: String
    val headline: String
    val summary: String
    if (mode == MODE_BALANCE) {
        val r = balanceResult
        headlineLabel = "You will receive"
        headline = if (r == null) "৳0.00" else "৳" + TakaFormatter.format(r.received)
        summary = if (r == null) "" else buildString {
            appendLine("Cashout Counter")
            appendLine("Balance: ৳${TakaFormatter.format(r.balance)}")
            appendLine("Charge rate: ${selectedRate?.displayValue()}/1000")
            appendLine("Charge: ৳${TakaFormatter.format(r.charge)}")
            append("You receive: ৳${TakaFormatter.format(r.received)}")
        }
    } else {
        val r = chargeResult
        headlineLabel = "Total deduction from balance"
        headline = if (r == null) "৳0.00" else "৳" + TakaFormatter.format(r.totalDeduction)
        summary = if (r == null) "" else buildString {
            appendLine("Cashout Counter")
            appendLine("Cash out: ৳${TakaFormatter.format(r.amount)}")
            appendLine("Charge rate: ${selectedRate?.displayValue()}/1000")
            appendLine("Charge: ৳${TakaFormatter.format(r.charge)}")
            append("Total deduction: ৳${TakaFormatter.format(r.totalDeduction)}")
        }
    }

    IosCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(emoji = "🧾", title = "Result")
            Spacer(Modifier.weight(1f))
            RoundIconButton(
                icon = if (copied) Icons.Filled.Check else Icons.Filled.Copy,
                contentDescription = if (copied) "Copied" else "Copy result",
                tint = if (copied) Color(0xFF30D158) else LabelSecondary,
                onClick = {
                    if (valid) {
                        clipboard.setText(AnnotatedString(summary))
                        copied = true
                    }
                },
            )
        }

        Spacer(Modifier.height(14.dp))
        Text(
            text = headlineLabel,
            style = MaterialTheme.typography.bodySmall,
            color = LabelSecondary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = headline,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (valid) LabelPrimary else LabelTertiary,
        )

        Spacer(Modifier.height(16.dp))
        CardDivider()

        Spacer(Modifier.height(12.dp))
        BreakdownRow(
            label = if (mode == MODE_BALANCE) "Balance" else "Cash Out Amount",
            value = "৳" + TakaFormatter.format(amount),
        )
        Spacer(Modifier.height(10.dp))
        BreakdownRow(
            label = "Charge Rate",
            value = null,
            badge = "${selectedRate?.displayValue() ?: "0৳"} / 1000",
        )
        Spacer(Modifier.height(10.dp))
        val charge = balanceResult?.charge ?: chargeResult?.charge ?: 0.0
        BreakdownRow(
            label = if (mode == MODE_BALANCE) "Deducted Charge" else "Charge",
            value = "৳" + TakaFormatter.format(charge),
        )
    }
}

@Composable
private fun BreakdownRow(label: String, value: String?, badge: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = LabelSecondary,
            modifier = Modifier.weight(1f),
        )
        if (badge != null) RateBadge(text = badge)
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = LabelPrimary,
            )
        }
    }
}

@Composable
private fun QuickReferenceCard(rates: List<ChargeRate>) {
    IosCard {
        SectionLabel(emoji = "ℹ️", title = "Quick Reference")
        Spacer(Modifier.height(12.dp))
        rates.forEach { rate ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = rate.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LabelSecondary,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "${rate.displayValue()} / 1000",
                    style = MaterialTheme.typography.titleSmall,
                    color = LabelPrimary,
                )
            }
            Spacer(Modifier.height(10.dp))
        }
        Text(
            text = "Edit or add rates in the Rates tab.",
            style = MaterialTheme.typography.bodySmall,
            color = LabelTertiary,
        )
    }
}
