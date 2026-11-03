package com.shishir.cashoutcounter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishir.cashoutcounter.ui.components.clickableNoIndication
import com.shishir.cashoutcounter.ui.screens.AboutScreen
import com.shishir.cashoutcounter.ui.screens.CounterScreen
import com.shishir.cashoutcounter.ui.screens.RatesScreen
import com.shishir.cashoutcounter.ui.theme.AppBackground
import com.shishir.cashoutcounter.ui.theme.BkashPink
import com.shishir.cashoutcounter.ui.theme.CardBorder
import com.shishir.cashoutcounter.ui.theme.LabelPrimary
import com.shishir.cashoutcounter.ui.theme.LabelSecondary
import com.shishir.cashoutcounter.ui.theme.TabBarDark

private data class TabItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val Tabs = listOf(
    TabItem("Counter", Icons.Filled.Calculate, Icons.Outlined.Calculate),
    TabItem("Rates", Icons.Filled.Tune, Icons.Outlined.Tune),
    TabItem("About", Icons.Filled.Info, Icons.Outlined.Info),
)

@Composable
fun AppRoot(viewModel: AppViewModel) {
    val rates by viewModel.rates.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .imePadding(),
    ) {
        Box(Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> CounterScreen(
                    rates = rates,
                    onOpenRates = { selectedTab = 1 },
                )
                1 -> RatesScreen(
                    rates = rates,
                    onAddRate = viewModel::addRate,
                    onUpdateRate = viewModel::updateRate,
                    onDeleteRate = viewModel::deleteRate,
                    onResetToDefaults = viewModel::resetToDefaults,
                )
                else -> AboutScreen()
            }
        }
        IosTabBar(
            tabs = Tabs,
            selectedIndex = selectedTab,
            onSelected = { selectedTab = it },
        )
    }
}

@Composable
private fun IosTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(TabBarDark.copy(alpha = 0.98f))
            .border(
                width = 0.5.dp,
                brush = androidx.compose.ui.graphics.SolidColor(CardBorder),
                shape = androidx.compose.ui.graphics.RectangleShape,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp),
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedIndex
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickableNoIndication { onSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(10.dp))
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.label,
                        tint = if (selected) BkashPink else LabelSecondary,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) BkashPink else LabelSecondary,
                    )
                }
            }
        }
    }
}
