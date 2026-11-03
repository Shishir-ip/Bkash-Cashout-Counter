package com.shishir.cashoutcounter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shishir.cashoutcounter.logic.AmountInput
import com.shishir.cashoutcounter.model.ChargeRate
import com.shishir.cashoutcounter.ui.components.CardDivider
import com.shishir.cashoutcounter.ui.components.CupertinoDialog
import com.shishir.cashoutcounter.ui.components.IosCard
import com.shishir.cashoutcounter.ui.components.IosFilledButton
import com.shishir.cashoutcounter.ui.components.MoneyField
import com.shishir.cashoutcounter.ui.components.SimpleField
import com.shishir.cashoutcounter.ui.components.clickableNoIndication
import com.shishir.cashoutcounter.ui.theme.AppBackground
import com.shishir.cashoutcounter.ui.theme.BkashPink
import com.shishir.cashoutcounter.ui.theme.CardBorder
import com.shishir.cashoutcounter.ui.theme.CardDark
import com.shishir.cashoutcounter.ui.theme.DestructiveRed
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.LabelTertiary

private sealed interface RateSheetTarget {
    data object New : RateSheetTarget
    data class Edit(val rate: ChargeRate) : RateSheetTarget
}

@Composable
fun RatesScreen(
    rates: List<ChargeRate>,
    onAddRate: (name: String, rate: Double) -> Unit,
    onUpdateRate: (id: String, name: String, rate: Double) -> Unit,
    onDeleteRate: (id: String) -> Unit,
    onResetToDefaults: () -> Unit,
) {
    var sheetTarget by remember { mutableStateOf<RateSheetTarget?>(null) }
    var pendingDelete by remember { mutableStateOf<ChargeRate?>(null) }
    var confirmReset by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Large title header with Add action ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Rates",
                    style = MaterialTheme.typography.displayMedium,
                    color = LabelPrimary,
                )
                Text(
                    text = "Used by both calculators",
                    style = MaterialTheme.typography.bodySmall,
                    color = LabelSecondary,
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(BkashPink.copy(alpha = 0.16f))
                    .clickableNoIndication { sheetTarget = RateSheetTarget.New }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = BkashPink,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.titleSmall,
                    color = BkashPink,
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // ── Grouped list of rates ──
        IosCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
            if (rates.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "No rates yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = LabelPrimary,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Tap Add to create your first rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = LabelTertiary,
                    )
                }
            } else {
                rates.forEachIndexed { index, rate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableNoIndication { sheetTarget = RateSheetTarget.Edit(rate) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = rate.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = LabelPrimary,
                                maxLines = 1,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "charge per ৳1,000 cash out",
                                style = MaterialTheme.typography.bodySmall,
                                color = LabelTertiary,
                            )
                        }
                        Text(
                            text = rate.displayValue(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BkashPink,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Edit ${rate.name}",
                            tint = LabelTertiary,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    if (index < rates.lastIndex) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = CardBorder,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Reset to defaults ──
        IosCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { confirmReset = true }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Reset to Defaults",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DestructiveRed,
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        Text(
            text = "Rates are saved on this device automatically.",
            style = MaterialTheme.typography.bodySmall,
            color = LabelTertiary,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Spacer(Modifier.height(24.dp))
    }

    // ── Add / edit bottom sheet ──
    sheetTarget?.let { target ->
        RateEditSheet(
            target = target,
            onDismiss = { sheetTarget = null },
            onSave = { name, rate ->
                when (target) {
                    is RateSheetTarget.New -> onAddRate(name, rate)
                    is RateSheetTarget.Edit -> onUpdateRate(target.rate.id, name, rate)
                }
                sheetTarget = null
            },
            onDelete = {
                pendingDelete = (target as RateSheetTarget.Edit).rate
                sheetTarget = null
            },
        )
    }

    // ── Delete confirmation (iOS alert) ──
    pendingDelete?.let { rate ->
        CupertinoDialog(
            title = "Delete Rate",
            message = "Delete \"${rate.name}\"? This cannot be undone.",
            confirmText = "Delete",
            destructive = true,
            onConfirm = {
                onDeleteRate(rate.id)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }

    if (confirmReset) {
        CupertinoDialog(
            title = "Reset to Defaults",
            message = "All your custom rates will be replaced with the four default rates.",
            confirmText = "Reset",
            destructive = true,
            onConfirm = {
                onResetToDefaults()
                confirmReset = false
            },
            onDismiss = { confirmReset = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RateEditSheet(
    target: RateSheetTarget,
    onDismiss: () -> Unit,
    onSave: (name: String, rate: Double) -> Unit,
    onDelete: () -> Unit,
) {
    val isEdit = target is RateSheetTarget.Edit
    var name by remember(target) {
        mutableStateOf((target as? RateSheetTarget.Edit)?.rate?.name ?: "")
    }
    var rateText by remember(target) {
        mutableStateOf(
            (target as? RateSheetTarget.Edit)?.rate?.rate?.let { AmountInput.rateToString(it) } ?: ""
        )
    }

    val rateValue = rateText.toDoubleOrNull()
    val valid = name.isNotBlank() && rateValue != null && rateValue > 0.0 && rateValue <= 9999.99
    val touched = name.isNotEmpty() || rateText.isNotEmpty()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardDark,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            // grabber
            Box(
                Modifier
                    .padding(top = 10.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF48484A))
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(18.dp))
            Text(
                text = if (isEdit) "Edit Rate" else "Add Rate",
                style = MaterialTheme.typography.titleLarge,
                color = LabelPrimary,
            )
            Spacer(Modifier.height(18.dp))

            Text(
                text = "NAME",
                style = MaterialTheme.typography.labelMedium,
                color = LabelTertiary,
            )
            Spacer(Modifier.height(6.dp))
            SimpleField(
                value = name,
                onValueChange = { if (it.length <= 40) name = it },
                placeholder = "e.g. bKash ATM, Bank App",
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "CHARGE RATE (৳ PER 1,000)",
                style = MaterialTheme.typography.labelMedium,
                color = LabelTertiary,
            )
            Spacer(Modifier.height(6.dp))
            MoneyField(
                value = rateText,
                onValueChange = { rateText = AmountInput.sanitizeAmount(it) },
                placeholder = "13.95",
            )

            if (!valid && touched) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Enter a name and a rate greater than 0",
                    style = MaterialTheme.typography.bodySmall,
                    color = DestructiveRed,
                )
            }

            Spacer(Modifier.height(20.dp))
            IosFilledButton(
                text = if (isEdit) "Save Changes" else "Add Rate",
                enabled = valid,
                onClick = { if (valid) onSave(name, rateValue!!) },
            )

            if (isEdit) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x1FFF453A))
                        .clickableNoIndication(onDelete)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Delete Rate",
                        style = MaterialTheme.typography.titleMedium,
                        color = DestructiveRed,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
