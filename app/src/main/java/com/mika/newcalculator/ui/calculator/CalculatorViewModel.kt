package com.mika.newcalculator.ui.calculator

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mika.newcalculator.data.local.ExchangeRatePreferences
import com.mika.newcalculator.domain.CalculatorLogic
import com.mika.newcalculator.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for holding state and handling business logic for the Calculator screen.
 * It manages the display input and currency data fetching, delegating calculation to CalculatorLogic.
 */
class CalculatorViewModel(
    private val currencyRepository: CurrencyRepository,
    private val calculatorLogic: CalculatorLogic = CalculatorLogic() // Logic class injection
) : ViewModel() {

    // UI State for the calculator display
    private val _display = MutableStateFlow("0")
    val display = _display.asStateFlow()

    // UI State for the Euro to Yen conversion rate
    private val _conversionRate = MutableStateFlow(170.0) // Default value
    val conversionRate = _conversionRate.asStateFlow()

    // UI State for the date of the fetched rate
    private val _conversionDate = MutableStateFlow("")
    val conversionDate = _conversionDate.asStateFlow()

    init {
        fetchConversionRate()
    }

    /**
     * Asynchronously fetches the latest exchange rate from the repository.
     */
    private fun fetchConversionRate() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = currencyRepository.getEuroToYenRate()
                data.rate?.let { _conversionRate.value = it.toDouble() }
                data.date?.let { _conversionDate.value = it }
            } catch (e: Exception) {
                Log.e("CalculatorViewModel", "Error fetching conversion rate", e)
            }
        }
    }

    /**
     * Handles button clicks from the UI.
     */
    fun onButtonClick(value: String) {
        when (value) {
            "C" -> _display.value = "0"
            "⌫" -> _display.value = _display.value.dropLast(1).ifEmpty { "0" }
            "=" -> calculateResult()
            "%" -> {
                _display.value += value
                calculateResult()
            }
            else -> {
                _display.value = if (_display.value == "0") value else _display.value + value
            }
        }
    }

    private fun calculateResult() {
        // Delegate the complex calculation to CalculatorLogic
        _display.value = calculatorLogic.calculate(_display.value)
    }

    /**
     * Factory for creating CalculatorViewModel with dependencies.
     */
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val preferences = ExchangeRatePreferences(context)
                val repository = CurrencyRepository(preferences)
                // We can just create a new instance of logic here since it holds no state
                return CalculatorViewModel(repository) as T
            }
        }
    }
}