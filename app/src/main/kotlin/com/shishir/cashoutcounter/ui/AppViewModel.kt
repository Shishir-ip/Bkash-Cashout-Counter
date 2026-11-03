package com.shishir.cashoutcounter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishir.cashoutcounter.data.RateStore
import com.shishir.cashoutcounter.model.ChargeRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds the editable charge rates and persists every change immediately.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val store = RateStore(application)

    private val _rates = MutableStateFlow(store.load())
    val rates: StateFlow<List<ChargeRate>> = _rates.asStateFlow()

    fun addRate(name: String, rate: Double) {
        mutate { list ->
            list + ChargeRate(
                id = "rate_${System.currentTimeMillis()}",
                name = name.trim(),
                rate = rate
            )
        }
    }

    fun updateRate(id: String, name: String, rate: Double) {
        mutate { list ->
            list.map { if (it.id == id) it.copy(name = name.trim(), rate = rate) else it }
        }
    }

    fun deleteRate(id: String) {
        mutate { list -> list.filterNot { it.id == id } }
    }

    fun resetToDefaults() {
        mutate { RateStore.defaults() }
    }

    private fun mutate(transform: (List<ChargeRate>) -> List<ChargeRate>) {
        viewModelScope.launch {
            val updated = transform(_rates.value)
            _rates.value = updated
            store.save(updated)
        }
    }
}
