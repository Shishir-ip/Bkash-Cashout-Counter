package com.shishir.cashoutcounter.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shishir.cashoutcounter.R
import com.shishir.cashoutcounter.ui.components.CardDivider
import com.shishir.cashoutcounter.ui.components.IosCard
import com.shishir.cashoutcounter.ui.components.clickableNoIndication
import com.shishir.cashoutcounter.ui.theme.AppBackground
import com.shishir.cashoutcounter.ui.theme.BkashPinkLight
import com.shishir.cashoutcounter.ui.theme.FacebookBlue
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.LabelTertiary

private const val URL_GITHUB_PROFILE = "https://github.com/Shishir-ip"
private const val URL_GITHUB_PROJECT = "https://github.com/Shishir-ip/Bkash-Cashout-Counter"
private const val URL_FACEBOOK = "https://facebook.com/imshishr"
private const val URL_WEB_VERSION =
    "https://github.com/Shishir-ip/Bkash-Cashout-Balance-Counter"

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val versionName = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "?"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.ic_app_logo),
                contentDescription = "Cashout Counter app icon",
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(22.dp)),
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Cashout Counter",
                style = MaterialTheme.typography.headlineMedium,
                color = LabelPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Version $versionName",
                style = MaterialTheme.typography.bodySmall,
                color = LabelSecondary,
            )
            Spacer(Modifier.height(24.dp))
        }

        Column(Modifier.padding(horizontal = 16.dp)) {
            // ── Developer ──
            IosCard {
                Text(
                    text = "DEVELOPED BY",
                    style = MaterialTheme.typography.labelMedium,
                    color = LabelTertiary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Shohidul Islam Shishir",
                    style = MaterialTheme.typography.titleMedium,
                    color = LabelPrimary,
                )
            }
            Spacer(Modifier.height(12.dp))

            // ── Links ──
            IosCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                LinkRow(Icons.Filled.Person, "GitHub Profile", LabelPrimary) {
                    context.openUrl(URL_GITHUB_PROFILE)
                }
                CardDivider(Modifier.padding(start = 52.dp))
                LinkRow(Icons.Filled.Code, "GitHub Project", LabelPrimary) {
                    context.openUrl(URL_GITHUB_PROJECT)
                }
                CardDivider(Modifier.padding(start = 52.dp))
                LinkRow(Icons.Filled.Facebook, "Connect on Facebook", FacebookBlue) {
                    context.openUrl(URL_FACEBOOK)
                }
                CardDivider(Modifier.padding(start = 52.dp))
                LinkRow(Icons.Filled.Language, "Web Version", LabelPrimary) {
                    context.openUrl(URL_WEB_VERSION)
                }
            }
            Spacer(Modifier.height(12.dp))

            // ── How to use ──
            IosCard {
                Text(
                    text = "How to use",
                    style = MaterialTheme.typography.titleMedium,
                    color = LabelPrimary,
                )
                Spacer(Modifier.height(8.dp))
                AboutBullet("1", "Pick Balance to see how much cash you get from your balance, or Charge to see the total deduction for a cash-out amount.")
                AboutBullet("2", "Tap a rate chip to switch rates — 13.95৳ Priyo/ATM, 18.50৳ Regular and more.")
                AboutBullet("3", "Add your own rates in the Rates tab; they're saved automatically.")
            }
            Spacer(Modifier.height(12.dp))

            // ── Disclaimer ──
            IosCard {
                Text(
                    text = "Disclaimer",
                    style = MaterialTheme.typography.titleMedium,
                    color = LabelPrimary,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Cashout Counter is an independent utility app. It is not affiliated " +
                        "with, endorsed by, or connected to bKash Limited, Nagad Ltd or any " +
                        "mobile financial service provider. Charge rates are editable and " +
                        "maintained by you — always verify current rates with official sources " +
                        "before making transactions.",
                    style = MaterialTheme.typography.bodySmall,
                    color = LabelSecondary,
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Made with ❤️ in Bangladesh",
                style = MaterialTheme.typography.bodySmall,
                color = LabelTertiary,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun AboutBullet(number: String, text: String) {
    Row(Modifier.padding(vertical = 4.dp)) {
        Text(
            text = number,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = BkashPinkLight,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = LabelSecondary,
        )
    }
}

@Composable
private fun LinkRow(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoIndication(onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = LabelPrimary,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = LabelTertiary,
            modifier = Modifier.size(16.dp),
        )
    }
}

private fun Context.openUrl(url: String) {
    runCatching {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
