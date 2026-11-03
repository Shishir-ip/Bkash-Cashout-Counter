package com.shishir.cashoutcounter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.shishir.cashoutcounter.R

/**
 * Inter reads very close to SF Pro, giving the UI its iOS feel.
 * The Bangladeshi taka sign (৳) automatically falls back to the
 * system Bengali font (Noto Sans Bengali) on the device.
 */
val InterFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

val AppTypography = Typography(
    // iOS "Large Title" — screen headers
    displayLarge = baseline(34.sp, FontWeight.Bold),
    displayMedium = baseline(28.sp, FontWeight.Bold),
    // iOS "Title 1/2"
    headlineLarge = baseline(26.sp, FontWeight.Bold),
    headlineMedium = baseline(22.sp, FontWeight.SemiBold),
    titleLarge = baseline(20.sp, FontWeight.SemiBold),
    titleMedium = baseline(17.sp, FontWeight.SemiBold), // iOS body-semibold
    titleSmall = baseline(15.sp, FontWeight.SemiBold),
    // iOS body / callout
    bodyLarge = baseline(17.sp, FontWeight.Normal),
    bodyMedium = baseline(15.sp, FontWeight.Normal),
    bodySmall = baseline(13.sp, FontWeight.Normal),
    // iOS footnote / caption
    labelLarge = baseline(15.sp, FontWeight.Medium),
    labelMedium = baseline(12.sp, FontWeight.Medium),
    labelSmall = baseline(11.sp, FontWeight.Medium),
)

private fun baseline(size: TextUnit, weight: FontWeight) = TextStyle(
    fontFamily = InterFamily,
    fontWeight = weight,
    fontSize = size,
    lineHeight = TextUnit(size.value * 1.35f, TextUnitType.Sp),
)
