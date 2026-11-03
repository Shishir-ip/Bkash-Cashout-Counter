package com.shishir.cashoutcounter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * The app intentionally ships a single, always-dark theme so the
 * bKash-pink + near-black branding stays consistent, like the web app.
 */
private val CashoutColorScheme = darkColorScheme(
    primary = BkashPink,
    onPrimary = LabelPrimary,
    primaryContainer = BkashPinkDark,
    onPrimaryContainer = LabelPrimary,
    secondary = BkashPinkLight,
    onSecondary = LabelPrimary,
    background = AppBackground,
    onBackground = LabelPrimary,
    surface = CardDark,
    onSurface = LabelPrimary,
    surfaceVariant = CardInner,
    onSurfaceVariant = LabelSecondary,
    outline = CardBorder,
    outlineVariant = CardBorder,
    error = DestructiveRed,
    onError = LabelPrimary,
    surfaceContainer = CardDark,
    surfaceContainerHigh = CardInner,
    surfaceContainerHighest = CardInner,
)

@Composable
fun CashoutCounterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CashoutColorScheme,
        typography = AppTypography,
        content = content,
    )
}
