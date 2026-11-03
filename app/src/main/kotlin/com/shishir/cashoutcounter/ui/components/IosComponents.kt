package com.shishir.cashoutcounter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shishir.cashoutcounter.ui.theme.BkashPink
import com.shishir.cashoutcounter.ui.theme.BkashPinkDark
import com.shishir.cashoutcounter.ui.theme.BkashPinkLight
import com.shishir.cashoutcounter.ui.theme.CardBorder
import com.shishir.cashoutcounter.ui.theme.CardDark
import com.shashir.cashoutcounter.ui.theme.CardInner
import com.shishir.cashoutcounter.ui.theme.DestructiveRed
import com.shishir.cashoutcounter.ui.theme.InterFamily
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.LabelTertiary
import com.shishir.cashoutcounter.ui.theme.TabBarDark

/** Clickable without the Material ripple — iOS taps don't ripple. */
fun Modifier.clickableNoIndication(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}

/** Inset grouped card, the backbone of iOS lists. */
@Composable
fun IosCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .padding(contentPadding),
        content = content,
    )
}

/** "emoji + label" section header, matching the web app's section titles. */
@Composable
fun SectionLabel(emoji: String, title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = emoji, fontSize = 15.sp)
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = LabelPrimary,
        )
    }
}

/** iOS segmented control (Cupertino SlidingSegmentedControl look). */
@Composable
fun IosSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.20f))
            .padding(3.dp),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) Color.White else Color.Transparent)
                    .clickableNoIndication { onSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selected) BkashPink else LabelPrimary.copy(alpha = 0.92f),
                )
            }
        }
    }
}

/** Money input styled like an iOS filled text field, with the ৳ prefix. */
@Composable
fun MoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardInner)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "৳",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BkashPink,
        )
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 18.sp,
                    fontFamily = InterFamily,
                    color = LabelTertiary,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = LabelPrimary,
                ),
                cursorBrush = SolidColor(BkashPink),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
            )
        }
    }
}

/** Plain single-line field used in the rate editor sheet. */
@Composable
fun SimpleField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardInner)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (prefix != null) {
            Text(
                text = prefix,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = BkashPink,
            )
            Spacer(Modifier.width(8.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    fontFamily = InterFamily,
                    color = LabelTertiary,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = LabelPrimary,
                ),
                cursorBrush = SolidColor(BkashPink),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done,
                ),
            )
        }
    }
}

/** Pink gradient CTA button with a subtle press-scale, iOS-like. */
@Composable
fun IosFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 90),
        label = "buttonScale",
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (enabled) Brush.verticalGradient(listOf(BkashPinkLight, BkashPinkDark))
                else CardInner
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) LabelPrimary else LabelTertiary,
        )
    }
}

/** iOS alert (Cupertino-style dialog) for destructive confirmations. */
@Composable
fun CupertinoDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    destructive: Boolean = false,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 44.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2C2C2E)),
        ) {
            Spacer(Modifier.height(18.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = LabelSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(18.dp))
            HorizontalDivider(thickness = 0.5.dp, color = CardBorder)
            Row(modifier = Modifier.height(46.dp)) {
                DialogAction(
                    label = "Cancel",
                    color = LabelPrimary,
                    bold = false,
                    onDismiss,
                )
                VerticalDivider(thickness = 0.5.dp, color = CardBorder)
                DialogAction(
                    label = confirmText,
                    color = if (destructive) DestructiveRed else BkashPink,
                    bold = true,
                    onConfirm,
                )
            }
        }
    }
}

@Composable
private fun RowScope.DialogAction(label: String, color: Color, bold: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clickableNoIndication(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = InterFamily,
            color = color,
        )
    }
}

/** Hairline divider used inside dark cards. */
@Composable
fun CardDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier, thickness = 0.5.dp, color = CardBorder)
}

/** Pink pill badge, e.g. "13.95৳ / 1000". */
@Composable
fun RateBadge(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BkashPink.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = BkashPinkLight,
        )
    }
}

/** A small circular icon button used on cards (e.g. copy). */
@Composable
fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = LabelSecondary,
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(CardInner)
            .clickableNoIndication(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(18.dp))
    }
}
