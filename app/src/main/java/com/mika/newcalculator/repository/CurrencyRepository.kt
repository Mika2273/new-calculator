package com.mika.newcalculator.repository

import com.mika.newcalculator.data.local.ExchangeRatePreferences
import com.mika.newcalculator.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.first

data class ConversionData(
    val rate: Float?,
    val date: String?
)

/**
 * Repository class that handles data operations.
 * It decides whether to fetch data from the remote API or use locally cached data.
 */
class CurrencyRepository(
    private val preferences: ExchangeRatePreferences
) {
    /**
     * Fetches the Euro to Yen exchange rate.
     * Tries to fetch from the API first; if it fails, falls back to local storage.
     */
    suspend fun getEuroToYenRate(): ConversionData {
        // 1. Attempt to fetch from the remote API
        try {
            val response = RetrofitInstance.api.getEuroToYenRate()
            val rate = response.rates["JPY"]
            val date = response.date

            // If data is successfully fetched, save it locally for offline use
            if (rate != null && date != null) {
                preferences.saveExchangeRate(rate, date)
                return ConversionData(rate, date)
            }
        } catch (e: Exception) {
            // If API call fails (e.g., no internet), proceed to use cached data
        }

        // 2. Fallback: Use locally saved data
        // .first() gets the current snapshot of the data
        val savedRate = preferences.savedRate.first()
        val savedDate = preferences.savedDate.first()

        return ConversionData(savedRate, savedDate)
    }
}