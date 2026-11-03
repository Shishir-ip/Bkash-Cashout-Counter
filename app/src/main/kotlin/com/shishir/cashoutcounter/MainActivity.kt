package com.shishir.cashoutcounter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishir.cashoutcounter.ui.AppRoot
import com.shishir.cashoutcounter.ui.AppViewModel
import com.shishir.cashoutcounter.ui.theme.CashoutCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            CashoutCounterTheme {
                AppRoot(viewModel = viewModel())
            }
        }
    }
}
